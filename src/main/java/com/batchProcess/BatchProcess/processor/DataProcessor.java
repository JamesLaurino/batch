package com.batchProcess.BatchProcess.processor;

import com.batchProcess.BatchProcess.dto.ClientDto;
import com.batchProcess.BatchProcess.entity.Client;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class DataProcessor implements ItemProcessor<ClientDto, Client>
{
    public int majeurQty = 0;
    public int mineurQty = 0;

    @Override
    public Client process(ClientDto clientDto) throws Exception
    {
        String year = clientDto.getAnnee() + "T00:00:00";

        int clientYear = LocalDateTime.parse(year).getYear();
        int currentYear = LocalDateTime.now().getYear();
        int age = currentYear - clientYear;

        if(age < 18)
        {
            mineurQty += 1;
            return Client.builder()
                    .id(clientDto.getId())
                    .firstname(clientDto.getFirstname())
                    .lastname(clientDto.getLastName())
                    .age(age)
                    .status("MINEUR")
                    .build();
        }
        else {
            majeurQty += 1;
            return Client.builder()
                    .id(clientDto.getId())
                    .firstname(clientDto.getFirstname())
                    .lastname(clientDto.getLastName())
                    .age(age)
                    .status("MAJEUR")
                    .build();
        }
    }
}
