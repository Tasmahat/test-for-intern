package com.example.testforintern.BDs.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Clients {

    @Id
    @Column(name = "client_name")
    private String name;

    @Column(name = "short_client_name")
    private String shortName;

    @Column(name = "client_address")
    private String address;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "legal_form_name")
    private LegalForm legalForm = new LegalForm();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Deposits> depositsSet = new HashSet<>();

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getAddress() {
        return address;
    }

    public LegalForm getLegalForm() {
        return legalForm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLegalForm(LegalForm legalForm) {
        this.legalForm = legalForm;
    }

    public void setDepositsSet(Set<Deposits> depositsSet) {
        this.depositsSet = depositsSet;
    }

    @PreRemove
    private void detachLegalForm() {
        legalForm = null;
    }
}
