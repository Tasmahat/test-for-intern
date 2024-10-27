package com.example.testforintern.BDs.Repositories;

import com.example.testforintern.BDs.Entities.Banks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanksRepository extends JpaRepository<Banks, Long> {

    Banks getByBic(String bic);

    Page<Banks> findByNameContainingIgnoreCase(String subString, Pageable pageable);

    Page<Banks> findByBicContainingIgnoreCase(String subString, Pageable pageable);
}

