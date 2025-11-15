package com.example.demo.config;

import com.example.demo.ws.CompteSoapService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.xml.ws.Endpoint;

@Configuration
public class CxfConfig {

    private final CompteSoapService compteSoapService;
    private final Bus bus;

    public CxfConfig(CompteSoapService compteSoapService, Bus bus) {
        this.compteSoapService = compteSoapService;
        this.bus = bus;
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, compteSoapService);

        // Publier sous /ws
        endpoint.publish("/ws"); // URL finale : http://localhost:8080/ws

        // Définir explicitement l'URL publiée pour le WSDL
        endpoint.setPublishedEndpointUrl("http://localhost:8080/ws?wsdl");

        return endpoint;
    }
}
