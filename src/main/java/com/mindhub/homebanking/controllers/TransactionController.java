package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PDFExportDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.dtos.TransactionFilterDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransactionController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> transfer(
            Authentication authentication,
            @RequestParam double amount, @RequestParam String description,
            @RequestParam String fromNumber, @RequestParam String toNumber){

        Account account = accountRepository.findByNumber(toNumber);
        Client clientGeneral = clientRepository.findByEmail(authentication.getName());
        Account accountFrom = accountRepository.findByNumber(fromNumber);


        //si la cuenta receptora existe
        if (account == null){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }

        // que los parámetros no estén vacíos
        if (amount <= 0) {
            return  new ResponseEntity<>("amount mal", HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty() || fromNumber.isEmpty() || toNumber.isEmpty()) {
            return new ResponseEntity<>("desc vacia", HttpStatus.FORBIDDEN);
        }

        // que los números de cuenta no sean iguales
        if (fromNumber.equals(toNumber)){
            return  new ResponseEntity<>("No se puede transferir a la misma cuenta", HttpStatus.FORBIDDEN);
        }
        // que exista la cuenta de origen
        if (accountRepository.findByNumber(fromNumber) == null) {
            return  new ResponseEntity<>("Cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }
        // que la cuenta de origen pertenezca al cliente autenticado
        if (!clientGeneral.getAccounts().contains(accountFrom)) {
            return new ResponseEntity<>("que la cuenta de origen pertenezca al cliente autenticado", HttpStatus.FORBIDDEN);
        }
        // que exista la cuenta destino
        if (accountRepository.findByNumber(toNumber) == null){
            return new ResponseEntity<>("que exista la cuenta destino", HttpStatus.FORBIDDEN);
        }
        //que la cuenta de origen tenga el monto disponible
        if(accountFrom.getBalance() < amount) {
            return new ResponseEntity<>("La cuenta tiene un monto menor disponible", HttpStatus.FORBIDDEN);
        }

        Transaction transaction= new Transaction(TransactionType.DEBITO, -amount, "Transferencia Realizada De- "+ account.getNumber() + " " + description, LocalDateTime.now(), accountFrom);
        //accountFrom.addTransaction(transaction);
        transactionRepository.save(transaction);
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountRepository.save(accountFrom);

        Transaction transaction1= new Transaction(TransactionType.CREDITO, amount, "Transferencia Recibida De- "+ accountFrom.getNumber() + " " +description, LocalDateTime.now(), account);
        //account.addTransaction(transaction1);
        transactionRepository.save(transaction1);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        return new ResponseEntity<>("Se realizó la transferencia de manera exitosa", HttpStatus.ACCEPTED);
    }

    @PostMapping ("/api/transactionFilter")
    public ResponseEntity <?> ListTransactionDTO(Authentication authentication, @RequestBody TransactionFilterDTO transactionFilterDTO){
        transactionFilterDTO.setUntilDate(transactionFilterDTO.getUntilDate().plusDays(1));

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(transactionFilterDTO.getAccountNumber());

        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Datos incorrectos", HttpStatus.FORBIDDEN);
        }

        List<LocalDate> listOfDates = transactionFilterDTO.getFromDate().datesUntil(transactionFilterDTO.getUntilDate()).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList1 = transactionDTOList.stream().filter(e -> listOfDates.contains(e.getDate().toLocalDate())).collect(Collectors.toList());
        transactionDTOList1.sort(Comparator.comparing(TransactionDTO::getDate).reversed());

        return new ResponseEntity<>(transactionDTOList1, HttpStatus.ACCEPTED);
    }

    @PostMapping ("/api/transaction/export/pdf")
    public void exportToPDF(HttpServletResponse response, Authentication authentication, @RequestBody TransactionFilterDTO transactionFilterDTO) throws DocumentException, IOException {

        transactionFilterDTO.setUntilDate(transactionFilterDTO.getUntilDate().plusDays(1));

        Account account = accountRepository.findByNumber(transactionFilterDTO.getAccountNumber());

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=file.pdf";
        response.setHeader(headerKey, headerValue);

        List<LocalDate> listOfDates = transactionFilterDTO.getFromDate().datesUntil(transactionFilterDTO.getUntilDate()).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        List<TransactionDTO> transactionDTOList1 = transactionDTOList.stream().filter(e -> listOfDates.contains(e.getDate().toLocalDate())).collect(Collectors.toList());
        transactionDTOList1.sort(Comparator.comparing(TransactionDTO::getDate).reversed());
        PDFExportDTO exporter = new PDFExportDTO(transactionDTOList1);
        exporter.export(response);

    }

}
