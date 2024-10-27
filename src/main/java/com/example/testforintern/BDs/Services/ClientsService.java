package com.example.testforintern.BDs.Services;

import com.example.testforintern.BDs.Entities.Clients;
import com.example.testforintern.BDs.Entities.LegalForm;
import com.example.testforintern.BDs.Repositories.ClientsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;
    private final Pageable pageableUnPaged = Pageable.unpaged();

    @Autowired
    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public Clients createClient(Clients client) {
        return clientsRepository.save(client);
    }

    public Page<Clients> getAllClients() {
        return clientsRepository.findAll(pageableUnPaged);
    }

    public Clients getClientByName(String name) {
        return clientsRepository.getByName(name);
    }

    public List<Clients> sortClients(List<String> listOfSortingParams) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String i : listOfSortingParams) {
            orders.add(new Sort.Order(Sort.Direction.ASC, i));
        }

        return clientsRepository.findAll(Sort.by(orders));
    }

    public Page<Clients> filterClientsByName(String subString) {
        return clientsRepository.findByNameContainingIgnoreCase(subString, pageableUnPaged);
    }

    public Page<Clients> filterClientsByShortName(String subString) {
        return clientsRepository.findByShortNameContainingIgnoreCase(subString, pageableUnPaged);
    }

    public Page<Clients> filterClientsByAddress(String subString) {
        return clientsRepository.findByAddressContainingIgnoreCase(subString, pageableUnPaged);
    }

    public Page<Clients> filterClientsByLegalForm(LegalForm legalForm) {
        return clientsRepository.findByLegalForm(legalForm, pageableUnPaged);
    }

    public Clients updateClient(Clients client) {
        return clientsRepository.save(client);
    }

    public void deleteClient(Clients client) {
        clientsRepository.delete(client);
    }
}
