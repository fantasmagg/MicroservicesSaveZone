package spring_boot_webflux_client_model.client.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringBootWebfluxClientModelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxClientModelApplication.class, args);
	}

}
