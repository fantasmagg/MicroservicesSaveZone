package spring_boot_webflux_client_model.client.app.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import spring_boot_webflux_client_model.client.app.models.UrlDto;
import spring_boot_webflux_client_model.client.app.models.services.UrlService;

@Component
public class UrlHandler {

	@Autowired
	private UrlService service;
	
	public Mono<ServerResponse> crear(ServerRequest request){
		
		Mono<UrlDto> url = request.bodyToMono(UrlDto.class);
		return url.flatMap(u -> {
			if(u.getUrl()==null) {
				u.setUrl("https://exmple.com/");
			}
			return service.save(u);
		}).flatMap(u-> ServerResponse.created(URI.create("/api/client/".concat(u.getUrl())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(u)));
		
		
		
	}
	
}
