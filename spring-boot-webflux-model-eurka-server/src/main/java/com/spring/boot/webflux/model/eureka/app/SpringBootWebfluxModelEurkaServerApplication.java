package com.spring.boot.webflux.model.eureka.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SpringBootWebfluxModelEurkaServerApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxModelEurkaServerApplication.class, args);
	}


}
