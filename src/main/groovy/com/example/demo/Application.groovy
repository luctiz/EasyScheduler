package com.example.demo

import Controladores.EquipoController
import Controladores.UsuarioController
import Servicios.EquipoService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["Repositorios"])
@ComponentScan(basePackages = ["Controladores", "Servicios"])
class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args)
	}

}
