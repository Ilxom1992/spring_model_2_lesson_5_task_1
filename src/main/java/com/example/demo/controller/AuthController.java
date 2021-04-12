package com.example.demo.controller;

import com.example.demo.payload.LoginDto;
import com.example.demo.payload.RegisterDto;
import com.example.demo.payload.Response;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDto registerDto) {
        final Response response = authService.userRegister(registerDto);
        return ResponseEntity.status(response.isStatus() ? 201 : 409).body(response);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto) {
        final Response response = authService.login(loginDto);
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }
}
