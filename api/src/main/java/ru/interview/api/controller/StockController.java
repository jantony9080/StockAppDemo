package ru.interview.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.interview.api.service.StockService;
import ru.interview.model.Stock;

import java.util.Collection;
import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/stocks/", method = GET)
    public Collection<Stock> getStocks() {
        return stockService.getStocks();
    }

    @RequestMapping(value = "/stocks/{id}", method = GET)
    public Stock getStock(@PathVariable("id") long stockId) {
        return stockService.getStock(stockId);
    }

    @RequestMapping(value = "/stocks/{id}", method = PUT)
    public void updateStock(@PathVariable("id") long stockId, @RequestBody  Stock stock) {
        stockService.updateStockPrice(stockId,stock.getPrice());
    }
    
    @RequestMapping(value="/stocks/", method = POST)
    public void createStock(@RequestBody Stock newStock) {
        stockService.createStock(newStock);
    }
}