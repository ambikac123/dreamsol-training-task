package com.dreamsol;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;


@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Department-UserType",
				version = "",
				description = "This is Department-usertype api docs"
				),
		servers = @Server(
				url = "http://localhost:8080",
				description = "Department-UserType url"
				)
)
public class DepartmentUsertypeApplication {

    @Bean
    public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(DepartmentUsertypeApplication.class, args);
	}
}
