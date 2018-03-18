import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.interview.model.Stock;
import ru.interview.storage.StockRepository;
import ru.interview.storage.service.RepositoryService;

import java.math.BigDecimal;

/*
I add this test just to give you a clue, how I would be trying to test it in real app. 
Those test don't make much sense since the sewrvice itself doesn't contain any logic.
It's just a proxy to the Spring repository. That are not supposed to be tested.
 */
@RunWith(SpringRunner.class)
public class RepositoryServiceTest {

    @MockBean
    StockRepository stockRepository;
    @Autowired
    RepositoryService repoService;

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public RepositoryService repoServise() {
            return new RepositoryService();
        }
    }
    
    @Before
    public void mockRepo() {
        Mockito.when(stockRepository.save(Mockito.any(Stock.class))).thenAnswer(i -> {
            Stock stock = (Stock) i.getArguments()[0];
            Stock copyStock = copyAndUpdateId(stock,1L);
            return copyStock;
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
    public void insertionTest() {
        Stock testStock = new Stock("testStock1", BigDecimal.TEN);
        
        Stock savedStock = repoService.saveStock(testStock);
        Assert.assertNotEquals(0, savedStock.getId());
        Assert.assertEquals(testStock.getLastUpdate(), savedStock.getLastUpdate());
    }

    @Test
    public void updateTest() throws InterruptedException {
        Stock testStock = new Stock("testStock1", BigDecimal.TEN);
        Stock savedStock = repoService.saveStock(testStock);

        savedStock.setPrice(BigDecimal.ONE);
        Stock updatedStock = repoService.saveStock(savedStock);
        Assert.assertEquals(BigDecimal.ONE, updatedStock.getPrice());
    }
    
}
