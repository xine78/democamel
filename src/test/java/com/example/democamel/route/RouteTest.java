package com.example.democamel.route;

import com.example.democamel.dao.DummyDao;
import com.example.democamel.model.Dummy;
import com.example.democamel.service.DummyServiceImpl;
import org.apache.camel.*;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

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
        String out = template.requestBody("direct:getProbe", null, String.class);
        assertEquals("I'm Alive !", out);
        //
        template.stop();
    }

    @Test
    @Order(3)
    public void testCount() throws Exception {
        template = camelContext.createProducerTemplate();
        Long out = template.requestBody("direct:getCount", null, Long.class);
        assertEquals(2, out);
        //
        template.stop();
    }

    @Test
    @Order(4)
    public void testCreateDummy() throws Exception {
        template = camelContext.createProducerTemplate();
        //template.sendBody("direct:createDummy", new Dummy(3, "dummy_3"));
        Dummy md = (Dummy) template.requestBody("direct:createDummy", new Dummy(3, "dummy_3"));
        assertTrue(md != null && md.getId()==3 && md.getName().equals("dummy_3"));
        //
        template.stop();
    }

}
