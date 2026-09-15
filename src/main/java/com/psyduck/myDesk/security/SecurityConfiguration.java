package com.psyduck.myDesk.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


import com.psyduck.myDesk.benutzerschnittstelle.LoginView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(
	        HttpSecurity http) throws Exception {

	    http.authorizeHttpRequests(auth -> auth
	            .requestMatchers("/images/**").permitAll()
	    );

	    http.with(
	        VaadinSecurityConfigurer.vaadin(),
	        vaadin -> vaadin.loginView(LoginView.class)
	    );

	    return http.build();
	}

}
