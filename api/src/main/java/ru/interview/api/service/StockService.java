package ru.interview.api.service;

import org.springframework.stereotype.Service;
import ru.interview.model.Stock;

import java.math.BigDecimal;
import java.util.Collection;

@Service
public interface StockService {

    public Collection<Stock> getStocks();

    public Stock getStock(long stockId);

    public Stock updateStockPrice(long stockId, BigDecimal newPrice);

    public Stock createStock(Stock newStock);
}
