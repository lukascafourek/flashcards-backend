package cz.cvut.fel.cafoulu1.flashcards.backend.config;

import cz.cvut.fel.cafoulu1.flashcards.backend.controller.interceptors.LoggerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for interceptors.
 */
@Component
public class InterceptorAppConfig implements WebMvcConfigurer {
    private final LoggerInterceptor loggerInterceptor;

    @Autowired
    public InterceptorAppConfig(LoggerInterceptor loggerInterceptor) {
        this.loggerInterceptor = loggerInterceptor;
    }

    // add rest api entity path patterns later
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor).addPathPatterns();
    }
}
