package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.utils.Utils;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/clients/current/cards")
    private ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColor color){
        Client clientGeneral = clientRepository.findByEmail(authentication.getName());
        if(clientGeneral.getCards().stream().filter(tipo -> tipo.getType().toString().equals(type.toString())).count() > 2) {
            return new ResponseEntity<>("403 Ya tiene 3 tarjetas", HttpStatus.FORBIDDEN);
        }
        Card tarjeta= new Card(clientGeneral, type, color, Utils.getCardNumber(1000, 9999), Utils.getCVV(100, 999), LocalDateTime.now(), LocalDateTime.now().plusYears(5));
        cardRepository.save(tarjeta);
        return new ResponseEntity<>("201 Tarjeta Creada", HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/cards/delete/{id}")
    public ResponseEntity<?> deleteCard(Authentication authentication, @PathVariable long id){
        Client client = clientRepository.findByEmail(authentication.getName());
        Card card = cardRepository.findById(id);

        if(card == null){
            return new ResponseEntity<>("Tarjeta inexistente", HttpStatus.FORBIDDEN);
        }
        if (!client.getCards().contains(card)){
            return new ResponseEntity<>("Tarjeta no pertenece al cliente", HttpStatus.FORBIDDEN);
        }

        cardRepository.delete(card);
        return new ResponseEntity<>("Tarjeta eliminada correctamente", HttpStatus.ACCEPTED);
    }
}
