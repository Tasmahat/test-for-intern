package com.example.testforintern;

import com.example.testforintern.BDs.Controllers.LegalFormController;
import com.example.testforintern.BDs.Entities.LegalForm;
import com.example.testforintern.BDs.Services.LegalFormService;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class LegalFormControllerUnitTests {
    LegalFormService legalFormServiceMock = Mockito.mock(LegalFormService.class);

    LegalFormController legalFormController = new LegalFormController(legalFormServiceMock);

    @Test
    void forGetAllLegalFormShouldReturnListOfLegalForm() {
        LegalForm legalForm = new LegalForm();
        List<LegalForm> listOfLegalForm = new ArrayList<>();
        listOfLegalForm.add(legalForm);

        Mockito.when(legalFormServiceMock.getAllLegalForm()).thenReturn(listOfLegalForm);

        assertEquals("Неверная страница", listOfLegalForm, legalFormController.getAllLegalForm());
    }
}
