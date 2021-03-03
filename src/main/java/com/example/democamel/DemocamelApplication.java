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

}
