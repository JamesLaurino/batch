package com.batchProcess.BatchProcess.processor;

import com.batchProcess.BatchProcess.dto.ClientDto;
import com.batchProcess.BatchProcess.entity.Client;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClientProcessor implements ItemProcessor<ClientDto,Client> {
    @Override
    public Client process(ClientDto clientDto) throws Exception {

        String year = clientDto.getAnnee() + "T00:00:00";

        int clientYear = LocalDateTime.parse(year).getYear();
        int currentYear = LocalDateTime.now().getYear();
        int age = currentYear - clientYear;

        return Client.builder()
                .id(clientDto.getId())
                .firstname(clientDto.getFirstname())
                .lastname(clientDto.getLastName())
                .age(age)
                .build();
    }
}
