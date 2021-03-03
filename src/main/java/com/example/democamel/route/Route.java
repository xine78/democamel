package com.example.democamel.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import org.apache.camel.model.rest.RestBindingMode;

@Component
public class Route extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest("/hello").get()
                .to("direct:hello");

        from("direct:hello")
                .log(LoggingLevel.INFO, "Hello World")
                .transform().simple("Hello World");
    }
}
