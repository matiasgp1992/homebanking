package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.CrearLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/api/loans")
    public List<LoanDTO> getLoan(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> loans(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){

        Client client= clientRepository.findByEmail(authentication.getName()); //autentico el cliente que pide el prestamo
        Account account= accountRepository.findByNumber(loanApplicationDTO.getAccount()); //cuenta destino del prestamo
        Loan loan= loanRepository.findByName(loanApplicationDTO.getName()); //nombre del prestamo

        if (loanApplicationDTO.getAccount().isEmpty() || loanApplicationDTO.getName().isEmpty() || loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayment() == 0){
            return new ResponseEntity<>("Uno de los datos está vacío", HttpStatus.FORBIDDEN);
        }
        if (loan == null){
            return new ResponseEntity<>("No existe el Préstamo", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("Monto solicitado super el permitido", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayment())){
            return new ResponseEntity<>("Cantidad de cuotas incorrectas", HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("Cuenta destino incorrecta", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Cuenta destino no pertenece al cliente", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan= new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayment(), client, loan);
        clientLoanRepository.save(clientLoan);
        Transaction transaction= new Transaction(TransactionType.CREDITO, loanApplicationDTO.getAmount(), loan.getName()+ " loan approved", LocalDateTime.now(), account);
        transactionRepository.save(transaction);
        //account.addTransaction(transaction);
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        accountRepository.save(account);
        return new ResponseEntity<>("Account balance updated", HttpStatus.CREATED);
    }

    @PostMapping("/api/admin/loan")
    public ResponseEntity<?> createLoan(Authentication authentication, @RequestBody CrearLoanDTO crearLoanDTO){
        Client client = clientRepository.findByEmail(authentication.getName());

        if (!client.getEmail().contains("@admin.com")){
            return new ResponseEntity<>("Not authorized", HttpStatus.FORBIDDEN);
        }
        if (crearLoanDTO.getInterest() == 0 || crearLoanDTO.getMaxAmount() == 0 || crearLoanDTO.getName().isEmpty() || crearLoanDTO.getPayments().isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        loanRepository.save(new Loan(crearLoanDTO.getName(), crearLoanDTO.getMaxAmount(), crearLoanDTO.getPayments(),crearLoanDTO.getInterest()));
        return new ResponseEntity<>("New Loan Added", HttpStatus.CREATED);
    }
}
