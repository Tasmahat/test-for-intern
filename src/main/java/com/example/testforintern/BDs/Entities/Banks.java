package com.example.testforintern.BDs.Entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "banks")
public class Banks {

    @Column(name = "bank_name")
    private String name;

    @Id
    @Column(name = "bank_bic")
    private String bic;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bank", cascade = CascadeType.ALL)
    private Set<Deposits> depositsSet = new HashSet<>();

    public String getBic() {
        return bic;
    }

    public String getName() {
        return name;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepositsSet(Set<Deposits> depositsSet) {
        this.depositsSet = depositsSet;
    }
}
