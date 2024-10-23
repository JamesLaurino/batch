package com.batchProcess.BatchProcess.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class Client
{
    @Id
    private int id;
    private String firstname;
    private String lastname;
    private int age;
    private String status;
}