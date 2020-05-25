package com.stocks.web.rest;

import com.stocks.service.UserService;
import com.stocks.service.dto.UserDTO;
import com.stocks.web.rest.errors.BadRequestException;
import com.stocks.web.rest.vm.ManagedUserVM;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody @Valid ManagedUserVM managedUserVM) {

        if (!managedUserVM.getPassword().equals(managedUserVM.getCheckPassword())) {
            throw new BadRequestException("Passwords don't match");
        }

        return userService.registerUser(managedUserVM, managedUserVM.getPassword());
    }
}
