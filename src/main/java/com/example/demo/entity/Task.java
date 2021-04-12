package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "task")
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String  name;
    private String description;
    private Date deadline;
    private boolean isActive;
    @CreatedBy
    private UUID createBy;//KIM QO'SHGANLIGI
    @LastModifiedBy
    private UUID updateBy;//KIM TAHRIRLAGANLIGI
    @UpdateTimestamp
    private Timestamp updateAt;
    @CreationTimestamp
    private Timestamp createdAt;
}
