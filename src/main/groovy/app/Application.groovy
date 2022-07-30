package app

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import javax.servlet.Filter
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

@SpringBootApplication
@ServletComponentScan
@EnableMongoRepositories(basePackages = ["Repositorios"])
@ComponentScan(basePackages = ["Controladores", "Servicios"])
@Configuration
@EnableSwagger2
class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args)
	}

	@Component
	public class ConfigCtrl implements Filter {

		@Override
		public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
			final HttpServletResponse response = (HttpServletResponse) res;
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE, PATCH");
			response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
			response.setHeader("Access-Control-Max-Age", "3600");
			if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				chain.doFilter(req, res);
			}
		}
		@Override
		public void destroy() {
		}
		@Override
		public void init(FilterConfig config) throws ServletException {
		}
	}

	@Bean
	MongoClient mongoClient() {
		return MongoClients.create("mongodb://localhost:27017")
	}

	@Bean
	MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), "EasyScheduler")
	}

	@Bean
	Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
	}
}


