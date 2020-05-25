package com.stocks.repository;

import com.stocks.domain.Alert;
import com.stocks.utils.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("select stock from Alert where active = true group by stock")
    List<String> getAllStocks();

    List<Alert> findAllByUserEmail(String email);

    Optional<Alert> findByStockAndUserEmail(String stock, String email);

    Optional<Alert> findByStockAndUserEmailAndIdNot(String stock, String email, Long id);

    Optional<Alert> findByIdAndUserEmail(Long id, String email);

    List<Alert> findByActiveTrueAndStockAndDirectionAndExpectedPriceBefore(String stock,
                                                                           Direction direction,
                                                                           Double expectedPrice);

    List<Alert> findByActiveTrueAndStockAndDirectionAndExpectedPriceAfter(String stock,
                                                                          Direction direction,
                                                                          Double expectedPrice);
}
