package ru.interview.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "stocks API", description = "Stocks simple API.")
public class StockController {

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "List all stocks available", response = Collection.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved stocks list"),
            @ApiResponse(code = 204, message = "No stocks available")
    }
    )
    @RequestMapping(value = "/stocks/", method = GET)
    public Collection<Stock> getStocks(HttpServletResponse response) {
        Collection stocks = stockService.getStocks();
        response.setStatus(stocks.isEmpty() ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_OK);
        return stocks;
    }

    @ApiOperation(value = "Get the stock with particular ID", response = Stock.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved stock"),
            @ApiResponse(code = 500, message = "Stock with the specified id isn't found")
    }
    )
    @RequestMapping(value = "/stocks/{id}", method = GET)
    public Stock getStock(@PathVariable("id") long stockId, HttpServletResponse response) {
        Stock found = stockService.getStock(stockId);
        return found;
    }

    @ApiOperation(value = "Update price for the stock with particular ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Stock price successfully updated"),
            @ApiResponse(code = 500, message = "Stock with the specified ID is not found")
    }
    )
    @RequestMapping(value = "/stocks/{id}", method = PUT)
    public void updateStock(@PathVariable("id") long stockId, @RequestBody Stock stock) {
        stockService.updateStockPrice(stockId, stock.getPrice());
    }

    @ApiOperation(value = "Add new stock to the system")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created stock"),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @RequestMapping(value = "/stocks/", method = POST)
    public void createStock(@RequestBody Stock newStock, HttpServletResponse response) {
        stockService.createStock(newStock);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}