package com.example.democamel.route;

import com.example.democamel.errors.CustomException;
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

        onException(CustomException.class)
                .maximumRedeliveries(2)
                .retryAttemptedLogLevel(LoggingLevel.WARN)
                .backOffMultiplier(2)
                .maximumRedeliveryDelay(60000)
                .useExponentialBackOff()
                .handled(true)
                .to("bean:camelRetryExceptionHandlerService?method=handleCustomException");

        rest("/hello").get()
                .to("direct:hello");

        from("direct:hello")
                .log(LoggingLevel.INFO, "Hello World")
                .transform().simple("Hello World");

    }
}
