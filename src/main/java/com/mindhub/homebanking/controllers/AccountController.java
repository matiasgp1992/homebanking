package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utils.Utils;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/accounts")
    public Set<AccountDTO> getTransaction(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getTransaction(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> creator(Authentication authentication, @RequestParam AccountType type) {
        Client clientGeneral = clientRepository.findByEmail(authentication.getName());
        if (clientGeneral.getAccounts().size() > 2) {
            return new ResponseEntity<>("Already have 3 accounts", HttpStatus.FORBIDDEN);
        };
        Account cuenta= new Account(Utils.accNumber() +"-"+clientGeneral.getId(), LocalDateTime.now(), 0.00, type);
        clientGeneral.addAccount(cuenta);
        accountRepository.save(cuenta);

        return new ResponseEntity<>("201 Creada", HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/accounts/{number}")
    public ResponseEntity<?> deleteAccount(Authentication authentication, @PathVariable String number){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(number);
        Set<Transaction> transactions= account.getTransactions();
        if (!client.getAccount().contains(account)){
            return new ResponseEntity<>("La cuenta no pertence al cliente", HttpStatus.FORBIDDEN);
        }

        if(transactions != null){
            transactionRepository.deleteAll(transactions);
        }

        accountRepository.delete(account);

        return new ResponseEntity<>("Cuenta y transacciones eliminadas", HttpStatus.ACCEPTED);
    }

}
