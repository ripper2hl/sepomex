package com.perales.sepomex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.config.JsonMessageConverter;
import org.springframework.cloud.function.json.JacksonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@SpringBootApplication
@Log4j2
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        log.info("configurando cors");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
    
    @Bean
    public JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        JacksonMapper jacksonMapper = new JacksonMapper(objectMapper);
        JsonMessageConverter jsonMessageConverter = new JsonMessageConverter(jacksonMapper);
        copyDirectory("sepomex-indices", "/tmp/sepomex-indices");
        return jsonMessageConverter;
    }
    
    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)  {
        File sourceDirectory = new File(sourceDirectoryLocation);
        File destinationDirectory = new File(destinationDirectoryLocation);
        try {
            FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}