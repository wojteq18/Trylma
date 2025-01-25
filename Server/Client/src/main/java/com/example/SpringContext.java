package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class SpringContext {
    private final ApplicationContext context;

    public SpringContext(Class<?> mainClass) {
        context = SpringApplication.run(mainClass);
    }

    public <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
