package com.study.mandarin.lang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LangApplication {

	public static void main(String[] args) {
		SpringApplication.run(LangApplication.class, args);
	}

//	@Bean
//	ApplicationRunner debugMongo(Environment env) {
//		return args -> {
//			System.out.println("=== MONGO DEBUG ===");
//			System.out.println("URI: " + env.getProperty("spring.data.mongodb.uri"));
//			System.out.println("HOST: " + env.getProperty("spring.data.mongodb.host"));
//			System.out.println("PORT: " + env.getProperty("spring.data.mongodb.port"));
//		};
//	}
}