package me.glux.omd.rest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import me.glux.omd.rest.anno.JsonApi;
import me.glux.omd.rest.anno.JsonParam;
import me.glux.omd.rest.exception.InvokeBaseException;
import me.glux.omd.rest.view.AbstractInvokeResult;
import me.glux.omd.rest.view.InvokeErrorResult;
import me.glux.omd.rest.view.JsonView;
import me.glux.omd.rest.view.StandardError;
import me.glux.omd.rest.view.InvokeSuccessResult;

public class JsonRestMappingHandlerAdapter extends AbstractHandlerMethodAdapter {
    public static final Logger logger = LoggerFactory.getLogger(JsonRestMappingHandlerAdapter.class);
    private static Pattern jsonPattern = Pattern.compile(".*/json.*");
    private ObjectMapper mapper = new ObjectMapper();
    private JsonView jsonView = new JsonView();

    public JsonRestMappingHandlerAdapter() {
    }

    public void setJsonView(JsonView jsonView) {
        this.jsonView = jsonView;
    }

    @Override
    protected boolean supportsInternal(HandlerMethod handlerMethod) {
        return null != handlerMethod.getMethodAnnotation(JsonApi.class);
    }

    @Override
    protected long getLastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod) {
        return 0;
    }

    @Override
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        AbstractInvokeResult invokeResult;
        try {
            Object result = invoke(request, handlerMethod);
            invokeResult = new InvokeSuccessResult(result);
        } catch (InvokeBaseException e) {
            invokeResult = new InvokeErrorResult(e.getCode(), e.getErrorMessage());
        } catch (Throwable e) {
            logger.error("Error api", e);
            invokeResult = new InvokeErrorResult(StandardError.SI_UNKNWN_ERROR, "Server internal error.");
        }
        Map<String, Object> model = new HashMap<>();
        model.put(JsonView.RESULT_FIELD, invokeResult);
        ModelAndView mav = new ModelAndView(jsonView, model);
        return mav;
    }

    private Object invoke(HttpServletRequest request, HandlerMethod handlerMethod) {
        Object[] args = null;
        if (handlerMethod.getMethodParameters().length == 0) {
            args = new Object[] {};
        } else {
            String contentType = request.getContentType();
            if (contentType == null || !jsonPattern.matcher((contentType = contentType.trim().toLowerCase())).matches()) {
                throw new InvokeBaseException(StandardError.FM_UNSUPPORT_CONTENT_ERROR, "Unsupport content type ,only json is accepted.");
            }
            JsonNode root = null;
            try {
                root = mapper.readTree(request.getInputStream());
            } catch (JsonProcessingException e) {
                throw new InvokeBaseException(StandardError.FM_NOT_JSON_INPUT_ERROR, "Input is not valid json.");
            } catch (IOException e) {
                throw new InvokeBaseException(StandardError.SI_READ_INPUT_ERROR, "Unable to read input,may be network error.");
            }

            if (root.isArray()) {
                args = handleArray(ArrayNode.class.cast(root), handlerMethod);
            } else if (root.isObject()) {
                args = handleObject(ObjectNode.class.cast(root), handlerMethod);
            } else {
                throw new InvokeBaseException(StandardError.FM_UNSUPPORT_JSON_TYPE, "Unsupport json value type, only OBJECT or ARRAY accepted.");
            }
        }
        return processInvoke(handlerMethod, args);
    }

    private Object[] handleArray(ArrayNode arrayParams, HandlerMethod method) {
        MethodParameter[] params = method.getMethodParameters();
        Object[] args = null;
        if (params.length != arrayParams.size()) {
            throw new IllegalArgumentException();
        } else {
            args = new Object[params.length];
            int i = 0;
            for (MethodParameter param : params) {
                args[i] = jsonToParamValue(arrayParams.get(i), param.getGenericParameterType(), i + 1);
                i++;
            }
        }
        return args;
    }

    private Object processInvoke(HandlerMethod method, Object[] args) {
        try {
            return method.getMethod().invoke(method.getBean(), args);
        } catch (InvocationTargetException e) {
            Throwable targetE = e.getTargetException();
            logger.error("Invoke error.", targetE);
            if (InvokeBaseException.class.isInstance(targetE)) {
                throw (InvokeBaseException) targetE;
            } else {
                throw new InvokeBaseException(StandardError.API_UNDEFINED_INTER_ERROR, targetE.getMessage());
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            logger.error("Invoke error.", e);
            throw new InvokeBaseException(StandardError.SI_INVOKE_METHOD_ERROR, "Server internal error.");
        }
    }

    private Object[] handleObject(ObjectNode objectNode, HandlerMethod method) {
        MethodParameter[] params = method.getMethodParameters();
        Object[] args = new Object[params.length];
        int i = 0;
        for (MethodParameter param : params) {
            args[i] = processParameter(objectNode, param);
            i++;
        }
        return args;
    }

    private Object processParameter(ObjectNode objectNode, MethodParameter param) {
        JsonParam anno = param.getParameterAnnotation(JsonParam.class);
        if (null == anno) {
            logger.error("Parameter at [" + param.getParameterIndex() + "] of api [" + param.getMethod().getName() + "] missing annotation @JsonParam.");
            throw new InvokeBaseException(StandardError.SI_PARAM_MISSING_ANNO, "Server internal error:Api config error.");
        }
        String paramName = anno.value();
        if ((paramName = paramName.trim()).length() == 0) {
            logger.error("Parameter at [" + param.getParameterIndex() + "] of api [" + param.getMethod().getName() + "] with blank parameter name.");
            throw new InvokeBaseException(StandardError.SI_PARAM_NAME_BLANK, "Server internal error:Api config error.");
        }
        JsonNode json = objectNode.get(paramName);
        if (null == json && !anno.defaultValue().equals(ValueConstants.DEFAULT_NONE)) {
            try {
                json = mapper.readTree(anno.defaultValue());
            } catch (JsonProcessingException e) {
                logger.error("Parameter at [" + param.getParameterIndex() + "] of api [" + param.getMethod().getName() + "] is not valid value.");
                throw new InvokeBaseException(StandardError.SI_PARAM_DEFAULT_ERROR, "Server internal error:Api config error.");
            } catch (IOException e) {
                // shoule not reach
            }
        }
        if (null == json && anno.required()) {
            throw new InvokeBaseException(StandardError.FM_PARAM_MISSING_ERROR, "Parameter name [" + anno.value() + "] is missing.");
        }
        return jsonToParamValue(json, param.getGenericParameterType(), paramName);
    }

    private Object jsonToParamValue(JsonNode node, Type type, Object index) {
        JsonParser paramJsonParser = mapper.treeAsTokens(node);
        JavaType paramJavaType = TypeFactory.defaultInstance().constructType(type);
        try {
            return mapper.readValue(paramJsonParser, paramJavaType);
        } catch (IOException e) {
            if (String.class.isInstance(index)) {
                throw new InvokeBaseException(StandardError.FM_PARAM_VALUE_ERROR, "Invalid value for parameter named '" + index.toString() + "'");
            } else {
                throw new InvokeBaseException(StandardError.FM_PARAM_VALUE_ERROR, "Invalid value for parameter at index '" + index.toString() + "'");
            }
        }
    }
}