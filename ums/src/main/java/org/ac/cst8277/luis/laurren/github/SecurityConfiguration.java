package org.ac.cst8277.luis.laurren.github;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {

	@Bean
	SecurityWebFilterChain configure(ServerHttpSecurity http) {
		return http
				.authorizeExchange(exchanges -> exchanges
						.pathMatchers("/login/**").permitAll()
						.pathMatchers("/user/**").permitAll()
						.pathMatchers("/users/**").permitAll()
						.pathMatchers("/roles/**").permitAll()
						.pathMatchers("/git/**").permitAll()
						.anyExchange().authenticated())
				.oauth2Login(withDefaults()).csrf().disable().oauth2Client(withDefaults()).build();
	}
}
