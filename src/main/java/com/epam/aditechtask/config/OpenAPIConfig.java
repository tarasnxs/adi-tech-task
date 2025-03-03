package com.epam.aditechtask.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Product & Category Management API")
				.version("1.0")
				.description("API for managing products and categories."))
			.addSecurityItem(new SecurityRequirement().addList("BearerAuth")) // Apply security globally
			.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes("BearerAuth", new SecurityScheme()
					.name("BearerAuth")
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")));
	}
}
