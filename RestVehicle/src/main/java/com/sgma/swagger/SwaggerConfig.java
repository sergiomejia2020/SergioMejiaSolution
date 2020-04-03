package com.sgma.swagger;

import springfox.documentation.service.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("RestVehicle-API")
				.apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.sgma.controller"))
	            .paths(PathSelectors.regex("/.*")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("RestVehicle API")
				.description("Sergio Mejia Code Assesment solution")
				.termsOfServiceUrl("http://sergiomejia.com")
				.contact(new Contact("Sergio Mejia","","sm.meija.javadev@gmail.com"))
				.license("Code Assesment Java License")
				.licenseUrl("sm.meija.javadev@gmail.com").version("1.0").build();
	}

}
