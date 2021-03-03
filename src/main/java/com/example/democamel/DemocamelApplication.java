package com.example.democamel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemocamelApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemocamelApplication.class, args);
    }

    /*protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                rest("/hello").get()
                        .to("direct:hello");

                from("direct:hello")
                        .log(LoggingLevel.INFO, "Hello World")
                        .transform().simple("Hello World");

            }
        };
    }*/
}
