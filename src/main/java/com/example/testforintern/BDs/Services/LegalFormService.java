package com.example.testforintern.BDs.Services;

import com.example.testforintern.BDs.Entities.LegalForm;
import com.example.testforintern.BDs.Repositories.LegalFormRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegalFormService {

    private final LegalFormRepository legalFormRepository;

    @Autowired
    public LegalFormService(LegalFormRepository legalFormRepository) {
        this.legalFormRepository = legalFormRepository;
    }

    public List<LegalForm> getAllLegalForm() {
        return legalFormRepository.findAll();
    }

    public LegalForm getLegalFormByName(String name) {
        return legalFormRepository.findByName(name);
    }
}
