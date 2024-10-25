//package com.udise.portal.config;
//
//import com.udise.portal.common.JwtRequestFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
////                .authorizeHttpRequests(authorize -> authorize
////                        .requestMatchers("/ws","/emit","/**", "/v1/login/**", "/register", "/start", "/v1/sign_up/**", "/api/upload","v1/fileupload/upload").permitAll() // Allow these paths
////                        .anyRequest().authenticated() // All other requests require authentication
////                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
//                )
//                .addFilterAfter(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Register your filter
//
//        return http.build();
//    }
//}


package com.udise.portal.config;

import com.udise.portal.common.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .cors(cors -> cors.configurationSource(request -> new org.springframework.web.cors.CorsConfiguration().applyPermitDefaultValues())) // Enable CORS with default values
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
                )
                .addFilterAfter(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Register JWT filter

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all endpoints
                        .allowedOrigins("*") // Allow requests from your frontend during development
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow cookies and credentials
            }
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                // Route all non-API requests to index.html
                registry.addViewController("/{spring:[^\\.]*}")
                        .setViewName("forward:/index.html");
            }

        };
    }
}
