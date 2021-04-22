package com.example.demo.repository;

import com.example.demo.entity.Turniket;
import com.example.demo.entity.TurniketHistory;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TurniketHistoryRepository extends JpaRepository<TurniketHistory,Integer> {
@Query(value = "select  * from turniket_history  join turniket t on t.id = turniket_history.turniket_id\n" +
        "where t.status=true and t.id=?1 and turniket_history.exit_date_time is null",nativeQuery = true)
     Optional<TurniketHistory> getBy(Integer turniketId);
@Query(value = "select * from turniket_history tur JOIN turniket t on t.id = tur.turniket_id where t.user_id" +
        " =?1 and (tur.time >= ?2 or tur.time <=?3",nativeQuery = true)
     List<TurniketHistory> getTurniketHistoryByUserId(UUID userId, LocalDateTime enterTime,LocalDateTime exitTime);
}
