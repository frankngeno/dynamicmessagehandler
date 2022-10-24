package com.ftc.dynamicmessagehandler.config;

import com.ftc.dynamicmessagehandler.Destinations;
import com.ftc.dynamicmessagehandler.reciever.ActiveMqReceiverRouter;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import java.util.HashMap;

@Configuration
public class connectionsConfig {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ActiveMqReceiverRouter routeBuilder;
        
    @PostConstruct
    public void postConstruct() {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();

        camelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        try {
            camelContext.addRoutes(routeBuilder);
            camelContext.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
