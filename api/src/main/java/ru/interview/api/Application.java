package ru.interview.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.interview.storage.service.RepositoryService;
import ru.interview.storage.service.StorageService;

@SpringBootApplication
@EnableJpaRepositories("ru.interview.storage")
@EntityScan("ru.interview.model")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public StorageService createStorageService(){
        return new RepositoryService();
    }

}
