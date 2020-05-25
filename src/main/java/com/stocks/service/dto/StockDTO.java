package com.stocks.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("Global Quote")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDTO {

    @JsonProperty("05. price")
    private double price;

    @JsonProperty("09. change")
    private double change;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
