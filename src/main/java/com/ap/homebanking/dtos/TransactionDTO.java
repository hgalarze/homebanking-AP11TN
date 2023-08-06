package com.ap.homebanking.dtos;

import com.ap.homebanking.models.TransactionType;

import java.time.LocalDate;

public class TransactionDTO {

    private long id;

    private TransactionType type;

    private double Amount;

    private LocalDate Date;

    private String Description;


    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return Amount;
    }

    public LocalDate getDate() {
        return Date;
    }

    public String getDescription() {
        return Description;
    }
}
