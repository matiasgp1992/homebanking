package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    //ATRIBUTOS
    private long id;
    private long loanId;
    private String name;
    private double amount;
    private int payments;

    //CONSTRUCTORES
    public ClientLoanDTO(){}

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id= clientLoan.getId();
        this.loanId= clientLoan.getLoan().getId();
        this.name= clientLoan.getLoan().getName();
        this.amount= clientLoan.getAmount();
        this.payments= clientLoan.getPayments();
    }

    //GETTERS AND SETTERS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
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

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    //METODOS
}
