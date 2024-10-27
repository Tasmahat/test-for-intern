package com.example.testforintern.BDs.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "legal_form")
public class LegalForm {

    @Id
    @Column(name = "legal_form_name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "legalForm", cascade = CascadeType.ALL)
    private Set<Clients> clientsSet = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClientsSet(Set<Clients> clientsSet) {
        this.clientsSet = clientsSet;
    }
}
