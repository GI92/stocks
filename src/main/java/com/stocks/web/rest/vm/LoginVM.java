package com.stocks.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginVM {

    @NotNull
    @Size(min = 10, max = 50)
    private String username;

    @NotNull
    @Size(min = 4, max = 50)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
