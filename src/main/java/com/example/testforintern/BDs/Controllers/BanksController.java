package com.example.testforintern.BDs.Controllers;

import com.example.testforintern.BDs.Entities.Banks;
import com.example.testforintern.BDs.Services.BanksService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/banks")
public class BanksController {

    private final BanksService banksService;

    @Autowired
    public BanksController(BanksService banksService) {
        this.banksService = banksService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createBank(
            @RequestParam String name,
            @RequestParam String bic
    ) {

        if (banksService.getBankByBic(bic) != null) {
            return new ResponseEntity<>(
                    "Банк с таким БИК уже существует",
                    HttpStatus.BAD_REQUEST
            );
        }

        Banks bank  = new Banks();
        bank.setBic(bic);
        bank.setName(name);

        return new ResponseEntity<>(banksService.createBank(bank), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<Banks> getAllBanks() {
        return banksService.getAllBanks();
    }

    @GetMapping("/sort")
    public List<Banks> getSortedBanks(
            @RequestParam (name = "params") List<String> listOfSortParams
    ) {
        try {
            return banksService.sortBanks(listOfSortParams);
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    @GetMapping("/filter")
    public Page<Banks> getFilteredBanks(@RequestParam String filterKey, @RequestParam String filterValue) {
        return switch (filterKey.toLowerCase(Locale.ENGLISH)) {
            case "name" -> banksService.filterBanksByName(filterValue);
            case "bic" -> banksService.filterBanksByBic(filterValue);
            default -> Page.empty();
        };
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateBank(
            @RequestParam String name,
            @RequestParam String bic
    ) {
        Banks bank = banksService.getBankByBic(bic);
        if (bank == null) {
            return new ResponseEntity<>("Банк еще не существует", HttpStatus.BAD_REQUEST);
        }

        bank.setName(name);

        return new ResponseEntity<>(banksService.updateBank(bank), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBank(@RequestParam String bic) {
        Banks bank = banksService.getBankByBic(bic);

        if (bank == null) {
            return new ResponseEntity<>("Не существует банк на удаление", HttpStatus.BAD_REQUEST);
        }

        banksService.deleteBank(bank);

        return new ResponseEntity<>("Банк был удален", HttpStatus.OK);
    }
}
