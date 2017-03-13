# Vert.x Spring Verticle Factory

[![Build Status](https://travis-ci.org/juanavelez/vertx-spring-verticle-factory.svg?branch=master)](https://travis-ci.org/juanavelez/vertx-spring-verticle-factory) 

An implementation of a Vert.x Verticle Factory where the verticles are obtained via a Spring Application Context. The verticles must exist as Spring Beans whose bean scope must be PROTOTYPE. The verticles are free to use all the Spring-provided capabilities to set themselves up. This factory's createVerticle method can be considered slow, hence blockingCreate() returns true.

This implementation relies on setting a Spring Application Context (using com.chibchasoft.vertx.spring.ApplicationContextProvider) before Vert.x Verticles are deployed. The prefix for this verticle is "spring:" and the verticle name is the spring bean id/name assigned to such verticle.

##Usage:##

Vert.x Spring Verticle Factory is published to the [maven public repo](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.chibchasoft%22%20AND%20a%3A%22vertx-spring-verticle-factory%22).

Add the vertx-spring-verticle-factory dependency to your project, in case of maven like this:

```xml
        <dependency>
            <groupId>com.chibchasoft</groupId>
            <artifactId>vertx-spring-verticle-factory</artifactId>
            <version>1.0.2</version>
        </dependency>
```

Then create a Spring Application Context (either using an XML or annotation approach) and assign it to the ApplicationContextProvider:

```java
   ApplicationContext appCtx = new AnnotationConfigApplicationContext(AnnotatedSpringConfiguration.class);
   ApplicationContextProvider.setApplicationContext(appCtx);
```

Proceed to deploy the spring-bean-based verticle like this:

```java
   Vertx vertx = Vertx.vertx();
   vertx.deployVerticle("spring:mySpringVerticle", new DeploymentOptions().setInstances(2).setWorker(true));
```

**NOTE**: The Spring Application Context set in the ApplicationContextProvider is used for all the verticles created using the Spring Verticle Factory up to the point where it is changed. Once changed, the Spring Verticle Factory will use the new one when creating new verticles.

The verticle class itself can use any Spring capabilities, for example:

```java
@Component("mySpringVerticle")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MySpringVerticle extends AbstractVerticle {
  @Value("${my.property}")
  private String theProperty;

  @Inject
  private MyService myService;

  @PostConstruct
  public void init() {
     System.out.println("Perform initialization in here");
  }
}
```
