package me.glux.omd.rest.view;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonView implements View {
    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);
    private static final Charset defaultCharset = Charset.forName("utf-8");
    public static final String RESULT_FIELD = "result";
    private static final String JSON_MIME = "application/json";
    private static final RenderHttpFrame UNKNOWN_RESPONSE_ERROR;
    static {
        UNKNOWN_RESPONSE_ERROR = new RenderHttpFrame();
        UNKNOWN_RESPONSE_ERROR.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        UNKNOWN_RESPONSE_ERROR.setHeaders(null);
        InvokeErrorResult unknwon = new InvokeErrorResult();
        unknwon.setCode(StandardError.SI_UNKNWN_ERROR);
        unknwon.setMessage("Server internal error.");
        UNKNOWN_RESPONSE_ERROR.setContent(unknwon);
    }
    public static final Object NULL_VALUE = "null";
    private static final String RANDER_OPTION_PRETTY_JSON = "Json-Pretty";
    private static final String RANDER_OPTION_ESCAPE_JSON = "Json-Escape";

    private ObjectMapper mapper = new ObjectMapper();

    public JsonView() {
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    @Autowired(required = false)
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String getContentType() {
        return JSON_MIME;
    }

    @SuppressWarnings("unchecked")
    private <T> T getModelValue(Map<String, ?> model, Class<?> clazz, String field) {
        Object result = model.get(field);
        if (null == result)
            return null;
        if (clazz.isInstance(result)) {
            return (T) result;
        } else {
            return null;
        }
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AbstractInvokeResult invokeRespone = getModelValue(model, AbstractInvokeResult.class, RESULT_FIELD);
        RenderHttpFrame renderResponse = null;
        if (null == invokeRespone) {
            renderResponse = UNKNOWN_RESPONSE_ERROR;
        } else {
            InvokeStatus status = invokeRespone.getStatus();
            if (null == status) {
                renderResponse = UNKNOWN_RESPONSE_ERROR;
            } else {
                renderResponse = new RenderHttpFrame();
                renderResponse.setContent(invokeRespone);
                if (status == InvokeStatus.SUCCESS) {
                    renderResponse.setHttpStatus(HttpServletResponse.SC_OK);
                } else {
                    renderResponse.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
        ObjectWriter wirter = getWriter(request);
        writeResponse(renderResponse, wirter, response);
    }

    protected ObjectWriter getWriter(HttpServletRequest request) {
        Boolean pretty = isHeaderSet(request, RANDER_OPTION_PRETTY_JSON, false);
        Boolean escape = isHeaderSet(request, RANDER_OPTION_ESCAPE_JSON, false);
        ObjectWriter writer = null;
        if (pretty) {
            writer = mapper.writerWithDefaultPrettyPrinter();
        } else {
            writer = mapper.writer();
        }
        if (escape) {
            writer.with(Feature.ESCAPE_NON_ASCII);
        } else {
            writer.without(Feature.ESCAPE_NON_ASCII);
        }
        return writer;
    }

    private boolean isHeaderSet(HttpServletRequest request, String header, boolean defaultValue) {
        Boolean value = defaultValue;
        String valueStr;
        if (null != (valueStr = request.getHeader(header)) && (valueStr = valueStr.trim()).length() > 0) {
            try {
                Boolean option = Boolean.parseBoolean(valueStr.toLowerCase());
                if (null != option) {
                    value = option;
                }
            } catch (Exception e) {
                // noop
            }
        }
        return value;
    }

    private void writeResponse(RenderHttpFrame frame, ObjectWriter writer, HttpServletResponse response) {
        response.setStatus(frame.getHttpStatus());
        response.setContentType(JSON_MIME);
        response.setCharacterEncoding(defaultCharset.displayName());
        response.addDateHeader("Expires", 1L);
        response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
        response.addHeader("Pragma", "no-cache");
        Map<String, String> extraHeader = frame.getHeaders();
        if (extraHeader != null && extraHeader.size() > 0) {
            for (Entry<String, String> entry : extraHeader.entrySet()) {
                response.addHeader(entry.getKey(), entry.getValue());
            }
        }
        /* response.setContentLength(responseContent.length); */
        try {
            writer.writeValue(response.getOutputStream(), frame.getContent());
        } catch (IOException e) {
            logger.info("Write response error.", e);
        }
    }

    private static class RenderHttpFrame {
        private int httpStatus;
        private Map<String, String> headers;
        private AbstractInvokeResult content;

        public int getHttpStatus() {
            return httpStatus;
        }

        public void setHttpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public AbstractInvokeResult getContent() {
            return content;
        }

        public void setContent(AbstractInvokeResult content) {
            this.content = content;
        }
    }
}