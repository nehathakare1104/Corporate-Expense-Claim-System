package com.example.ExpenseClaim.Controller;

import com.example.ExpenseClaim.Dto.LoginRequestDto;
import com.example.ExpenseClaim.Dto.LoginResponseDto;
import com.example.ExpenseClaim.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto  login(@Valid @RequestBody LoginRequestDto loginRequest){
        return authService.login(loginRequest);
    }
}
