/*
 * Copyright (c) 2016 chibchasoft.com
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Apache License v2.0 which accompanies
 * this distribution.
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * Author <a href="mailto:jvelez@chibchasoft.com">Juan Velez</a>
 */
package com.chibchasoft.vertx.spring;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.context.ApplicationContext;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;

/**
 * <p>A Verticle Factory that relies on a {@link ApplicationContext Spring Application Context}
 * to get the requested Verticles. A verticleName must match a bean name/id within the Application
 * Context.</p>
 * <p>This factory relies on {@link ApplicationContextProvider} to provide the actual
 * Spring Application Context.</p>
 * <p>Verticles themselves should be prototype scope.</p>
 * 
 * @author <a href="mailto:jvelez@chibchasoft.com">Juan Velez</a>
 */
public class SpringVerticleFactory implements VerticleFactory {
    private static final String PREFIX = "spring";

    private static AtomicReference<ApplicationContext> appCtx = new AtomicReference<>();
    private static AtomicBoolean instantiationHappening = new AtomicBoolean(false);

    public SpringVerticleFactory() {
        if (appCtx.get() == null) {
            // Prevent recursive entrance
            if ( !instantiationHappening.compareAndSet(false, true) ) return;
            ApplicationContext theAppCtx = ApplicationContextProvider.getApplicationContext();
            if (theAppCtx==null) {
                throw new IllegalStateException("No Application Context Instance has been set in "
                    + "ApplicationContextProvider.");
            } else {
                appCtx.compareAndSet(null, theAppCtx);
            }
            instantiationHappening.compareAndSet(true, false);
        }
    }

    /* (non-Javadoc)
     * @see io.vertx.core.spi.VerticleFactory#prefix()
     */
    @Override
    public String prefix() {
        return PREFIX;
    }

    /* (non-Javadoc)
     * @see io.vertx.core.spi.VerticleFactory#blockingCreate()
     */
    @Override
    public boolean blockingCreate() {
        return true;
    }

    /* (non-Javadoc)
     * @see io.vertx.core.spi.VerticleFactory#createVerticle(java.lang.String, java.lang.ClassLoader)
     */
    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        verticleName = VerticleFactory.removePrefix(verticleName);
        ApplicationContext ctx = appCtx.get();
        if (!ctx.containsBean(verticleName))
            throw new IllegalArgumentException(String.format("No bean found for %s", verticleName));

        if (!ctx.isPrototype(verticleName))
            throw new IllegalArgumentException(String.format("Bean %s needs to be of Prototype scopoe", verticleName));

        Verticle verticle = (Verticle) ctx.getBean(verticleName);
        if (verticle==null)
            throw new IllegalArgumentException(String.format("No bean found for %s", verticleName));
        return verticle;
    }
}