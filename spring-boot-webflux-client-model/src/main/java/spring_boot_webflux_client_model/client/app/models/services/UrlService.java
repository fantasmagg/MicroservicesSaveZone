package spring_boot_webflux_client_model.client.app.models.services;

import reactor.core.publisher.Mono;
import spring_boot_webflux_client_model.client.app.models.UrlDto;

public interface UrlService {

	public Mono<UrlDto> save (UrlDto url);
	
}
