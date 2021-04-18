package com.example.demo.repository;

import com.example.demo.entity.Task;
import com.example.demo.entity.enams.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    Optional<Task> findByTaskCode(String taskCode);
    List<Task> findAllByStatusAndResponsibleId(int status, UUID responsible_id);
    List<Task> findAllByStatusAndResponsibleId(TaskStatus status, UUID responsible_id);
}
