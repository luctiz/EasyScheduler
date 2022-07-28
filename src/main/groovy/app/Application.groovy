package app

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
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
		return MongoClients.create("mongodb://localhost:27017")
	}

	@Bean MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), "EasyScheduler")
	}

	@Bean Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000")
			}
		}
	}

}
//@Configuration
//@EnableWebSecurity
//class WebSecurity implements SecurityFilterChain {
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().csrf().disable()
//				.authorizeRequests()
//	}
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		final CorsConfiguration config = new CorsConfiguration();
//		config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//		config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
//		config.setAllowCredentials(true);
//		config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", config);
//		return source;
//	}
//
//	@Override
//	boolean matches(HttpServletRequest request) {
//		return false
//	}
//
//	@Override
//	List<Filter> getFilters() {
//		return null
//	}
//}
