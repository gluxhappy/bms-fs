package me.glux.omd.rest;

import java.io.IOException;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import me.glux.omd.rest.anno.JsonParam;

public class JsonArgumentResolver implements HandlerMethodArgumentResolver{

    private ObjectMapper mapper=new ObjectMapper();
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        JsonParam anno=parameter.getParameterAnnotation(JsonParam.class);
        String paramName=anno.value();
        String json=webRequest.getParameter(paramName);
        if(null == json && !anno.defaultValue().equals(ValueConstants.DEFAULT_NONE)){
            json=anno.defaultValue();
        }
        if(null == json && anno.required()){
            throw new IllegalArgumentException("Required JsonParam '"+anno.value()+"' is missing and no default value.");
        }
        JavaType paramJavaType = TypeFactory.defaultInstance().constructType(parameter.getGenericParameterType());
        try {
            return mapper.readValue(json, paramJavaType);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid parameter type.");
        }
    }

}
