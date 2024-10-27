package com.example.testforintern;

import com.example.testforintern.BDs.Controllers.DepositsController;
import com.example.testforintern.BDs.Entities.Banks;
import com.example.testforintern.BDs.Entities.Clients;
import com.example.testforintern.BDs.Entities.Deposits;
import com.example.testforintern.BDs.Services.BanksService;
import com.example.testforintern.BDs.Services.ClientsService;
import com.example.testforintern.BDs.Services.DepositsService;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class DepositsControllerUnitTests {
    DepositsService depositsServiceMock = Mockito.mock(DepositsService.class);
    ClientsService clientsServiceMock = Mockito.mock(ClientsService.class);
    BanksService banksServiceMock = Mockito.mock(BanksService.class);

    DepositsController depositsController =
            new DepositsController(depositsServiceMock, banksServiceMock, clientsServiceMock);

    @Test
    void forCreateDepositShouldReturnStatusCreated() {
        Deposits deposit = new Deposits();

        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(new Clients());
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(new Banks());
        Mockito.when(depositsServiceMock.createDeposit(deposit)).thenReturn(deposit);

        ResponseEntity<Object> responseEntityActual = depositsController.createDeposit(
                "test",
                "test",
                new Date(),
                123,
                123
        );

        assertEquals("Неверный статус ответа" , HttpStatus.CREATED, responseEntityActual.getStatusCode());
    }

    @Test
    void forCreateDepositWithNullClientShouldReturnMessageClientOrBankDoesntExist() {
        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(new Clients());

        ResponseEntity<Object> responseEntityActual = depositsController.createDeposit(
                "test",
                "test",
                new Date(),
                123,
                123
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Клиент или банк не существует, невозможно создать вклад",
                HttpStatus.BAD_REQUEST
        );

        assertEquals("Неверный ответ" , responseEntityExpected, responseEntityActual);
    }

    @Test
    void forCreateDepositWithNullBankShouldReturnMessageClientOrBankDoesntExist() {
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(new Banks());

        ResponseEntity<Object> responseEntityActual = depositsController.createDeposit(
                "test",
                "test",
                new Date(),
                123,
                123
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Клиент или банк не существует, невозможно создать вклад",
                HttpStatus.BAD_REQUEST
        );

        assertEquals("Неверный ответ" , responseEntityExpected, responseEntityActual);
    }

    @Test
    void forCreateDepositShouldReturnMessageDepositAlreadyExists() {
        Clients client = new Clients();
        Banks bank = new Banks();
        Date date = new Date();

        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(client);
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(bank);
        Mockito.when(depositsServiceMock.getDepositByPK(client, bank, date))
                .thenReturn(new Deposits());

        ResponseEntity<Object> responseEntityActual = depositsController.createDeposit(
                "test",
                "test",
                date,
                123,
                123
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Вклад уже существует",
                HttpStatus.BAD_REQUEST
        );

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forGetAllDepositsShouldReturnPageOfDeposits() {
        Deposits deposit = new Deposits();
        List<Deposits> listOfDeposits = new ArrayList<>();
        listOfDeposits.add(deposit);

        Page<Deposits> page = new PageImpl<>(listOfDeposits, Pageable.unpaged(), 1);

        Mockito.when(depositsServiceMock.getAllDeposits()).thenReturn(page);

        assertEquals("Неверная страница", page, depositsController.getAllDeposits());
    }

    @Test
    void forSortDepositsShouldReturnList() {
        List<Deposits> depositsList = Mockito.anyList();
        Mockito.when(depositsServiceMock.sortDeposits(List.of())).thenReturn(depositsList);

        assertEquals("Неверный ответ", depositsList, depositsController.getSortedDeposits(List.of()));
    }

    @Test
    void forWrongSortParamShouldReturnEmptyList() {
        Mockito.when(depositsServiceMock.sortDeposits(List.of())).thenThrow(new RuntimeException());

        assertEquals("Неверный ответ", List.of(), depositsController.getSortedDeposits(List.of()));
    }

    @Test
    void forFilterByClientShouldReturnPageWithDeposits() {
        Deposits deposit = new Deposits();
        List<Deposits> listOfDeposits = new ArrayList<>();
        listOfDeposits.add(deposit);

        Page<Deposits> page = new PageImpl<>(listOfDeposits, Pageable.unpaged(), 1);

        Clients client = new Clients();

        Mockito.when(depositsServiceMock.filterDepositsByClient(client)).thenReturn(page);
        Mockito.when(clientsServiceMock.getClientByName("name")).thenReturn(client);

        assertEquals(
                "Неверный ответ",
                page,
                depositsController.getFilteredDeposits(
                        "client",
                        Optional.of("name"),
                        Optional.of(new Date()),
                        Optional.of(new Date()),
                        Optional.of(1),
                        Optional.of(1))
        );
    }

    @Test
    void forFilterByBankShouldReturnPageWithDeposits() {
        Deposits deposit = new Deposits();
        List<Deposits> listOfDeposits = new ArrayList<>();
        listOfDeposits.add(deposit);

        Page<Deposits> page = new PageImpl<>(listOfDeposits, Pageable.unpaged(), 1);

        Banks bank = new Banks();

        Mockito.when(depositsServiceMock.filterDepositsByBank(bank)).thenReturn(page);
        Mockito.when(banksServiceMock.getBankByBic("name")).thenReturn(bank);

        assertEquals(
                "Неверный ответ",
                page,
                depositsController.getFilteredDeposits(
                        "bank",
                        Optional.of("name"),
                        Optional.of(new Date()),
                        Optional.of(new Date()),
                        Optional.of(1),
                        Optional.of(1))
        );
    }

    @Test
    void forFilterByOpenDateShouldReturnPageWithDeposits() {
        Deposits deposit = new Deposits();
        List<Deposits> listOfDeposits = new ArrayList<>();
        listOfDeposits.add(deposit);

        Date dateStart = new Date();
        Date dateEnd = new Date();

        Page<Deposits> page = new PageImpl<>(listOfDeposits, Pageable.unpaged(), 1);

        Mockito.when(depositsServiceMock.filterDepositsByDate(dateStart, dateEnd)).thenReturn(page);

        assertEquals(
                "Неверный ответ",
                page,
                depositsController.getFilteredDeposits(
                        "openDate",
                        Optional.of("test"),
                        Optional.of(dateStart),
                        Optional.of(dateEnd),
                        Optional.of(1),
                        Optional.of(1))
        );
    }

    @Test
    void forFilterByPercentShouldReturnPageWithDeposits() {
        Deposits deposit = new Deposits();
        List<Deposits> listOfDeposits = new ArrayList<>();
        listOfDeposits.add(deposit);

        Page<Deposits> page = new PageImpl<>(listOfDeposits, Pageable.unpaged(), 1);

        Mockito.when(depositsServiceMock.filterDepositsByPercent(1, 1)).thenReturn(page);


        assertEquals(
                "Неверный ответ",
                page,
                depositsController.getFilteredDeposits(
                        "percent",
                        Optional.of("test"),
                        Optional.of(new Date()),
                        Optional.of(new Date()),
                        Optional.of(1),
                        Optional.of(1))
        );
    }

    @Test
    void forFilterByTermShouldReturnPageWithDeposits() {
        Deposits deposit = new Deposits();
        List<Deposits> listOfDeposits = new ArrayList<>();
        listOfDeposits.add(deposit);

        Page<Deposits> page = new PageImpl<>(listOfDeposits, Pageable.unpaged(), 1);

        Mockito.when(depositsServiceMock.filterDepositsByTerm(1, 1)).thenReturn(page);


        assertEquals(
                "Неверный ответ",
                page,
                depositsController.getFilteredDeposits(
                        "term",
                        Optional.of("test"),
                        Optional.of(new Date()),
                        Optional.of(new Date()),
                        Optional.of(1),
                        Optional.of(1))
        );
    }

    @Test
    void forFilteredByWrongFilterKeyShouldReturnEmptyPage() {
        assertEquals(
                "Неверная страница",
                Page.empty(),
                depositsController.getFilteredDeposits(
                        "null",
                        Optional.of("test"),
                        Optional.of(new Date()),
                        Optional.of(new Date()),
                        Optional.of(1),
                        Optional.of(1)
                )
        );
    }

    @Test
    void forUpdateDepositShouldReturnMessageWithDeposit() {
        Clients client = new Clients();
        Banks bank = new Banks();
        Date date = new Date();

        Deposits deposit = new Deposits();

        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(client);
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(bank);
        Mockito.when(depositsServiceMock.getDepositByPK(client, bank, date)).thenReturn(deposit);
        Mockito.when(depositsServiceMock.updateDeposit(deposit)).thenReturn(deposit);

        ResponseEntity<Object> responseEntityActual = depositsController.updateDeposit(
                "test",
                "test",
                date,
                Optional.of(1),
                Optional.of(1)
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                depositsServiceMock.updateDeposit(deposit), HttpStatus.OK
        );

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    public void forUpdateDepositShouldReturnMessageDepositDoesntExist() {
        Clients client = new Clients();
        Banks bank = new Banks();
        Date date = new Date();

        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(client);
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(bank);
        Mockito.when(depositsServiceMock.getDepositByPK(client, bank, date)).thenReturn(null);

        ResponseEntity<Object> responseEntityActual = depositsController.updateDeposit(
                "test",
                "test",
                date,
                Optional.of(1),
                Optional.of(1)
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Вклада не существует", HttpStatus.BAD_REQUEST);

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forDeleteDepositShouldReturnMessageDepositWasDeleted() {
        Clients client = new Clients();
        Banks bank = new Banks();
        Date date = new Date();

        Deposits deposit = new Deposits();

        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(client);
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(bank);
        Mockito.when(depositsServiceMock.getDepositByPK(client, bank, date)).thenReturn(deposit);

        ResponseEntity<String> responseEntityActual
                = depositsController.deleteDeposit("test", "test", date);

        ResponseEntity<String> responseEntityExpected = new ResponseEntity<>("Вклад удален", HttpStatus.OK);

        assertEquals("Неверное сообщение", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forDeleteDepositShouldReturnMessageDepositDoesntExist() {
        Clients client = new Clients();
        Banks bank = new Banks();
        Date date = new Date();

        Mockito.when(clientsServiceMock.getClientByName("test")).thenReturn(client);
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(bank);
        Mockito.when(depositsServiceMock.getDepositByPK(client, bank, date)).thenReturn(null);

        ResponseEntity<String> responseEntityActual
                = depositsController.deleteDeposit("test", "test", date);

        ResponseEntity<String> responseEntityExpected
                = new ResponseEntity<>("Вклада не существует", HttpStatus.BAD_REQUEST);

        assertEquals("Неверное сообщение", responseEntityExpected, responseEntityActual);
    }
}
