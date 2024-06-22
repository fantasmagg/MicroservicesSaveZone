package com.spring.boot.webflux.virustotal.app.dtos.services;

import org.springframework.http.codec.multipart.FilePart;

import com.spring.boot.webflux.virustotal.app.dto.VirusTotalAttributesDTO;
import com.spring.boot.webflux.virustotal.app.dtos.VirusTotalAnalysisDTO;

import reactor.core.publisher.Mono;

public interface FilesServices {

	public Mono<VirusTotalAnalysisDTO> save(FilePart pdf);
	public Mono<VirusTotalAttributesDTO> getAnalisis (String id);
}
