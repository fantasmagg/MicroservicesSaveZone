package spring_boot_webflux_client_model.client.app.handler;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import spring_boot_webflux_client_model.client.app.dto.HistorialUrlDto;
import spring_boot_webflux_client_model.client.app.models.UrlDto;
import spring_boot_webflux_client_model.client.app.models.services.UrlService;

@Component
public class UrlHandler {

	@Autowired
	private UrlService services;
	
	public Mono<ServerResponse> getUrls(ServerRequest request){
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(services.getUrls(),HistorialUrlDto.class);
	}
	
	public Mono<ServerResponse> crear(ServerRequest request){
		
		Mono<UrlDto> url = request.bodyToMono(UrlDto.class);
		return url.flatMap(u -> {
			if(u.getUrl()==null) {
				u.setUrl("https://exmple.com/");
			}
			return services.save(u);
		}).flatMap(u-> ServerResponse.created(URI.create("/api/client/".concat(u.getUrl())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(u)));
		
		
		
	}
	
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
