package com.stocks.service;

import com.stocks.domain.Alert;
import com.stocks.domain.User;
import com.stocks.repository.AlertRepository;
import com.stocks.repository.UserRepository;
import com.stocks.security.SecurityUtils;
import com.stocks.service.dto.AlertDTO;
import com.stocks.service.dto.StockDTO;
import com.stocks.service.mapper.AlertMapper;
import com.stocks.utils.Direction;
import com.stocks.web.rest.errors.BadRequestException;
import com.stocks.web.rest.errors.UnauthorizedAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private AlertRepository alertRepository;
    private AlertMapper alertMapper;
    private AlphaRestService alphaRestService;
    private UserRepository userRepository;

    public AlertService(AlertRepository alertRepository, AlertMapper alertMapper,
                        AlphaRestService alphaRestService, UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
        this.alphaRestService = alphaRestService;
        this.userRepository = userRepository;
    }

    public List<AlertDTO> retrieveMyAlerts() {
        final String email = getCurrentUserEmail();

        final List<AlertDTO> myAlerts = alertRepository.findAllByUserEmail(email)
                .stream()
                .map(alertMapper::toDTO)
                .collect(Collectors.toList());
        myAlerts.forEach(this::addCurrentPriceAndPercentageFor);

        return myAlerts;
    }

    private void addCurrentPriceAndPercentageFor(final AlertDTO alert) {
        final StockDTO stockDTO = alphaRestService.getGlobalQuote(alert.getStock());

        if (stockDTO == null) {
            return;
        }

        final double currentPrice = stockDTO.getPrice();
        final int currentDifference = percent(alert.getPrice(), currentPrice);

        alert.setCurrentPrice(currentPrice);
        alert.setCurrentDifference(currentDifference);
    }

    private int percent(double oldPrice, double newPrice) {
        double result = 0;
        result = ((newPrice - oldPrice) * 100) / oldPrice;

        return (int) result;
    }

    public AlertDTO create(AlertDTO alertDTO) {
        final String email = getCurrentUserEmail();
        final User user = userRepository.findByEmail(email).orElseThrow(UnauthorizedAccessException::new);

        alertRepository.findByStockAndUserEmail(alertDTO.getStock(), email)
                .ifPresent(alert -> {
                    throw new BadRequestException("Alert with " + alert.getStock() + " already exists!");
                });

        setPriceAndPercentageFor(alertDTO);

        final Alert alert = alertMapper.fromDTO(alertDTO);
        alert.setUser(user);

        updateExpectedPrice(alert);

        alertRepository.save(alert);

        alertDTO.setId(alert.getId());
        alertDTO.setActive(true);

        return alertDTO;
    }

    public AlertDTO updateAlert(AlertDTO alertDTO) {
        final String email = getCurrentUserEmail();
        final Long id = alertDTO.getId();
        final Alert alert = alertRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(UnauthorizedAccessException::new);

        if (!alert.getStock().equals(alertDTO.getStock())) {
            alertRepository.findByStockAndUserEmailAndIdNot(alertDTO.getStock(), email, id)
                    .ifPresent(a -> {
                        throw new BadRequestException("Alert with " + a.getStock() + " already exists!");
                    });
            alert.setStock(alertDTO.getStock());
        }

        setPriceAndPercentageFor(alertDTO);
        alert.setActive(true);
        alert.setPercentage(alertDTO.getPercentage());
        alert.setStock(alertDTO.getStock());

        updateExpectedPrice(alert);

        alertRepository.save(alert);

        AlertDTO result = alertMapper.toDTO(alert);
        result.setCurrentPrice(alertDTO.getCurrentPrice());
        result.setCurrentDifference(alertDTO.getCurrentDifference());

        return result;
    }

    public void delete(Long id) {
        final String email = getCurrentUserEmail();
        final Alert alert = alertRepository.findByIdAndUserEmail(id, email).orElseThrow(
                UnauthorizedAccessException::new);

        alertRepository.delete(alert);
    }

    private String getCurrentUserEmail() {
        return SecurityUtils.getCurrentUserEmail().orElseThrow(UnauthorizedAccessException::new);
    }

    private Alert updateExpectedPrice(final Alert alert) {
        int percentage = alert.getPercentage();
        alert.setDirection(percentage > 0 ? Direction.UP : Direction.DOWN);


        double price = alert.getPrice();
        double expectedPrice = price + (price * percentage) / 100;
        alert.setExpectedPrice(expectedPrice);

        return alert;
    }

    private void setPriceAndPercentageFor(final AlertDTO alert) {
        final StockDTO stockDTO = alphaRestService.getGlobalQuote(alert.getStock());

        if (stockDTO == null) {
            return;
        }

        final double price = stockDTO.getPrice();
        alert.setPrice(price);
        alert.setCurrentPrice(price);
        alert.setCurrentDifference(0);
    }
}
