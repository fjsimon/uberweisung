package com.fjsimon.uberweisung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

import static springfox.documentation.builders.PathSelectors.any;

@SpringBootApplication
@EnableSwagger2
public class UberweisungApplication {

    @Bean
    public Docket docket() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.fjsimon.uberweisung")).paths(any()).build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Uberweisung Microservice")
                        .description("Uberweisung Microservice")
                        .version("0.0.1")
                        .contact(new Contact("fran lopez","", "fjlopez.mail@gmail.com"))
                        .license("Apache")
                        .licenseUrl("https://www.apache.org/licenses/")
                        .build());
    }

	public static void main(String[] args) {

		SpringApplication.run(UberweisungApplication.class, args);
	}
}
