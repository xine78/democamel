package com.example.democamel.route;

import com.example.democamel.errors.CustomException;
import com.example.democamel.model.Dummy;
import com.example.democamel.service.DummyService;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.awt.print.Book;

@Component
public class Route extends RouteBuilder {
    @Autowired
    DummyService dummyService;

    @Override
    public void configure() throws Exception {

        onException(CustomException.class)
                .maximumRedeliveries(2)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .backOffMultiplier(2)
                .maximumRedeliveryDelay(60000)
                .useExponentialBackOff()
                .handled(true)
                .to("bean:camelRetryExceptionHandlerService?method=handleCustomException");

        restConfiguration()
                .component("jetty").host("localhost").port(9080)
                // use camel-http as rest client
                .producerComponent("http")
                // produce json
                .bindingMode(RestBindingMode.json);

        rest("/dummys")
                .get()
                    .description("Gets All the Dummys")
                    .produces("application/json")
                    .to("direct:getDummys")
                .get("/{id}")
                    .description("Gets the Dummy by ID")
                    .param()
                        .name("id")
                        .type(RestParamType.path)
                        .description("The id of the Dummy to get")
                        .dataType("number")
                    .endParam()
                    .produces("application/json")
                    .outType(Dummy.class)
                    .to("direct:getDummy")
                .get("/count")
                    .description("Count Dummys")
                    .to("direct:getCount")
                .get("/probe")
                    .description("still alive ?")
                    .to("direct:getProbe")
                .post()
                    .type(Dummy.class)
                    .outType(Dummy.class)
                    .description("Creates a Dummy")
                    .consumes("application/json")
                    .produces("application/json")
                    .to("direct:createDummy")
                .put("/{id}")
                    .type(Dummy.class)
                    .outType(Dummy.class)
                    .description("Update the Dummy by ID")
                    .consumes("application/json")
                    .produces("application/json")
                    .to("direct:updateDummy")
                .delete("/{id}")
                    .param()
                        .name("id")
                        .type(RestParamType.path)
                        .description("Deletes a Dummy from database given an ID")
                        .dataType("number")
                    .endParam()
                    .produces("application/json")
                    .outType(String.class)
                    .to("direct:deleteDummy");

        // GET
        // >Probe
        from("direct:getProbe")
                .transform()
                .simple("I'm Alive !")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
        // >Count
        from("direct:getCount")
                .to("bean:dummyService?method=count")
                .process(exchange -> {
                    Long nombre = exchange.getIn().getBody(Long.class);
                    exchange.getMessage().setBody(nombre);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

        // >All
        from("direct:getDummys")
                .to("bean:dummyService?method=findDummys()");
        //>by ID
        from("direct:getDummy")
                .to("bean:dummyService?method=findDummy(${header.id})")
                .process(exchange -> {
                        // Return the Dummy's founded Object in the response Body
                        Dummy myDummy = exchange.getIn().getBody(Dummy.class);
                        exchange.getMessage().setBody(myDummy);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

        // POST
        from("direct:createDummy")
                .to("bean:dummyService?method=create(${body})")
                .process(exchange -> {
                    Dummy myDummy = exchange.getIn().getBody(Dummy.class);
                    exchange.getMessage().setBody(myDummy);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

        // PUT
        from("direct:updateDummy")
                .to("bean:dummyService?method=updateDummy(${header.id}, ${body})")
                .process(exchange -> {
                    Dummy myDummy = exchange.getIn().getBody(Dummy.class);
                    exchange.getMessage().setBody(myDummy);
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

        // DELETE
        from("direct:deleteDummy")
                .to("bean:dummyService?method=delete(${header.id})")
                .transform()
                .simple("deleted")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
    }
}
