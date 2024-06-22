package com.spring.boot.webflux.virustotal.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

	@Value("${ruta.base.send}")
	private String rutaSend;
	
	@Bean
	public WebClient.Builder registrarWebClient(){
		return WebClient.builder().baseUrl(rutaSend);
	}
	
	
}
