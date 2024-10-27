package com.example.testforintern.BDs.Services;

import com.example.testforintern.BDs.Entities.Banks;
import com.example.testforintern.BDs.Entities.Clients;
import com.example.testforintern.BDs.Entities.Deposits;
import com.example.testforintern.BDs.Repositories.DepositsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DepositsService {

    private final DepositsRepository depositsRepository;
    private final Pageable pageableUnPaged = Pageable.unpaged();

    @Autowired
    public DepositsService(DepositsRepository depositsRepository) {
        this.depositsRepository = depositsRepository;
    }

    public Deposits createDeposit(Deposits deposit) {
        return depositsRepository.save(deposit);
    }

    public Page<Deposits> getAllDeposits() {
        return depositsRepository.findAll(pageableUnPaged);
    }

    public Deposits getDepositByPK(Clients client, Banks bank, Date openDate) {
        return depositsRepository.findByClientAndBankAndOpenDate(client, bank, openDate);
    }

    public List<Deposits> sortDeposits(List<String> listOfSortingParams) {
        List<Sort.Order> orders = new ArrayList<>();
        for (String i : listOfSortingParams) {
            orders.add(new Sort.Order(Sort.Direction.ASC, i));
        }

        return depositsRepository.findAll(Sort.by(orders));
    }

    public Page<Deposits> filterDepositsByClient(Clients client) {
        return depositsRepository.findByClient(client, pageableUnPaged);
    }

    public Page<Deposits> filterDepositsByBank(Banks bank) {
        return depositsRepository.findByBank(bank, pageableUnPaged);
    }

    public Page<Deposits> filterDepositsByDate(Date openDateStart, Date openDateEnd) {
        return depositsRepository.findByOpenDateBetween(openDateStart, openDateEnd, pageableUnPaged);
    }

    public Page<Deposits> filterDepositsByPercent(int percentStart, int percentEnd) {
        return depositsRepository.findByPercentBetween(percentStart, percentEnd, pageableUnPaged);
    }

    public Page<Deposits> filterDepositsByTerm(int termStart, int termEnd) {
        return depositsRepository.findByTermBetween(termStart, termEnd, pageableUnPaged);
    }

    public Deposits updateDeposit(Deposits deposit) {
        return depositsRepository.save(deposit);
    }

    public void deleteDeposit(Deposits deposit) {
        depositsRepository.delete(deposit);
    }
}
