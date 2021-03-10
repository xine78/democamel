package com.example.democamel.route;

import com.example.democamel.dao.DummyDao;
import com.example.democamel.model.Dummy;
import com.example.democamel.service.DummyServiceImpl;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ServiceStatus;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CamelSpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.cloud.consul.enabled=false")
@EnableAutoConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RouteTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CamelContext camelContext;

    @EndpointInject("mock:bean:dummyService")
    private MockEndpoint mockDummyService;

    private Dummy dummy;
    private ProducerTemplate template;

    @BeforeAll
    static void initAll() {

    }
    @Test
    @Order(1)
    public void testStatus() throws Exception {
        assertEquals(ServiceStatus.Started, camelContext.getStatus());
    }

    @Test
    @Order(2)
    public void testProbe() throws Exception {
        template = camelContext.createProducerTemplate();
        String out = template.requestBody("direct:probe", null, String.class);
        assertEquals("\"I'm Alive !\"", out);
        //
        template.stop();
    }

    @Test
    @Order(3)
    public void testDummy() throws Exception {
        dummy = new Dummy();
        dummy.setId(3);
        dummy.setName("dummy_3");
        //mockDummyService.
    }

}
