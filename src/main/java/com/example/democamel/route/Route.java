package com.example.democamel.route;

import com.example.democamel.errors.CustomException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class Route extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        CamelContext context = new DefaultCamelContext();
        /*restConfiguration()
                .contextPath("/titi")
                .port(9090)
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiContextRouteId("doc-api")
                //.component("servlet")
                .bindingMode(RestBindingMode.json);*/
        // OK //
        // from("timer:foo").to("log:bar");
        // from("file:/in/").to("log:bar");
        // from("direct:start").to("log:OUT");
        // from("timer://trigger").to("log:OUT");
        //OKOK
        from("jetty:http://0.0.0.0:9080/testtolog").to("log:write to log");
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

    }
}
