package com.example.testforintern.BDs.Entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Embeddable
public class DepositsPK implements Serializable {

    private Clients client;

    private Banks bank;

    private Date openDate;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
