package ru.interview.api.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.interview.model.Stock;
import ru.interview.storage.service.StorageService;


@Component
public class DataUploader  implements ApplicationRunner {
    
    @Autowired
    StorageService storageService;
    
    private JacksonReader<Stock> stockReader = new JacksonReader<>(Stock.class,"stocks.json");
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        stockReader.readData().stream()
                .forEach(storageService::saveStock);
    }
}
