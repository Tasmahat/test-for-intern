package com.example.testforintern.BDs.Controllers;

import com.example.testforintern.BDs.Entities.Clients;
import com.example.testforintern.BDs.Entities.LegalForm;
import com.example.testforintern.BDs.Services.ClientsService;
import com.example.testforintern.BDs.Services.LegalFormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


@RestController
@RequestMapping("/clients")
public class ClientsController {

    private final ClientsService clientsService;
    private final LegalFormService legalFormService;

    @Autowired
    public ClientsController(ClientsService clientsService, LegalFormService legalFormService) {
        this.clientsService = clientsService;
        this.legalFormService = legalFormService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createClient(
            @RequestParam String name,
            @RequestParam String shortName,
            @RequestParam String address,
            @RequestParam String legalForm
    ) {
        LegalForm legalFormObject = legalFormService.getLegalFormByName(legalForm);
        if (legalFormObject == null) {
            return new ResponseEntity<>(
                    "Введите организационно-правовую форму из списка",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (clientsService.getClientByName(name) != null) {
            return new ResponseEntity<>(
                    "Клиент с таким именем уже существует",
                    HttpStatus.BAD_REQUEST
            );
        }

        Clients client = new Clients();
        client.setName(name);
        client.setShortName(shortName);
        client.setAddress(address);
        client.setLegalForm(legalFormObject);

        return new ResponseEntity<>(clientsService.createClient(client), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<Clients> getAllClients() {
        return clientsService.getAllClients();
    }

    @GetMapping("/sort")
    public List<Clients> getSortedClients(
            @RequestParam (name = "params") List<String> listOfSortParams
    ) {
        try {
            return clientsService.sortClients(listOfSortParams);
        } catch (RuntimeException e) {
            System.out.println("Возникла ошибка: " + e);
            return List.of();
        }
    }

    @GetMapping("/filter")
    public Page<Clients> getFilteredClients(@RequestParam String filterKey, @RequestParam String filterValue) {
        switch (filterKey.toLowerCase(Locale.ENGLISH)) {
            case "name" -> {
                return clientsService.filterClientsByName(filterValue);
            }
            case "shortname" -> {
                return clientsService.filterClientsByShortName(filterValue);
            }
            case "address" -> {
                return clientsService.filterClientsByAddress(filterValue);
            }
            case "legalform" -> {
                LegalForm legalForm = legalFormService.getLegalFormByName(filterValue);
                return clientsService.filterClientsByLegalForm(legalForm);
            }
            default -> {
                return Page.empty();
            }
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateClient(
            @RequestParam String name,
            @RequestParam Optional<String> shortName,
            @RequestParam Optional<String> address,
            @RequestParam Optional<String> legalForm
    ) {
        Clients client = clientsService.getClientByName(name);
        if (client == null) {
            return new ResponseEntity<>("Клиент еще не существует", HttpStatus.BAD_REQUEST);
        }

        shortName.ifPresent(client::setShortName);
        address.ifPresent(client::setAddress);
        legalForm.map(legalFormService::getLegalFormByName).ifPresent(client::setLegalForm);

        return new ResponseEntity<>(clientsService.updateClient(client), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteClient(@RequestParam String name) {
        Clients client = clientsService.getClientByName(name);
        if (client == null) {
            return new ResponseEntity<>("Не существует клиента на удаление", HttpStatus.BAD_REQUEST);
        }

        clientsService.deleteClient(client);

        return new ResponseEntity<>("Клиент был удален", HttpStatus.OK);
    }
}
