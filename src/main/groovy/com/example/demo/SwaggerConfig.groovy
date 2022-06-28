//package com.example.demo
//
//import Controladores.EquipoController
//import Controladores.UsuarioController
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.PropertySource
//import springfox.documentation.builders.ApiInfoBuilder
//import springfox.documentation.service.ApiInfo
//import springfox.documentation.spring.web.plugins.Docket
//import springfox.documentation.swagger2.annotations.EnableSwagger2
//
//@EnableSwagger2
////@PropertySource("classpath:swagger.properties")
//@ComponentScan(basePackages = "Controladores")
//@Configuration
//class SwaggerConfig {
//    private static final String SWAGGER_API_VERSION = "1.0";
//    private static final String LICENSE_TEXT = "License";
//    private static final String title = "Spacestudy Framework REST API";
//    private static final String description = "docs for test Framework";
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title(title)
//                .description(description)
//                .license(LICENSE_TEXT)
//                .version(SWAGGER_API_VERSION)
//                .build();
//    }
//
//    @Bean
//    Docket frameworkApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .pathMapping("/")
//                .select()
//                .paths(PathSelectors.regex("/api.*"))
//                .build();
//    }
//}
