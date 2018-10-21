package com.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class RequestMappingHandlerConfiguration {

    @Inject
    private List<RequestMappingHandlerAdapter> handlerAdapters;

    @Inject
    private List<CustomHandlerMethodArgumentResolver> customHandlerMethodArgumentResolvers;

    @PostConstruct
    public void prioritizeCustomMethodHandlers() {
        handlerAdapters.forEach(a -> {
            List<HandlerMethodArgumentResolver> originalArgumentResolvers = new ArrayList<>(a.getArgumentResolvers());
            List<HandlerMethodArgumentResolver> originalCustomResolvers = a.getCustomArgumentResolvers();
            originalArgumentResolvers.removeAll(originalCustomResolvers);
            originalArgumentResolvers.addAll(0, originalCustomResolvers);
            originalArgumentResolvers.addAll(0, customHandlerMethodArgumentResolvers);
            a.setArgumentResolvers(originalArgumentResolvers);
        });
    }
}