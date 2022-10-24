package com.ftc.dynamicmessagehandler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "destinations")
public class Destinations {

    private Map<String, String> routes;

    @PostConstruct
    public void postConstruct(){
        System.out.println( "" );

    }

}
