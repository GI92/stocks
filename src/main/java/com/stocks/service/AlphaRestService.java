package com.stocks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocks.service.dto.StockDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlphaRestService {

    private static final Logger LOG = LoggerFactory.getLogger(AlphaRestService.class);

    private final RestTemplate restTemplate;

    @Value("${alphavantage.key:beta}")
    private String key;

    public AlphaRestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public StockDTO getGlobalQuote(String symbol) {
        final String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + key;

        final String responseJson = this.restTemplate.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
            if (responseJson != null) {
                return mapper.readValue(responseJson, StockDTO.class);
            }
        } catch (JsonProcessingException e) {
            LOG.error("Could not get stock details for symbol {}", symbol, e);
        }

        return null;
    }
}
