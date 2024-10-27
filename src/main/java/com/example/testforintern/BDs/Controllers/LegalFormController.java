package com.example.testforintern.BDs.Controllers;

import com.example.testforintern.BDs.Entities.LegalForm;
import com.example.testforintern.BDs.Services.LegalFormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/legal_forms")
public class LegalFormController {

    private final LegalFormService legalFormService;

    @Autowired
    public LegalFormController(LegalFormService legalFormService) {
        this.legalFormService = legalFormService;
    }

    @GetMapping
    public List<LegalForm> getAllLegalForm() {
        return legalFormService.getAllLegalForm();
    }
}
