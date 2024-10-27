package com.example.testforintern.BDs.Controllers;

import com.example.testforintern.BDs.Entities.Banks;
import com.example.testforintern.BDs.Entities.Clients;
import com.example.testforintern.BDs.Entities.Deposits;
import com.example.testforintern.BDs.Services.BanksService;
import com.example.testforintern.BDs.Services.ClientsService;
import com.example.testforintern.BDs.Services.DepositsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/deposits")
public class DepositsController {

    private final DepositsService depositsService;
    private final BanksService banksService;
    private final ClientsService clientsService;

    @Autowired
    public DepositsController(
            DepositsService depositsService,
            BanksService banksService,
            ClientsService clientsService
    ) {
        this.depositsService = depositsService;
        this.banksService = banksService;
        this.clientsService = clientsService;
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createDeposit(
            @RequestParam String clientName,
            @RequestParam String bankBic,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date openDate,
            @RequestParam int percent,
            @RequestParam int term
    ) {
        Clients client = clientsService.getClientByName(clientName);
        Banks bank = banksService.getBankByBic(bankBic);

        if (!doesClientAndBankExist(client, bank)) {
            return new ResponseEntity<>(
                    "Клиент или банк не существует, невозможно создать вклад",
                    HttpStatus.BAD_REQUEST
            );
        } else if (doesDepositExists(depositsService.getDepositByPK(client, bank, openDate))) {
            return new ResponseEntity<>("Вклад уже существует", HttpStatus.BAD_REQUEST);
        }

        Deposits deposit = new Deposits();
        deposit.setClient(client);
        deposit.setBank(bank);
        deposit.setPercent(percent);
        deposit.setOpenDate(openDate);
        deposit.setTerm(term);

        return new ResponseEntity<>(depositsService.createDeposit(deposit), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<Deposits> getAllDeposits() {
        return depositsService.getAllDeposits();
    }

    @GetMapping("/sort")
    public List<Deposits> getSortedDeposits(
            @RequestParam (name = "params") List<String> listOfSortParams
    ) {
        try {
            return depositsService.sortDeposits(listOfSortParams);
        } catch (RuntimeException e) {
            System.out.println("Возникла ошибка: " + e);
            return List.of();
        }
    }

    @GetMapping("/filter")
    public Page<Deposits> getFilteredDeposits(
            @RequestParam String filterKey,
            @RequestParam Optional<String> stringFilterValue,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<Date> dateFilterValueStart,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<Date> dateFilterValueEnd,
            @RequestParam Optional<Integer> intFilterValueStart,
            @RequestParam Optional<Integer> intFilterValueEnd
    ) {
        switch (filterKey.toLowerCase(Locale.ENGLISH)) {
            case "client" -> {
                String clientName = stringFilterValue.orElse(null);
                if (clientName == null) {
                    return Page.empty();
                }
                Clients client = clientsService.getClientByName(clientName);
                if (client == null) {
                    return Page.empty();
                }
                return depositsService.filterDepositsByClient(client);
            }
            case "bank" -> {
                String bankBic = stringFilterValue.orElse(null);
                if (bankBic == null) {
                    return Page.empty();
                }
                Banks bank = banksService.getBankByBic(bankBic);
                if (bank == null) {
                    return Page.empty();
                }
                return depositsService.filterDepositsByBank(bank);
            }
            case "opendate" -> {
                Date openDateStart = dateFilterValueStart.orElse(null);
                Date openDateEnd = dateFilterValueEnd.orElse(null);
                if (openDateStart == null || openDateEnd == null) {
                    return Page.empty();
                }
                return depositsService.filterDepositsByDate(openDateStart, openDateEnd);
            }
            case "percent" -> {
                Integer percentStart = intFilterValueStart.orElse(null);
                Integer percentEnd = intFilterValueEnd.orElse(null);
                if (percentStart == null || percentEnd == null) {
                    return Page.empty();
                }
                return depositsService.filterDepositsByPercent(percentStart, percentEnd);
            }
            case "term" -> {
                Integer termStart = intFilterValueStart.orElse(null);
                Integer termEnd = intFilterValueEnd.orElse(null);
                if (termStart == null || termEnd == null) {
                    return Page.empty();
                }
                return depositsService.filterDepositsByTerm(termStart, termEnd);
            }
            default -> {
                return Page.empty();
            }
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateDeposit(
            @RequestParam String clientName,
            @RequestParam String bankBic,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yy") Date openDate,
            @RequestParam Optional<Integer> percent,
            @RequestParam Optional<Integer> term
    ) {
        Clients client = clientsService.getClientByName(clientName);
        Banks bank = banksService.getBankByBic(bankBic);

        if (!doesClientAndBankExist(client, bank)) {
            return new ResponseEntity<>(
                    "Клиент или банк не существует, невозможно изменить вклад",
                    HttpStatus.BAD_REQUEST
            );
        }

        Deposits deposit = depositsService.getDepositByPK(client, bank, openDate);

        if (!doesDepositExists(deposit)) {
            return new ResponseEntity<>("Вклада не существует", HttpStatus.BAD_REQUEST);
        }

        percent.ifPresent(deposit::setPercent);
        term.ifPresent(deposit::setTerm);

        return new ResponseEntity<>(depositsService.updateDeposit(deposit), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDeposit(
        @RequestParam String clientName,
        @RequestParam String bankBic,
        @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date openDate
    ) {
        Clients client = clientsService.getClientByName(clientName);
        Banks bank = banksService.getBankByBic(bankBic);

        if (!doesClientAndBankExist(client, bank)) {
            return new ResponseEntity<>(
                    "Клиент или банк не существует, невозможно удалить вклад",
                    HttpStatus.BAD_REQUEST
            );
        }

        Deposits deposit = depositsService.getDepositByPK(client, bank, openDate);

        if (!doesDepositExists(deposit)) {
            return new ResponseEntity<>("Вклада не существует", HttpStatus.BAD_REQUEST);
        }

        depositsService.deleteDeposit(deposit);

        return new ResponseEntity<>("Вклад удален", HttpStatus.OK);
    }

    private boolean doesClientAndBankExist(Clients client, Banks bank) {
        return client != null && bank != null;
    }

    private boolean doesDepositExists(Deposits deposit) {
        return deposit != null;
    }
}