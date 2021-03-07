package com.example.democamel.route;

import com.example.democamel.errors.CustomException;
import com.example.democamel.model.Dummy;
import com.example.democamel.service.DummyService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Route extends RouteBuilder {
    @Autowired
    DummyService dummyService;

    @Override
    public void configure() throws Exception {
        //CamelContext context = new DefaultCamelContext();
        Dummy dummy = new Dummy();
        dummy.setId(1);
        dummy.setName("dummy_1");
        dummyService.create(dummy);
        Dummy dummy2 = new Dummy();
        dummy2.setId(2);
        dummy2.setName("dummy_2");
        dummyService.create(dummy2);
        //
        onException(CustomException.class)
                .maximumRedeliveries(2)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .backOffMultiplier(2)
                .maximumRedeliveryDelay(60000)
                .useExponentialBackOff()
                .handled(true)
                .to("bean:camelRetryExceptionHandlerService?method=handleCustomException");

         /*from("jetty:http://0.0.0.0:9080/testtolog").to("log:write to log");
        from("jetty:http://0.0.0.0:9080/probe")
                .routeId("first-route")
                .to("direct:remoteService");
        from("direct:remoteService")
                .routeId("seconde-route")
                .tracing()
                .log(">>> 1")
                .log(">>> 2")
                .transform().simple("I'm Alive !")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
       */
        restConfiguration().component("jetty").host("localhost").port(9080)
                // use camel-http as rest client
                .producerComponent("http");
        //
        from("direct:probe").to("rest:get:probe");
        rest("/probe").get().route().transform().simple("I'm Alive !")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
        //
        from("direct:start").to("rest:get:dummys/{id}");
        // use the rest DSL to define the rest services
        rest("/dummys/").get("{id}").route().to("mock:input")
                .to("bean:dummyService?method=findDummy(${header.id})")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        Dummy myDummy = exchange.getIn().getBody(Dummy.class);
                        exchange.getMessage().setBody(myDummy.getName());
                    }
                });
    }
}
