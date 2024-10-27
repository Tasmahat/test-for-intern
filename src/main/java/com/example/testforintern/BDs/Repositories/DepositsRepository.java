package com.example.testforintern.BDs.Repositories;

import com.example.testforintern.BDs.Entities.Banks;
import com.example.testforintern.BDs.Entities.Clients;
import com.example.testforintern.BDs.Entities.Deposits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;


public interface DepositsRepository extends JpaRepository<Deposits, Long> {

    Deposits findByClientAndBankAndOpenDate(Clients client, Banks bank, Date openDate);

    Page<Deposits> findByClient(Clients client, Pageable pageable);

    Page<Deposits> findByBank(Banks bank, Pageable pageable);

    Page<Deposits> findByOpenDateBetween(Date openDateStart, Date openDateEnd, Pageable pageable);

    Page<Deposits> findByPercentBetween(int percentStart, int percentEnd, Pageable pageable);

    Page<Deposits> findByTermBetween(int termStart, int termEnd, Pageable pageable);
}
