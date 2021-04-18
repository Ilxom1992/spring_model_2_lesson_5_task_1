package com.example.demo.entity;

import com.example.demo.entity.enams.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
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
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    private Date deadLine;          // vazifa tugatilishi kerak bo'lgan vaqt
    @ManyToOne
    private User responsible;       // vazifaga mas'ul

    private String taskCode;        //taskni qabul qiluvchiga yuboriladigan code

    private TaskStatus status;

    @CreatedBy
    private UUID createdBy;          // vazifa qo'shuvchi

    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updateAt;


}
