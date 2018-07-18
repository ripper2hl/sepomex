package com.perales.sepomex.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new HibernateAwareObjectMapper();
        objectMapper.disable( SerializationFeature.FAIL_ON_EMPTY_BEANS );
        return objectMapper;
    }
}