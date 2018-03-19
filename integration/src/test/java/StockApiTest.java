import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.interview.api.Application;
import ru.interview.model.Stock;
import ru.interview.storage.service.StorageService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = Application.class)
@ActiveProfiles("it-test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StockApiTest {

    private final Stock GOOGLE = new Stock("google", BigDecimal.valueOf(55.55));
    private final Stock AMAZON = new Stock("amazon", BigDecimal.valueOf(655.67));


    @Autowired
    StorageService storage;
    @Autowired
    private MockMvc mvc;
    ObjectMapper mapper = new ObjectMapper();

   

    @Test
    public void givenStocks_whenGetStocks_thenStatus200()
            throws Exception {
        storage.saveStock(GOOGLE);
        storage.saveStock(AMAZON);

        mvc.perform(get("/api/stocks/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    public void emptyStorage_whenGetStocks_thenStatus209()
            throws Exception {

        mvc.perform(get("/api/stocks/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    public void dontExistStock_whenGetStocksById_thenStatus500() throws Exception {

        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("http://localhost:8080/api/stocks/1000", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void existStock_whenGetStocksById_thenStatus200() throws Exception {
        storage.saveStock(GOOGLE);
        mvc.perform(get("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("google")))
                .andExpect(jsonPath("$.price", is(55.55)))
                .andExpect(jsonPath("$.lastUpdate").isEmpty());
    }


    @Test
    public void updateStock_whenPutStock_thenStatus200() throws Exception {
        storage.saveStock(GOOGLE);
        storage.saveStock(AMAZON);

        MvcResult result =mvc.perform(get("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        .andReturn();

        Stock google=mapper.readValue(result.getResponse().getContentAsString(), mapper.getTypeFactory().constructType(Stock.class));
        google.setPrice(BigDecimal.TEN);
        
        mvc.perform(put("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(google)))
                .andExpect(status().isOk());

        mvc.perform(get("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("google")))
                .andExpect(jsonPath("$.price", is(10.0)))
                .andExpect(jsonPath("$.lastUpdate").isNotEmpty());
    }
}
