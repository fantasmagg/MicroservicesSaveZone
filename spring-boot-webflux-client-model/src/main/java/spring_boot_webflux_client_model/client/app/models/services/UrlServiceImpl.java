package spring_boot_webflux_client_model.client.app.models.services;

import java.time.Duration;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import spring_boot_webflux_client_model.client.app.dto.VirusTotalAttributesDTO;
import spring_boot_webflux_client_model.client.app.models.UrlDto;
import spring_boot_webflux_client_model.client.app.models.VirusTotalAnalysisDTO;

@Service
public class UrlServiceImpl  implements UrlService{

	
	
	private final WebClient client;
	
	private final WebClient webClient2;

	    public UrlServiceImpl(@Qualifier("registrarWebClient") WebClient.Builder webClientBuilder1,
	                             @Qualifier("registrarWebClientPdf") WebClient.Builder webClientBuilder2) {
	        this.client = webClientBuilder1.build();
	        this.webClient2 = webClientBuilder2.build();
	    }
	
	@Value("${clave.base.api}")
	private String apiKey;
	
	@Override
	public Mono<UrlDto> save(UrlDto url) {

		return client.post()
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(url))
				.retrieve()
				.bodyToMono(UrlDto.class);
	}

	@Override
	public Mono<VirusTotalAnalysisDTO> save(FilePart pdf) {

		MultipartBodyBuilder parts = new MultipartBodyBuilder();
		parts.asyncPart("file", pdf.content(), DataBuffer.class)
			.headers(h ->{
				h.setContentDispositionFormData("file", pdf.filename());
			});
		
		return webClient2.post()
				.uri("/files")
				.header("x-apikey", apiKey)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromValue(parts.build()))
				.retrieve()
				.bodyToMono(VirusTotalAnalysisDTO.class);
	}
	
	public Mono<VirusTotalAttributesDTO> getAnalisis(String id) {
        return fetchAnalysis(id)
                .flatMap(analysis -> {
                    if ("completed".equals(analysis.getData().getAttributes().getStatus())) {
                        return Mono.just(analysis);
                    } else {
                        return Mono.empty();
                    }
                })
                .repeatWhenEmpty(flux -> flux.delayElements(Duration.ofSeconds(10)))
                .doOnError(error -> {
                    System.err.println("Error al obtener el análisis: " + error.getMessage());
                });
    }

	
	public Mono<VirusTotalAttributesDTO> fetchAnalysis(String id) {
		// TODO Auto-generated method stub
		return webClient2.get()
				.uri("/analyses/{id}", Collections.singletonMap("id", id))
				.header("x-apikey", apiKey)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(VirusTotalAttributesDTO.class);
	}
	
}
