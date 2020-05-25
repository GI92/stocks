package com.stocks.web.rest.errors;

public class EmailAlreadyExists extends BadRequestException {

    public EmailAlreadyExists() {
        super("Email already exists!");
    }
}
