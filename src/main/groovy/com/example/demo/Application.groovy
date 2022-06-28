package com.example.demo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["Repositorios"])
@ComponentScan(basePackages = ["Controladores", "Servicios"])
@Configuration
@EnableSwagger2
class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args)
	}

	@Bean MongoClient mongoClient() {
		return MongoClients.create("mongodb://localhost:27017");
	}

	@Bean MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), "EasyScheduler");
	}

	@Bean Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}

}
