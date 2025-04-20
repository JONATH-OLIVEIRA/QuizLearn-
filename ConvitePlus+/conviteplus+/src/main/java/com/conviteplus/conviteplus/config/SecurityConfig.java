package com.conviteplus.conviteplus.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.SessionCookieConfig;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder(10); // Número padrão de rounds é 10
	}

	@Bean
	public ServletContextInitializer cookieCustomizer() {
		return servletContext -> {
			SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
			sessionCookieConfig.setHttpOnly(true);
			sessionCookieConfig.setSecure(false); // 🔹 Se estiver rodando em HTTP, mantenha "false"

			sessionCookieConfig.setPath("/");
			sessionCookieConfig.setMaxAge(60 * 60); // 🔹 Expira em 1 hora
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) 
	        .authorizeHttpRequests(auth -> auth
	            .anyRequest().permitAll() 
	        )
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 🔹 Garante que a sessão seja criada
	        );

	    return http.build();
	}
	
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurer() {
	        @Override
	        public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/**")
	                    .allowedOrigins("http://localhost:5173") // 🔹 Permitir acesso do frontend Vue.js
	                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 🔹 Permitir todos os métodos HTTP
	                    .allowCredentials(true);
	        }
	    };
	}

}
