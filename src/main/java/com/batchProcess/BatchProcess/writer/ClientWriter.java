package com.batchProcess.BatchProcess.writer;

import com.batchProcess.BatchProcess.entity.Client;
import com.batchProcess.BatchProcess.repository.ClientRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientWriter implements ItemWriter<Client> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void write(Chunk<? extends Client> lisfOfClient) throws Exception
    {
        clientRepository.saveAll(lisfOfClient);
    }
}
