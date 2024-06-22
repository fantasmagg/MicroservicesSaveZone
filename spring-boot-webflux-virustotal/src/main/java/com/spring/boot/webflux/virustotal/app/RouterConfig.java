package com.spring.boot.webflux.virustotal.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.spring.boot.webflux.virustotal.app.handler.FileHandler;

@Configuration
public class RouterConfig {

	@Bean
	public RouterFunction<ServerResponse> rutas(FileHandler handler){
		
		return RouterFunctions.route(RequestPredicates.POST("/api/virus/files"),handler::post)
				.andRoute(RequestPredicates.GET("/api/virus/analyses/{id}"), handler::getArch);
		
	}
	
}
