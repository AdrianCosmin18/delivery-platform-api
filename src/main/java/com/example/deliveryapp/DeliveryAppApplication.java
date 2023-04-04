package com.example.deliveryapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

	@Configuration
	public static class ObjectMapperBean{
		@Bean
		public ObjectMapper objectMapper(){return new ObjectMapper();}
	}
}
