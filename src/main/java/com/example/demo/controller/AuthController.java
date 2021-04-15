package com.example.demo.controller;

import com.example.demo.payload.LoginDto;
import com.example.demo.payload.RegisterDto;
import com.example.demo.payload.Response;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDto registerDto, HttpServletRequest httpServletRequest) {
        final Response response = authService.userRegister(registerDto,httpServletRequest);
        return ResponseEntity.status(response.isStatus() ? 201 : 409).body(response);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto) {
        final Response response = authService.login(loginDto);
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }
    @PostMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String email, @RequestParam String emailCode,@RequestParam String newPassword) {
        Response response = authService.verifyEmail(email,emailCode,newPassword);
        return ResponseEntity.ok(response);
    }
}
