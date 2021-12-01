package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClient() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());

        //findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList()); //ESTA ES LA FORMA LARGA DE ESCRIBIR LO ANTERIOR
    }

    /*@RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }*/
    @GetMapping("/api/clients/current")
    public ClientDTO getClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/api/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password,
            @RequestParam AccountType type) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client clientGeneral= new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(clientGeneral);
        Account cuenta= new Account("VIN-" +(int) (Math.random()*(99999999 - 00000001)+00000001) +"-"+clientGeneral.getId(), LocalDateTime.now(), 0.00, type);
        clientGeneral.addAccount(cuenta);
        accountRepository.save(cuenta);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    
}
