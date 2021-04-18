package com.example.demo.controller;

import com.example.demo.payload.Response;
import com.example.demo.service.TurniketService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turniket")
public class TurniketController {
    final TurniketService turniketService;

    public TurniketController(TurniketService turniketService) {
        this.turniketService = turniketService;
    }

    @PostMapping
    public HttpEntity<?> enterToWork() {
        Response response = turniketService.enterToWork();
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }

    @PutMapping
    public HttpEntity<?> exitFromWork(){
        Response response = turniketService.exitFromWork();
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }




}
