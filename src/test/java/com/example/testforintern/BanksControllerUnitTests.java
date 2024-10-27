package com.example.testforintern;

import com.example.testforintern.BDs.Controllers.BanksController;
import com.example.testforintern.BDs.Entities.Banks;
import com.example.testforintern.BDs.Services.BanksService;

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

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class BanksControllerUnitTests {
    BanksService banksServiceMock = Mockito.mock(BanksService.class);

    BanksController banksController = new BanksController(banksServiceMock);

    @Test
    void forCreateBankShouldReturnStatusCreated() {
        Banks bank = new Banks();

        Mockito.when(banksServiceMock.createBank(bank)).thenReturn(bank);

        ResponseEntity<Object> responseEntityActual = banksController.createBank(
                "test",
                "tst"
        );

        assertEquals("Неверный статус ответа" , HttpStatus.CREATED, responseEntityActual.getStatusCode());
    }

    @Test
    void forCreateBankShouldReturnMessageBankAlreadyExists() {
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(new Banks());

        ResponseEntity<Object> responseEntityActual = banksController.createBank(
                "test",
                "test"
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Банк с таким БИК уже существует",
                HttpStatus.BAD_REQUEST
        );

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forGetAllBanksShouldReturnPageOfBanks() {
        Banks bank = new Banks();
        List<Banks> listOfBanks = new ArrayList<>();
        listOfBanks.add(bank);

        Page<Banks> page = new PageImpl<>(listOfBanks, Pageable.unpaged(), 1);

        Mockito.when(banksServiceMock.getAllBanks()).thenReturn(page);

        assertEquals("Неверная страница", page, banksController.getAllBanks());
    }

    @Test
    void forSortBanksShouldReturnList() {
        List<Banks> banksList = Mockito.anyList();
        Mockito.when(banksServiceMock.sortBanks(List.of())).thenReturn(banksList);

        assertEquals("Неверный ответ", banksList, banksController.getSortedBanks(List.of()));
    }

    @Test
    void forWrongSortParamShouldReturnEmptyList() {
        Mockito.when(banksServiceMock.sortBanks(List.of())).thenThrow(new RuntimeException());

        assertEquals("Неверный ответ", List.of(), banksController.getSortedBanks(List.of()));
    }

    @Test
    void forFilterByBicShouldReturnPageWithBanks() {
        Banks bank = new Banks();
        List<Banks> listOfBanks = new ArrayList<>();
        listOfBanks.add(bank);

        Page<Banks> page = new PageImpl<>(listOfBanks, Pageable.unpaged(), 1);

        Mockito.when(banksServiceMock.filterBanksByBic("name")).thenReturn(page);

        assertEquals(
                "Неверный ответ",
                page,
                banksController.getFilteredBanks("bic", "name")
        );
    }

    @Test
    void forFilterByNameShouldReturnPageWithBanks() {
        Banks bank = new Banks();
        List<Banks> listOfBanks = new ArrayList<>();
        listOfBanks.add(bank);

        Page<Banks> page = new PageImpl<>(listOfBanks, Pageable.unpaged(), 1);

        Mockito.when(banksServiceMock.filterBanksByName("name")).thenReturn(page);

        assertEquals(
                "Неверный ответ",
                page,
                banksController.getFilteredBanks("name", "name")
        );
    }

    @Test
    void forFilteredByWrongFilterKeyShouldReturnEmptyPage() {
        assertEquals(
                "Неверная страница",
                Page.empty(),
                banksController.getFilteredBanks("null", "test")
        );
    }

    @Test
    void forUpdateBankShouldReturnMessageWithBank() {
        Banks bank = new Banks();

        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(bank);
        Mockito.when(banksServiceMock.updateBank(bank)).thenReturn(bank);

        ResponseEntity<Object> responseEntityActual = banksController.updateBank(
                "test",
                "test"
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                banksServiceMock.updateBank(bank), HttpStatus.OK
        );

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forUpdateBankShouldReturnMessageBankDoesntExist() {
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(null);

        ResponseEntity<Object> responseEntityActual = banksController.updateBank(
                "test",
                "test"
        );

        ResponseEntity<Object> responseEntityExpected = new ResponseEntity<>(
                "Банк еще не существует", HttpStatus.BAD_REQUEST);

        assertEquals("Неверный ответ", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forDeleteBankShouldReturnMessageBankWasDeleted() {
        Banks bank = new Banks();

        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(bank);

        ResponseEntity<String> responseEntityActual = banksController.deleteBank("test");

        ResponseEntity<String> responseEntityExpected = new ResponseEntity<>("Банк был удален", HttpStatus.OK);

        assertEquals("Неверное сообщение", responseEntityExpected, responseEntityActual);
    }

    @Test
    void forDeleteBankShouldReturnMessageBankDoesntExist() {
        Mockito.when(banksServiceMock.getBankByBic("test")).thenReturn(null);

        ResponseEntity<String> responseEntityActual = banksController.deleteBank("test");

        ResponseEntity<String> responseEntityExpected
                = new ResponseEntity<>("Не существует банк на удаление", HttpStatus.BAD_REQUEST);

        assertEquals("Неверное сообщение", responseEntityExpected, responseEntityActual);
    }
}
