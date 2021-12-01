package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PaymentController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;

    @Transactional
    @PostMapping("/api/payment")
    public ResponseEntity<?> payment(Authentication authentication, @RequestBody PaymentDTO paymentDTO){
        if(paymentDTO.getNumber().isEmpty() ||
                paymentDTO.getCvv() == 0 || paymentDTO.getDescription().isEmpty() || paymentDTO.getAmount() < 1){
            return new ResponseEntity<>("Datos incorrectos", HttpStatus.FORBIDDEN);
        }

        Card card = cardRepository.findByNumber(paymentDTO.getNumber());
        if(card == null){
            return new ResponseEntity<>("No se encontró la tarjeta", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());
        if(client == null){
            return new ResponseEntity<>("No se encontró el cliente", HttpStatus.FORBIDDEN);
        }

        if(!card.getThruDate().isAfter(LocalDateTime.now())) {
            return new ResponseEntity<>("Tarjeta vencida", HttpStatus.FORBIDDEN);
        }
        if(client.getCards().stream().filter(e -> e.getNumber().equals(paymentDTO.getNumber())).collect(Collectors.toSet()).size()==0){
            return new ResponseEntity<>("La tarjeta no pertenece al cliente", HttpStatus.FORBIDDEN);
        }
        if(card.getCvv() != paymentDTO.getCvv()){
            return new ResponseEntity<>("CVV Incorrecto",HttpStatus.FORBIDDEN);
        }

        List<Account> accounts = new ArrayList<>(client.getAccounts());
        Account account = accounts.stream().filter(a -> a.getBalance() >= paymentDTO.getAmount()).findFirst().get();
        if(account.getNumber() == null){
            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
        }
        Transaction transaction = new Transaction(TransactionType.DEBITO, -paymentDTO.getAmount(), "Pago -"+paymentDTO.getDescription(), LocalDateTime.now(), account);
        transactionRepository.save(transaction);
        account.setBalance(account.getBalance() - paymentDTO.getAmount());
        accountRepository.save(account);

        return new ResponseEntity<>("Pago procesado correctamente", HttpStatus.ACCEPTED);
     }
}
