package com.batchProcess.BatchProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class ClientDto
{
    private int id;
    private String firstname;
    private String lastName;
    private String annee;
}
