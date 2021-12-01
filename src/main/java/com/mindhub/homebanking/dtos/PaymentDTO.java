package com.mindhub.homebanking.dtos;

import java.time.LocalDate;

public class PaymentDTO {
    //ATRIBUTTOS

    private String number;
    private int cvv;

    private String description;
    private double amount;

    //CONSTRUCTORES
    public PaymentDTO() {
    }

    public PaymentDTO(String number, int cvv, String description, double amount) {
        this.number = number;
        this.cvv = cvv;
        this.description = description;
        this.amount = amount;
    }

    //GETTERS AND SETTERS
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
