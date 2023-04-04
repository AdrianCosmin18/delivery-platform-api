package com.example.deliveryapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
public class DeliveryAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryAppApplication.class, args);
	}


	@Configuration
	public static class ModelMapperBean{
		@Bean
		public ModelMapper modelMapper(){
			return new ModelMapper();
		}
	}

//	@Configuration
//	public static class ObjectMapperBean{
//		@Bean
//		public ObjectMapper objectMapper(){return new ObjectMapper();}
//	}

	@Configuration
	public class JacksonConfig {
		@Bean
		public JavaTimeModule javaTimeModule() {
			return new JavaTimeModule();
		}
		@Bean
		public ObjectMapper objectMapper() {
			return Jackson2ObjectMapperBuilder.json()
					.modules(javaTimeModule())
					.build();
		}
	}

}
