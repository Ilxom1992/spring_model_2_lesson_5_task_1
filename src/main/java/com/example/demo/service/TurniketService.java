package com.example.demo.service;

import com.example.demo.entity.Turniket;
import com.example.demo.entity.User;
import com.example.demo.payload.Response;
import com.example.demo.repository.TurniketRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TurniketService {
    final TurniketRepository turniketRepository;

    public TurniketService(TurniketRepository turniketRepository) {
        this.turniketRepository = turniketRepository;
    }


    public Response enterToWork() {
        Turniket turniket = new Turniket();
        turniket.setStatus(true);
        turniket.setEnterDateTime(LocalDate.now());

        turniketRepository.save(turniket);
        return new Response("Success! You entered!", true);
    }


    public Response exitFromWork() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            User user = (User) authentication.getPrincipal();


            Optional<Turniket> optionalTurniket = turniketRepository.findByCreatedByAndStatus(user.getId(), true);
            if (!optionalTurniket.isPresent())
                return new Response("Such turniket id not found!", false);

            optionalTurniket.get().setStatus(false);
            optionalTurniket.get().setExitDateTime(LocalDate.now());

            turniketRepository.save(optionalTurniket.get());

            return new Response("Success! You exited!", true);
        }
        return new Response("Authentication empty!", false);
    }
}
