package com.util.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.inject.Named;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.configuration.CustomHandlerMethodArgumentResolver;

@Named
public class DateTimeMilliSecondsResolver implements CustomHandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(DateTimeMilliSecondsFormat.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        DateTimeMilliSecondsFormat milliSecondsFormat = methodParameter.getParameterAnnotation(DateTimeMilliSecondsFormat.class);
        Long milliSeconds = Long.valueOf(nativeWebRequest.getParameter(milliSecondsFormat.value()));
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliSeconds), ZoneId.systemDefault());
    }
}