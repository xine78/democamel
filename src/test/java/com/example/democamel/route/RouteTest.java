package com.example.democamel.route;

import com.example.democamel.model.Dummy;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ServiceStatus;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@CamelSpringBootTest
@SpringBootTest()
@TestPropertySource(locations="classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RouteTest {

    @Autowired
    private CamelContext camelContext;

    private ProducerTemplate template;

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
    public void testCreateDummy() throws Exception {
        template = camelContext.createProducerTemplate();
        Dummy md = (Dummy) template.requestBody("direct:createDummy", new Dummy(1, "dummy_1"));
        assertTrue(md != null && md.getId()==1 && md.getName().equals("dummy_1"));
        //
        md = (Dummy) template.requestBody("direct:createDummy", new Dummy(2, "dummy_2"));
        assertTrue(md != null && md.getId()==2 && md.getName().equals("dummy_2"));
        //
        template.stop();
    }

    @Test
    @Order(4)
    public void testUpdateDummy() throws Exception {
        template = camelContext.createProducerTemplate();
        Dummy md = (Dummy) template.requestBodyAndHeader("direct:updateDummy"
                , new Dummy(1, "dummy_111")
                , "id"
                , 1);
        assertTrue(md != null && md.getId()==1 && md.getName().equals("dummy_111"));
        //
        template.stop();
    }

    @Test
    @Order(5)
    public void testDeleteDummy() throws Exception {
        template = camelContext.createProducerTemplate();
        template.sendBodyAndHeader("direct:deleteDummy", null, "id", 1);
        //
        Dummy md = (Dummy) template.requestBodyAndHeader("direct:getDummy", null,"id", 1);
        assertNull(md);
        //
        template.stop();
    }

    @Test
    @Order(6)
    public void testCount() throws Exception {
        template = camelContext.createProducerTemplate();
        //
        Long out = template.requestBody("direct:getCount", null, Long.class);
        assertEquals(1, out);
        //
        template.stop();
    }

}
