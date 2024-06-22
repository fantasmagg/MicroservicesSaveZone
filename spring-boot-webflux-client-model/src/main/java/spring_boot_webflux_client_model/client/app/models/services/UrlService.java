package spring_boot_webflux_client_model.client.app.models.services;

import org.springframework.http.codec.multipart.FilePart;


import reactor.core.publisher.Mono;
import spring_boot_webflux_client_model.client.app.dto.VirusTotalAttributesDTO;
import spring_boot_webflux_client_model.client.app.models.UrlDto;
import spring_boot_webflux_client_model.client.app.models.VirusTotalAnalysisDTO;

public interface UrlService {

	public Mono<UrlDto> save (UrlDto url);
	public Mono<VirusTotalAnalysisDTO> save(FilePart pdf);
	public Mono<VirusTotalAttributesDTO> getAnalisis (String id);
	
}
