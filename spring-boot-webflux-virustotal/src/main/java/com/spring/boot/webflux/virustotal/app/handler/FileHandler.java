package com.spring.boot.webflux.virustotal.app.handler;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.spring.boot.webflux.virustotal.app.dtos.services.FilesServices;

import reactor.core.publisher.Mono;

@Component
public class FileHandler {

	@Autowired
	private FilesServices services;
	

	
	public Mono<ServerResponse> post (ServerRequest request){
		return request.multipartData()
				.flatMap(x-> {
					FilePart filePart= (FilePart) x.toSingleValueMap().get("file");
					return services.save(filePart);
				})
				.flatMap(response -> ServerResponse.created(URI.create("/api/virus/files"))
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(response)))
				.onErrorResume(error -> {
					if(error instanceof WebClientResponseException) {
						WebClientResponseException errorResponse= (WebClientResponseException) error;
						if(errorResponse.getStatusCode()== HttpStatus.NOT_FOUND) {
							return ServerResponse.notFound().build();
						}
					}
					return Mono.error(error);
				});
	}
	
	public Mono<ServerResponse> getArch(ServerRequest request){
		String id = request.pathVariable("id");
		
		return services.getAnalisis(id)
				.flatMap(p-> 
				ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(p))
				.switchIfEmpty(ServerResponse.notFound().build())
				//esto es en caso de que no se encuentre el id al buscar el producto
				.onErrorResume(error ->{
					WebClientResponseException errorResponse =(WebClientResponseException) error;
					if(errorResponse.getStatusCode()== HttpStatus.NOT_FOUND) {
						Map<String, Object> body = new HashMap<>();
						body.put("error", "No existe el producto: ".concat(errorResponse.getMessage()));
						body.put("timestamp",new Date());
						body.put("status", errorResponse.getStatusCode());
						
						return ServerResponse.status(HttpStatus.NOT_FOUND)
								.body(BodyInserters.fromValue(body));
					}
					return Mono.error(errorResponse);
				})
						);
	}
	
} 
