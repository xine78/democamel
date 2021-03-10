package com.example.democamel.route;

import com.example.democamel.errors.CustomException;
import com.example.democamel.model.Dummy;
import com.example.democamel.service.DummyService;
import com.example.democamel.service.DummyServiceImpl;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
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
        dummyService.create(new Dummy(1, "dummy_1"));
        dummyService.create(new Dummy(2, "dummy_2"));
        //
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
                .get("/{id}")
                    .description("Gets the Dummy by ID")
                    .param()
                        .name("id")
                        .type(RestParamType.path)
                        .description("The id of the user to get")
                        .dataType("number")
                    .endParam()
                    .produces("application/json")
                    .outType(Dummy.class)
                    .to("direct:getDummy")
                .get("/probe")
                    .to("direct:getProbe")
                .delete("/{id}")
                    .param()
                        .name("id")
                        .type(RestParamType.path)
                        .description("Deletes the user from database given an ID")
                        .dataType("number")
                    .endParam()
                    .produces("application/json")
                    .outType(String.class)
                    .to("direct:deleteDummy")
                .post()
                    .type(Dummy.class)
                    .outType(Dummy.class)
                    .description("Creates a Dummy")
                    .consumes("application/json")
                    .produces("application/json")
                    .to("direct:createDummy");

        from("direct:getDummy").to("mock:input")
                .to("bean:dummyService?method=findDummy(${header.id})")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        Dummy myDummy = exchange.getIn().getBody(Dummy.class);
                        exchange.getMessage().setBody(myDummy);
                    }
                });

        from("direct:getProbe").transform().simple("I'm Alive !")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

        from("direct:createDummy")
                .to("mock:input")
                .to("bean:dummyService?method=create(${body})")
                .transform().simple("create");

        from("direct:deleteDummy")
                .to("mock:input")
                .to("bean:dummyService?method=delete(${header.id})")
                .transform().simple("delete");
    }
}
