package spring_boot_webflux_client_model.client.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import spring_boot_webflux_client_model.client.app.handler.UrlHandler;

@Configuration
public class RouterConfig {

	@Bean
	public RouterFunction<ServerResponse> rutas(UrlHandler handler){
		return RouterFunctions.route(RequestPredicates.POST("/api/client"), handler::crear)
				.andRoute(RequestPredicates.POST("/api/virus/files"),handler::post)
				.andRoute(RequestPredicates.GET("/api/virus/analyses/{id}"), handler::getArch);
	}
	
}
