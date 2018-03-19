package ru.interview.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.interview.storage.service.RepositoryService;
import ru.interview.storage.service.StorageService;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableJpaRepositories("ru.interview.storage")
@EntityScan("ru.interview.model")
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public StorageService createStorageService(){
        return new RepositoryService();
    }


    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("stock")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/api.*"))
                .build();

    }

    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Demo stock app")
                .description("The API allows you to create stocks and update the price.")
                .termsOfServiceUrl("www.example.com/terms-of-service")
                .contact("Andrey Erokhin")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/LICENSE")
                .version("2.0")
                .build();
    }
}
