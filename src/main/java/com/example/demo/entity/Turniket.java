package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Turniket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean status;                  // true - ishxonaga kirdi false - ishdan chiqdi

    @CreatedBy
    private UUID createdBy;                 // ishga kiruvchi user

    @NotNull
    private LocalDate enterDateTime;   // ishga kirgan vaqti

    private LocalDate exitDateTime;    // ishdan chiqqan vaqti


}
