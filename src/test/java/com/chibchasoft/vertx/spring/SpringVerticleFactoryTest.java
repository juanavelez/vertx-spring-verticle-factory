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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.vertx.test.core.VertxTestBase;

/**
 * @author <a href="mailto:jvelez@chibchasoft.com">Juan Velez</a> 
 */
public class SpringVerticleFactoryTest extends VertxTestBase {
    @BeforeClass
    public static void createAppCtx() {
        ApplicationContext appCtx = new AnnotationConfigApplicationContext(AnnotatedConfiguration.class);
        ApplicationContextProvider.setApplicationContext(appCtx);
    }

    @AfterClass
    public static void closeAppCtx() {
        ApplicationContext appCtx = ApplicationContextProvider.getApplicationContext();
        ((AnnotationConfigApplicationContext) appCtx).close();
    }

    @Test
    public void testVerticleDeployed() {
        vertx.deployVerticle("spring:springVerticle", ar -> {
            String result = ar.result();
            assertNotNull(result);
            testComplete();
        });
        await();
    }

    @Test
    public void testVerticleDoesNotExist() {
        vertx.deployVerticle("spring:SpringVerticle", ar -> {
            assertTrue(ar.failed());
            assertTrue(ar.cause().getMessage().toLowerCase().contains("no bean found for"));
            testComplete();
        });
        await();
    }

    @Test
    public void testVerticleNotPrototypeScope() {
        vertx.deployVerticle("spring:springSingletonVerticle", ar -> {
            assertTrue(ar.failed());
            assertTrue(ar.cause().getMessage().toLowerCase().contains("needs to be of prototype scope"));
            testComplete();
        });
        await();
    }
}
