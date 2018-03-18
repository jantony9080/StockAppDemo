package ru.interview.storage;

import org.springframework.data.repository.CrudRepository;
import ru.interview.model.Stock;

public interface StockRepository extends CrudRepository<Stock, Long> {

}
