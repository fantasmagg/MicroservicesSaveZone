package spring_boot_webflux_client_model.client.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import spring_boot_webflux_client_model.client.app.models.UrlDto;

@Service
public class UrlServiceImpl  implements UrlService{

	@Autowired
	private WebClient.Builder client;
	
	@Override
	public Mono<UrlDto> save(UrlDto url) {

		return client.build().post()
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(url))
				.retrieve()
				.bodyToMono(UrlDto.class);
	}

}
