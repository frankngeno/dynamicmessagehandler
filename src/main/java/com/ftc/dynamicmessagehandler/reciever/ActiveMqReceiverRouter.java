package com.ftc.dynamicmessagehandler.reciever;

import com.ftc.dynamicmessagehandler.Destinations;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.StringReader;


@Component
public class ActiveMqReceiverRouter extends RouteBuilder {

    Processor processor;

    private final Destinations destinations;

    public ActiveMqReceiverRouter(Destinations destinations) {
        this.destinations = destinations;
    }

    @Override
    public void configure() throws Exception {

        from("jms:my-activemq-queue")
                .dynamicRouter(new Expression() {
                    @Override
                    public <T> T evaluate(Exchange exchange, Class<T> type) {
                        try {
                            if (null != exchange.getIn().getHeader("mappingComplete")) {
                                exchange.getIn().removeHeader("mappingComplete");
                                return null;
                            }

                            String body = (String) exchange.getIn().getBody();
                            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                            domFactory.setNamespaceAware(true);
                            DocumentBuilder builder = null;

                            builder = domFactory.newDocumentBuilder();

                            Document doc = null;

                            doc = builder.parse(new InputSource(new StringReader(body)));

                            XPathFactory factory = XPathFactory.newInstance();
                            XPath xpath = factory.newXPath();
                            XPathExpression expr = null;

                            expr = xpath.compile("//objects/object/@type");

                            Object result = null;

                            result = expr.evaluate(doc, XPathConstants.STRING);

                            String typeString = (String) result;

                            System.out.println("Result is: " + typeString);


                            exchange.getIn().setHeader("mappingComplete", true);

                            return (T) destinations.getRoutes().get(typeString);

                        } catch (Exception ex) {
                            log.error("Exception ", ex);
                        }
                        return null;
                    }

                })
                .to("log:received-message-from-active-mq");

    }

}
