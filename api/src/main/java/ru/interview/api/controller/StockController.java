package ru.interview.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.interview.api.service.StockService;
import ru.interview.model.Stock;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/stocks/", method = GET)
    public Collection<Stock> getStocks(HttpServletResponse response) {
        Collection stocks = stockService.getStocks();
        response.setStatus(stocks.isEmpty() ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_OK);
        return stocks;
    }

    @RequestMapping(value = "/stocks/{id}", method = GET)
    public Stock getStock(@PathVariable("id") long stockId, HttpServletResponse response) {
        Stock found = stockService.getStock(stockId);
        return found;
    }

    @RequestMapping(value = "/stocks/{id}", method = PUT)
    public void updateStock(@PathVariable("id") long stockId, @RequestBody Stock stock) {
        stockService.updateStockPrice(stockId, stock.getPrice());
    }

    @RequestMapping(value = "/stocks/", method = POST)
    public void createStock(@RequestBody Stock newStock, HttpServletResponse response) {
        stockService.createStock(newStock);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}