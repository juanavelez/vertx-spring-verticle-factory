/*
 * Copyright (c) 2016 chibchasoft.com
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Apache License v2.0 which accompanies
 * this distribution.
 *
 *      The Apache License v2.0 is available at
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Author <a href="mailto:jvelez@chibchasoft.com">Juan Velez</a>
 */
package com.chibchasoft.vertx.spring;

import java.util.Objects;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p>The purpose of this class is to provide the actual
 * {@link ApplicationContext Spring Application Context} to be used by the {@link SpringVerticleFactory}.</p>
 * <p>The Provider can either use the class of the annotated-class that serves as the
 * Configuration ({@link org.springframework.context.annotation.Configuration Configuration})
 * for the Application Context or an actual instance of an Application Context.</p>
 * 
 * @author <a href="mailto:jvelez@chibchasoft.com">Juan Velez</a>
 */
public class ApplicationContextProvider {
    private static ApplicationContext appCtx = null;

    /**
     * Sets the annotated-configuration class to be used for the Application Context. It creates
     * an {@link org.springframework.context.annotation.AnnotationConfigApplicationContext
     * AnnotationConfigApplicationContext} using the provided annotated-configuration class.
     * @param annotatedConfigClass The class of the annotated-configuration
     */
    public static synchronized void setConfigurationClass(Class<?> annotatedConfigClass) {
        Objects.requireNonNull(annotatedConfigClass, "Annotated Configuration class is required");
        appCtx = new AnnotationConfigApplicationContext(annotatedConfigClass);
    }

    /**
     * Sets the actual application context that this provider will return.
     * @param appCtx The actual application context to be provided byt this class.
     */
    public static synchronized void setApplicationContext(ApplicationContext appCtx) {
        Objects.requireNonNull(appCtx, "Application Context is required");
        ApplicationContextProvider.appCtx = appCtx; 
    }

    /**
     * Returns the actual application context
     * @return The actual application context
     */
    public static synchronized ApplicationContext getApplicationContext() {
        return appCtx;
    }
}