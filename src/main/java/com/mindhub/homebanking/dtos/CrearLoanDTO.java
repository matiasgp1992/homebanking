package com.mindhub.homebanking.dtos;

import java.util.ArrayList;
import java.util.List;

public class CrearLoanDTO {
    //ATRIBUTOS
    private String name;
    private double maxAmount;
    private List<Integer> payments= new ArrayList<>();
    private double interest;

    //CONSTRUCTORES
    public CrearLoanDTO() {
    }

    public CrearLoanDTO(String name, double maxAmount, List<Integer> payments, double interest) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
    }

    //GETTERS AND SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
}
