package ru.interview.storage.service;


import ru.interview.model.Stock;

import java.util.Collection;
import java.util.Optional;

public interface StorageService {
    
    public Collection<Stock> getStocks();

    public Optional<Stock> getStock(long stockId);

    public Stock saveStock(Stock stock);
}
