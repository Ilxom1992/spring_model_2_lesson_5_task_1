package com.example.demo.repository;

import com.example.demo.entity.Turniket;
import com.example.demo.entity.TurniketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TurniketRepository extends JpaRepository<Turniket,Integer> {

    @Query(value = "select tur from Turniket tur where tur.created_by = ?1 and (tur.enter_date_time >= ?2 or tur.enter_date_time <= ?3)",nativeQuery = true)
    List<TurniketHistory> getAllByCreatedBy(UUID employeeId, LocalDateTime start, LocalDateTime finish);

    @Query(value = "select  * from turniket join turniket_history th on turniket.id = th.turniket_id where" +
            " th.user_id=?1 and turniket.status=true and turniket_id=?2",nativeQuery = true)
    public Optional<Turniket> getTurniketHistoryByUserIdAndTurniketIdAndStatus(UUID userId,Integer turniketId);


}
