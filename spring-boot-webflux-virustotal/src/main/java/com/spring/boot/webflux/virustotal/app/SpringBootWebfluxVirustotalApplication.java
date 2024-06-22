package com.spring.boot.webflux.virustotal.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringBootWebfluxVirustotalApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxVirustotalApplication.class, args);
	}


}
