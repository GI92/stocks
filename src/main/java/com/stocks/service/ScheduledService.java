package com.stocks.service;

import com.stocks.domain.Alert;
import com.stocks.repository.AlertRepository;
import com.stocks.service.dto.StockDTO;
import com.stocks.utils.Direction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
public class ScheduledService {

    private final AlphaRestService restService;

    private final AlertRepository alertRepository;

    private JavaMailSender mailSender;

    @Value("${alphavantage.key:beta}")
    private String secret;

    public ScheduledService(AlphaRestService restService,
                            AlertRepository alertRepository,
                            JavaMailSender mailSender) {
        this.restService = restService;
        this.alertRepository = alertRepository;
        this.mailSender = mailSender;
    }

    @Scheduled(fixedRateString = "${scheduling.polling.scheduleMilliseconds:1000}")
    public void scheduleAlert() {
        List<String> symbols = alertRepository.getAllStocks();
        if (symbols == null) {
            return;
        }

        symbols.parallelStream().forEach(this::processAlert);
    }

    private void processAlert(final String symbol) {
        final StockDTO stock = restService.getGlobalQuote(symbol);
        if (stock == null) {
            return;
        }

        List<Alert> alerts;
        final double price = stock.getPrice();
        if (stock.getChange() > 0) {
            alerts = alertRepository
                    .findByActiveTrueAndStockAndDirectionAndExpectedPriceBefore(symbol,
                            Direction.UP,
                            price);
        } else {
            alerts = alertRepository
                    .findByActiveTrueAndStockAndDirectionAndExpectedPriceAfter(symbol,
                            Direction.DOWN,
                            price);
        }
        alerts.parallelStream().forEach(alert -> sendEmail(alert, price));
    }

    private void sendEmail(final Alert alert, final Double price) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(alert.getUser().getEmail());

        message.setSubject("Stock alert for " + alert.getStock());
        message.setText(
                "Price changed for " + alert.getStock()
                        + " from " + alert.getPrice()
                        + " to " + price);

        mailSender.send(message);
        alert.setActive(false);
        alertRepository.save(alert);
    }
}
