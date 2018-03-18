package ru.interview.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.interview.model.Stock;
import ru.interview.storage.StockRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class RepositoryService implements StorageService {
    @Autowired
    private StockRepository repository;

    

    @Override
    public Collection<Stock> getStocks() {
        List<Stock> target = new ArrayList<>();
        repository.findAll().forEach(target::add);
        return target;
    }

    @Override
    public Optional<Stock> getStock(long stockId) {
        return repository.findById(stockId);
    }

    @Override
    public Stock saveStock(Stock stock) {
        return repository.save(stock);
    }
}
