package com.stocks.service.dto;

import javax.validation.constraints.NotNull;

public class AlertDTO {

    private Long id;

    @NotNull
    private String stock;

    private Double price;

    @NotNull
    private Integer percentage;

    private Double currentPrice;

    private Integer currentDifference;

    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getCurrentDifference() {
        return currentDifference;
    }

    public void setCurrentDifference(Integer currentDifference) {
        this.currentDifference = currentDifference;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
