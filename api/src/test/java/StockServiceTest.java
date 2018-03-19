import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.interview.api.service.StockServiceImpl;
import ru.interview.model.Stock;
import ru.interview.storage.service.StorageService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
public class StockServiceTest {

    public static final Stock TO_INSERT = new Stock("testStock", BigDecimal.valueOf(5.2));
    @MockBean
    public StorageService storage;
    @Autowired
    private StockServiceImpl service;

    @Before
    public void mockRepo() {
        Mockito.when(storage.saveStock(Mockito.any(Stock.class))).thenAnswer(i -> {
            Stock stock = (Stock) i.getArguments()[0];
            Stock copyStock = copyAndUpdateId(stock, 1L);
            return copyStock;
        });


        Mockito.when(storage.getStock(Mockito.anyLong())).thenAnswer(i -> {
            long stockId = (long) i.getArguments()[0];
            return stockId == 1L ? Optional.of(copyAndUpdateId(TO_INSERT, 1L)) : Optional.empty();
        });

        Mockito.when(storage.getStocks()).thenAnswer(i -> {
            Collection<Invocation> invocations = Mockito.mockingDetails(storage).getInvocations();
            invocations.removeIf(invocation -> !invocation.getMethod().getName().equals("saveStock"));
            List<Stock> stocks = Stream.generate(Stock::new)
                    .limit(invocations.size())
                    .collect(Collectors.toList());
            return stocks;
        });
    }

    private Stock copyAndUpdateId(Stock stock, long newId) {
        Stock copyStock = new Stock(stock.getName(), stock.getPrice());
        copyStock.setLastUpdate(stock.getLastUpdate());
        if (copyStock.getId() == 0) {
            ReflectionTestUtils.setField(copyStock, "id", newId);
        }
        return copyStock;
    }

    @Test
    public void testInsert() {
        Stock inserted = service.createStock(TO_INSERT);
        Assert.assertEquals(1, inserted.getId());
        Assert.assertEquals(TO_INSERT.getName(), inserted.getName());
        Assert.assertEquals(TO_INSERT.getPrice(), inserted.getPrice());
    }

    @Test(expected = RuntimeException.class)
    public void testInsertDuplicate() {
        Stock inserted = service.createStock(TO_INSERT);
        service.createStock(inserted);
    }

    @Test
    public void updatePriceTest() {
        Stock inserted = service.createStock(TO_INSERT);

        final BigDecimal expected = BigDecimal.valueOf(1.1);
        Stock updated = service.updateStockPrice(inserted.getId(), expected);

        Assert.assertEquals(inserted.getId(), updated.getId());
        Assert.assertEquals(inserted.getName(), updated.getName());
        Assert.assertNotNull(updated.getLastUpdate());
        Assert.assertEquals(expected, updated.getPrice());

    }

    @Test(expected = NoSuchElementException.class)
    public void updatePriceNotExistTest() {
        Stock inserted = service.createStock(TO_INSERT);

        final BigDecimal expected = BigDecimal.valueOf(1.1);
        Stock updated = service.updateStockPrice(Long.MIN_VALUE, expected);
    }

    @Test(expected = NoSuchElementException.class)
    public void getStockNotExistTest() {
        Stock inserted = service.createStock(TO_INSERT);

        Stock stock = service.getStock(Long.MIN_VALUE);
    }

    public void getStockTest() {
        Stock inserted = service.createStock(TO_INSERT);

        Stock stock = service.getStock(inserted.getId());
        Assert.assertNotNull(stock);
        Assert.assertEquals(TO_INSERT.getName(), stock.getName());
        Assert.assertEquals(TO_INSERT.getPrice(), stock.getPrice());
    }

    @Test
    public void getStocksListTest() {
        Collection<Stock> stocks = service.getStocks();
        Assert.assertTrue(stocks.isEmpty());
        Stock firstStock = new Stock("testStock", BigDecimal.valueOf(5.2));
        service.createStock(firstStock);
        stocks = service.getStocks();

        Assert.assertEquals(1, stocks.size());

        Stock secondStock = new Stock("testStock1", BigDecimal.valueOf(4.2));
        service.createStock(secondStock);
        stocks = service.getStocks();
        Assert.assertEquals(2, stocks.size());
    }

    @TestConfiguration
    static class ServiceImplTestContextConfiguration {

        @Bean
        public StockServiceImpl stockServise() {
            return new StockServiceImpl();
        }

    }
}
