package com.dreamsol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;


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
	public static void main(String[] args) {
		SpringApplication.run(DepartmentUsertypeApplication.class, args);
	}
}
