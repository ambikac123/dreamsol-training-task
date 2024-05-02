package com.dreamsol;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;


@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "DreamSol Tele Solutions Pvt. Ltd.",
				version = "v3",
				description = "This is documentation for RESTFUL APIs Created By: Ambika Chaudhary"
				),
		servers = @Server(
				url = "http://localhost:8080",
				description = "resource-name"
				)
)

public class DepartmentUsertypeApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(DepartmentUsertypeApplication.class, args);
	}
	@SecurityScheme(
			name = "bearerAuth",
			description = "JWT Authentication",
			scheme = "bearer",
			type = SecuritySchemeType.HTTP,
			bearerFormat = "JWT",
			in = SecuritySchemeIn.HEADER
	)
	public static class SwaggerConfig{
		@Bean
		public Docket api(){
			return new Docket(DocumentationType.SWAGGER_2)
					.select()
					.apis(RequestHandlerSelectors.any())
					.paths(PathSelectors.any())
					.build();
		}
	}
}
