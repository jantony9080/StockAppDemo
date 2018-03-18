package ru.interview.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.interview.model.Stock;
import ru.interview.storage.service.StorageService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StorageService storage;


    @Override
    public Collection<Stock> getStocks() {
        return storage.getStocks();
    }

    @Override
    public Stock getStock(long stockId) {
        Optional<Stock> maybeStock = storage.getStock(stockId);
        return maybeStock.orElseThrow(() -> new NoSuchElementException(String.format("No stock with id=%d found.", stockId)));
    }

    @Override
    public Stock updateStockPrice(long stockId, BigDecimal newPrice) {
        Stock stock = storage.getStock(stockId).orElseThrow(()->new NoSuchElementException("Stock not found."));
        stock.setPrice(newPrice);
        stock.setLastUpdate(new Date());
        Stock saved=storage.saveStock(stock);
        return saved;
    }

    @Override
    public Stock createStock(Stock newStock) {
        if (storage.getStock(newStock.getId()).isPresent())
            throw new RuntimeException("Stock is already exists.");
        Stock saved=storage.saveStock(newStock);
        return saved;
    }
}
