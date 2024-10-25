package com.udise.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*") // Allow all paths
                .allowedOrigins("*") // Your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(false); // Allow credentials if needed
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Route all non-API requests to index.html
        registry.addViewController("/{spring:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}
