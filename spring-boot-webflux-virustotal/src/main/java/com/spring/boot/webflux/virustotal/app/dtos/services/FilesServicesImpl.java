package com.spring.boot.webflux.virustotal.app.dtos.services;



import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.spring.boot.webflux.virustotal.app.dto.VirusTotalAttributesDTO;
import com.spring.boot.webflux.virustotal.app.dtos.VirusTotalAnalysisDTO;

import reactor.core.publisher.Mono;
@Service
public class FilesServicesImpl implements FilesServices{

	@Autowired
	private WebClient.Builder client;
	
	@Value("${clave.base.api}")
	private String apiKey;
	
	@Override
	public Mono<VirusTotalAnalysisDTO> save(FilePart pdf) {

		MultipartBodyBuilder parts = new MultipartBodyBuilder();
		parts.asyncPart("file", pdf.content(), DataBuffer.class)
			.headers(h ->{
				h.setContentDispositionFormData("file", pdf.filename());
			});
		
		return client.build().post()
				.uri("/files")
				.header("x-apikey", apiKey)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromValue(parts.build()))
				.retrieve()
				.bodyToMono(VirusTotalAnalysisDTO.class);
	}

	@Override
	public Mono<VirusTotalAttributesDTO> getAnalisis(String id) {
		// TODO Auto-generated method stub
		return client.build().get()
				.uri("/analyses/{id}", Collections.singletonMap("id", id))
				.header("x-apikey", apiKey)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(VirusTotalAttributesDTO.class);
	}

}
