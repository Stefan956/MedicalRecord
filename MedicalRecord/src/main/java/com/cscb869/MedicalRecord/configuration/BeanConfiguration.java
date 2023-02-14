package com.cscb869.MedicalRecord.configuration;

import com.cscb869.MedicalRecord.commons.utilities.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BeanConfiguration {
    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Mapper mapper(ModelMapper modelMapper) {
        return new Mapper(modelMapper);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/").allowedOrigins("http://localhost:3000");
                registry.addMapping("/*").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/doctor/list").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/doctor/delete/**").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/patient/list").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/patient/delete/**").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/specialization/list").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/patient/delete/**").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/doctor//create").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/patient/create").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/specialization/create").allowedOrigins("http://localhost:3000");
            }
        };
    }
}
