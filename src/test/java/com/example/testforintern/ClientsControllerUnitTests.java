package com.example.testforintern;


import com.example.testforintern.BDs.Controllers.ClientsController;
import com.example.testforintern.BDs.Entities.Clients;
import com.example.testforintern.BDs.Entities.LegalForm;
import com.example.testforintern.BDs.Services.ClientsService;
import com.example.testforintern.BDs.Services.LegalFormService;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class ClientsControllerUnitTests {
    ClientsService clientsServiceMock = Mockito.mock(ClientsService.class);
    LegalFormService legalFormServiceMock = Mockito.mock(LegalFormService.class);

    ClientsController clientsController = new ClientsController(clientsServiceMock, legalFormServiceMock);

    @Test
    void forCreateClientShouldReturnStatusCreated() {
        Clients client = new Clients();
        LegalForm legalForm = new LegalForm();

        Mockito.when(legalFormServiceMock.getLegalFormByName("ИП")).thenReturn(legalForm);
        Mockito.when(clientsServiceMock.createClient(client)).thenReturn(client);

        ResponseEntity<Object> responseEntityActual = clientsController.createClient(
                "test",
                "tst",
                "test",
                "ИП"
        );

        assertEquals("Неверный статус ответа" , HttpStatus.CREATED, responseEntityActual.getStatusCode());
    }

    @Test
    void forCreateClientWithNullLegalFormShouldReturnMessageWrongLegalForm() {
        Mockito.when(legalFormServiceMock.getLegalFormByName("null")).thenReturn(null);

        ResponseEntity<Object> responseEntityActual = clientsController.createClient(
                "test",
                "tst",
                "test",
                "null"
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Введите организационно-правовую форму из списка",
                HttpStatus.BAD_REQUEST
        );

        assertEquals("Неверный ответ" , responseEntityExpected, responseEntityActual);
    }

    @Test
    void forCreateClientShouldReturnMessageClientAlreadyExists() {
        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(new Clients());
        Mockito.when(legalFormServiceMock.getLegalFormByName("ИП")).thenReturn(new LegalForm());

        ResponseEntity<Object> responseEntityActual = clientsController.createClient(
                "test",
                "tst",
                "test",
                "ИП"
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Клиент с таким именем уже существует",
                HttpStatus.BAD_REQUEST
        );

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forGetAllClientsShouldReturnPageOfClients() {
        Clients client = new Clients();
        List<Clients> listOfClient = new ArrayList<>();
        listOfClient.add(client);

        Page<Clients> page = new PageImpl<>(listOfClient, Pageable.unpaged(), 1);

        Mockito.when(clientsServiceMock.getAllClients()).thenReturn(page);

        assertEquals("Неверная страница", page, clientsController.getAllClients());
    }

    @Test
    void forSortClientShouldReturnList() {
        List<Clients> clientsList = Mockito.anyList();
        Mockito.when(clientsServiceMock.sortClients(List.of())).thenReturn(clientsList);

        assertEquals("Неверный ответ", clientsList, clientsController.getSortedClients(List.of()));
    }

    @Test
    void forWrongSortParamShouldReturnEmptyList() {
        Mockito.when(clientsServiceMock.sortClients(List.of())).thenThrow(new RuntimeException());

        assertEquals("Неверный ответ", List.of(), clientsController.getSortedClients(List.of()));
    }

    @Test
    void forFilterByNameShouldReturnPageWithClients() {
        Clients client = new Clients();
        List<Clients> listOfClient = new ArrayList<>();
        listOfClient.add(client);

        Page<Clients> page = new PageImpl<>(listOfClient, Pageable.unpaged(), 1);

        Mockito.when(clientsServiceMock.filterClientsByName("name")).thenReturn(page);

        assertEquals(
                "Неверный ответ",
                page,
                clientsController.getFilteredClients("name", "name")
        );
    }

    @Test
    void forFilterByShortNameShouldReturnPageWithClients() {
        Clients client = new Clients();
        List<Clients> listOfClient = new ArrayList<>();
        listOfClient.add(client);

        Page<Clients> page = new PageImpl<>(listOfClient, Pageable.unpaged(), 1);

        Mockito.when(clientsServiceMock.filterClientsByShortName("name")).thenReturn(page);

        assertEquals(
                "Неверный ответ",
                page,
                clientsController.getFilteredClients("shortName", "name")
        );
    }

    @Test
    void forFilterByAddressShouldReturnPageWithClients() {
        Clients client = new Clients();
        List<Clients> listOfClient = new ArrayList<>();
        listOfClient.add(client);

        Page<Clients> page = new PageImpl<>(listOfClient, Pageable.unpaged(), 1);

        Mockito.when(clientsServiceMock.filterClientsByAddress("name")).thenReturn(page);

        assertEquals(
                "Неверный ответ",
                page,
                clientsController.getFilteredClients("address", "name")
        );
    }

    @Test
    void forFilterByLegalFormShouldReturnPageWithClients() {
        LegalForm legalForm = new LegalForm();

        Mockito.when(legalFormServiceMock.getLegalFormByName("name")).thenReturn(legalForm);

        Clients client = new Clients();
        List<Clients> listOfClient = new ArrayList<>();
        listOfClient.add(client);

        Page<Clients> page = new PageImpl<>(listOfClient, Pageable.unpaged(), 1);

        Mockito.when(clientsServiceMock.filterClientsByLegalForm(legalForm)).thenReturn(page);

        assertEquals(
                "Неверная страница",
                page,
                clientsController.getFilteredClients("legalForm", "name")
        );
    }

    @Test
    void forFilteredByWrongFilterKeyShouldReturnEmptyPage() {
        assertEquals(
                "Неверная страница",
                Page.empty(),
                clientsController.getFilteredClients("null", "test")
        );
    }

    @Test
    void forUpdateClientShouldReturnMessageWithClient() {
        Clients client = new Clients();

        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(client);
        Mockito.when(clientsServiceMock.updateClient(client)).thenReturn(client);

        ResponseEntity<Object> responseEntityActual = clientsController.updateClient(
                "test",
                Optional.of("tst"),
                Optional.of("test"),
                Optional.of("ИП")
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                clientsServiceMock.updateClient(client), HttpStatus.OK
        );

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forUpdateClientShouldReturnMessageClientDoesntExist() {
        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(null);

        ResponseEntity<Object> responseEntityActual = clientsController.updateClient(
                "test",
                Optional.of("tst"),
                Optional.of("test"),
                Optional.of("ИП")
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Клиент еще не существует", HttpStatus.BAD_REQUEST);

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forDeleteClientShouldReturnMessageClientWasDeleted() {
        Clients client = new Clients();

        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(client);

        ResponseEntity<String> responseEntityActual = clientsController.deleteClient("test");

        ResponseEntity<String> responseEntityExpected = new ResponseEntity<>("Клиент был удален", HttpStatus.OK);

        assertEquals("Неверное сообщение", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forDeleteClientShouldReturnMessageClientDoesntExist() {
        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(null);

        ResponseEntity<String> responseEntityActual = clientsController.deleteClient("test");

        ResponseEntity<String> responseEntityExpected
                = new ResponseEntity<>("Не существует клиента на удаление", HttpStatus.BAD_REQUEST);

        assertEquals("Неверное сообщение", responseEntityExpected, responseEntityActual);
    }
}
