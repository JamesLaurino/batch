package com.batchProcess.BatchProcess.repository;


import com.batchProcess.BatchProcess.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
