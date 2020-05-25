package com.stocks.web.rest.vm;

import com.stocks.service.dto.UserDTO;

import javax.validation.constraints.NotNull;

public class ManagedUserVM extends UserDTO {

    @NotNull
    private String password;

    @NotNull
    private String checkPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }
}
