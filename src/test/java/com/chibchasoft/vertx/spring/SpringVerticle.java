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

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * @author <a href="mailto:jvelez@chibchasoft.com">Juan Velez</a> 
 */
@Component("springVerticle")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpringVerticle extends AbstractVerticle {
    public SpringVerticle() {
        System.out.println("Creating new instance of SpringVerticle");
    }

    @Override
    public void start(Future<Void> startFuture) {
        startFuture.complete();
    }

    @Override
    public void stop() {
        System.out.println("Stopping instance of SpringVerticle");
    }

    @PreDestroy
    public void preDestroy() {
        stop();
    }
}
