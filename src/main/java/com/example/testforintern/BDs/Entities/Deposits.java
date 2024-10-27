package com.example.testforintern.BDs.Entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "deposits")
@IdClass(DepositsPK.class)
public class Deposits {

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_name")
    private Clients client;

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_bic")
    private Banks bank;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "deposit_open_date")
    private Date openDate;

    @Column(name = "deposit_percent")
    private int percent;

    @Column(name = "deposit_term")
    private int term;

    public Clients getClient() {
        return client;
    }

    public Banks getBank() {
        return bank;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public int getPercent() {
        return percent;
    }

    public int getTerm() {
        return term;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public void setBank(Banks bank) {
        this.bank = bank;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    @PreRemove
    private void detachClientAndBank() {
        this.client = null;
        this.bank = null;
    }
}
