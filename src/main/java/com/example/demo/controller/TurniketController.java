package com.example.demo.controller;

import com.example.demo.payload.Response;
import com.example.demo.service.TurniketService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/turniket")
public class TurniketController {
    final TurniketService turniketService;

    public TurniketController(TurniketService turniketService) {
        this.turniketService = turniketService;
    }

    @PostMapping
    public HttpEntity<?> addTurniket(@RequestParam String location, @RequestParam UUID userId) {
        Response response=turniketService.addturnket(location,userId);
        return  ResponseEntity.ok(response);
    }

    @GetMapping
    public HttpEntity<?> turniketUserEnterAndExit(@RequestParam Integer turniketId) {
        Response response = turniketService.addHistory(turniketId);
        return ResponseEntity.status(response.isStatus() ? 200 : 401).body(response);
    }

}
