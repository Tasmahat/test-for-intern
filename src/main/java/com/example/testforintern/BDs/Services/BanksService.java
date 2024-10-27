package com.example.testforintern.BDs.Services;

import com.example.testforintern.BDs.Entities.Banks;
import com.example.testforintern.BDs.Repositories.BanksRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BanksService {

    private final BanksRepository banksRepository;
    private final Pageable pageableUnPaged = Pageable.unpaged();

    @Autowired
    public BanksService(BanksRepository banksRepository) {
        this.banksRepository = banksRepository;
    }

    public Banks createBank(Banks bank) {
        return banksRepository.save(bank);
    }

    public Page<Banks> getAllBanks() {
        return banksRepository.findAll(pageableUnPaged);
    }

    public Banks getBankByBic(String bic) {
        return banksRepository.getByBic(bic);
    }

    public List<Banks> sortBanks(List<String> listOfSortingParams) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String i : listOfSortingParams) {
            orders.add(new Sort.Order(Sort.Direction.ASC, i));
        }

        return banksRepository.findAll(Sort.by(orders));
    }

    public Page<Banks> filterBanksByName(String subString) {
        return banksRepository.findByNameContainingIgnoreCase(subString, pageableUnPaged);
    }

    public Page<Banks> filterBanksByBic(String subString) {
        return banksRepository.findByBicContainingIgnoreCase(subString, pageableUnPaged);
    }

    public Banks updateBank(Banks bank) {
        return banksRepository.save(bank);
    }

    public void deleteBank(Banks bank) {
        banksRepository.delete(bank);
    }
}
