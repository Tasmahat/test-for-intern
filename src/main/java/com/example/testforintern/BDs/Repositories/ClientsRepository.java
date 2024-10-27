package com.example.testforintern.BDs.Repositories;

import com.example.testforintern.BDs.Entities.Clients;
import com.example.testforintern.BDs.Entities.LegalForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<Clients, Long> {

    Clients getByName(String name);

    Page<Clients> findByNameContainingIgnoreCase(String subString, Pageable pageable);

    Page<Clients> findByShortNameContainingIgnoreCase(String subString, Pageable pageable);

    Page<Clients> findByAddressContainingIgnoreCase(String subString, Pageable pageable);

    Page<Clients> findByLegalForm(LegalForm legalForm, Pageable pageable);
}

