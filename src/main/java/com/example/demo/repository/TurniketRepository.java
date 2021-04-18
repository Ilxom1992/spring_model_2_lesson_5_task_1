package com.example.demo.repository;

import com.example.demo.entity.Turniket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TurniketRepository extends JpaRepository<Turniket,Integer> {
    Optional<Turniket> findByCreatedByAndStatus(UUID createdBy, boolean status);
    @Query(value = "select tur from Turniket tur where tur.created_by = ?1 and (tur.enter_date_time >= ?2 or tur.enter_date_time <= ?3)",nativeQuery = true)
    List<Turniket> getAllByCreatedBy(UUID employeeId, LocalDateTime start, LocalDateTime finish);
}
