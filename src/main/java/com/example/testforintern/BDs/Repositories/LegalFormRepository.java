package com.example.testforintern.BDs.Repositories;

import com.example.testforintern.BDs.Entities.LegalForm;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LegalFormRepository extends JpaRepository<LegalForm, Long> {

    LegalForm findByName(String name);
}

