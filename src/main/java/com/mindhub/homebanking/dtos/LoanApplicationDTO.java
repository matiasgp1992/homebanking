package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class LoanApplicationDTO {
    //ATRIBUTOS
    private String account;
    private String name;
    private double amount;
    private int payment;

    //CONSTRUCTORES
    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(String account, String name, int amount, int payment) {
        this.account = account; //cuenta destino
        this.name = name; //nombre del prestamo o deberia poner el id? no entiendo
        this.amount = amount;
        this.payment = payment;
    }


    //GETTERS AND SETTERS

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }
}
