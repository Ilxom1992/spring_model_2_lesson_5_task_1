package com.example.demo.payload;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TaskDto {
    private String title;
    private String body;
    private Date deadLine;//topshirish muddati; tugatish muddati
    private UUID responsibleId;//javobgar id si
    private String taskCode=UUID.randomUUID().toString().substring(8);
}
