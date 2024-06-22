package spring_boot_webflux_client_model.client.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

	@Value("${microservice1.base.endpoint}")
    private String url1;

    @Value("${microservice2.base.endpoint}")
    private String url2;
	
	@Bean
	public WebClient.Builder registrarWebClient() {
		return WebClient.builder().baseUrl(url1);
	}
	
	@Bean
	public WebClient.Builder registrarWebClientPdf(){
		return WebClient.builder().baseUrl(url2);
	}
	
}
