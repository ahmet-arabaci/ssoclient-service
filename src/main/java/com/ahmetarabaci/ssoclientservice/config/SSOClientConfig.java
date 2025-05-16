package com.ahmetarabaci.ssoclientservice.config;

import java.io.IOException;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SSOClientConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSOClientConfig.class);
	
	@Bean
	public SecurityFilterChain initSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf(t -> t.disable());	
		http.addFilterAfter(getServletPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class);
		http.sessionManagement(t -> { t.sessionCreationPolicy(SessionCreationPolicy.STATELESS); });
		return http.build();
	}
		
	private ServletPolicyEnforcerFilter getServletPolicyEnforcerFilter() {
		return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {			
			@Override
			public PolicyEnforcerConfig resolve(HttpRequest request) {
				try {
					return JsonSerialization.readValue(getClass().getResourceAsStream("/keycloak-policy.json"),
							PolicyEnforcerConfig.class);
				} catch (IOException e) {			
					LOGGER.error("getServletPolicyEnforcerFilter | IOException occurred "
							+ "while reading values from JSON file!", e);
					throw new RuntimeException(e);
				}
			}
		});
	}
}
