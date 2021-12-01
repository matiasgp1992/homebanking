package com.mindhub.homebanking.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionFilterDTO {
    //ATRIBUTOS
    private LocalDate fromDate;
    private LocalDate untilDate;
    private String accountNumber;

    //CONSTRUCTORES
    public TransactionFilterDTO() {
    }

    public TransactionFilterDTO(LocalDate fromDate, LocalDate untilDate, String accountNumber) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.accountNumber = accountNumber;
    }

    //GETTERS AND SETTERS

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(LocalDate untilDate) {
        this.untilDate = untilDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
