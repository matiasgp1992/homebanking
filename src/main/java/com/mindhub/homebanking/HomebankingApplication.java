package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository,
									  AccountRepository repository1,
									  TransactionRepository repository2,
									  LoanRepository repository3,
									  ClientLoanRepository repository4,
									  CardRepository repository5){
		return args -> {
			//CREO CLIENTE
			Client cliente1= new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));
			Client cliente2= new Client("Rodrigo", "Paris", "rodri@paris.com", passwordEncoder.encode("rodrigo123"));
			Client cliente3= new Client("Dario", "Valsesia","dario@valsesia.com", passwordEncoder.encode("dario123"));
			//ADMIN
			Client admin = new Client("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("admin123"));

			//REPO CLIENT
			repository.save(cliente1);
			repository.save(cliente2);
			repository.save(cliente3);
			repository.save(admin);

			//CREO CUENTA
			Account cuentaMelba= new Account("VIN001", LocalDateTime.now(),5000, AccountType.AHORRO);
			Account cuentaMelba2= new Account("VIN002", LocalDateTime.now().plusDays(1),7500, AccountType.CTACTE);
			Account cuentaRodrigo= new Account("VIN003", LocalDateTime.now(), 32000, AccountType.AHORRO);
			Account cuentaDario= new Account("VIN014", LocalDateTime.now().plusDays(2), 4500, AccountType.AHORRO);

			//ASIGNO CUENTA AL CLIENTE
			cliente1.addAccount(cuentaMelba);
			cliente1.addAccount(cuentaMelba2);
			cliente2.addAccount(cuentaRodrigo);
			cliente3.addAccount(cuentaDario);

			//AGREGO AL REPOSITORIO

			repository1.save(cuentaMelba);
			repository1.save(cuentaMelba2);
			repository1.save(cuentaRodrigo);
			repository1.save(cuentaDario);


			//CREO TRANSACCIONES
			Transaction transaccion1= new Transaction(TransactionType.CREDITO, 2500, "De M.G.P", LocalDateTime.now(), cuentaMelba);
			repository2.save(transaccion1);
			cuentaMelba.setBalance(cuentaMelba.getBalance()+ transaccion1.getAmount());
			repository1.save(cuentaMelba);
			Transaction transaccion2= new Transaction(TransactionType.DEBITO, -1000, "McDonalds", LocalDateTime.now(), cuentaMelba);
			repository2.save(transaccion2);
			cuentaMelba.setBalance(cuentaMelba.getBalance()+ transaccion2.getAmount());
			repository1.save(cuentaMelba);
			Transaction transaccion3= new Transaction(TransactionType.CREDITO, 2000, "Dario transf", LocalDateTime.now(), cuentaMelba2);
			repository2.save(transaccion3);
			cuentaMelba2.setBalance(transaccion3.getBalance());
			repository1.save(cuentaMelba2);
			Transaction transaccion4= new Transaction(TransactionType.DEBITO, -500, "Floreria", LocalDateTime.now(), cuentaMelba2);
			repository2.save(transaccion4);
			cuentaMelba2.setBalance(transaccion4.getBalance());
			repository1.save(cuentaMelba2);
			Transaction transaction5= new Transaction(TransactionType.CREDITO, 2500, "transf Mati", LocalDateTime.now(), cuentaRodrigo);
			repository2.save(transaction5);
			cuentaRodrigo.setBalance(transaction5.getBalance());
			repository1.save(cuentaRodrigo);

			//ASIGNO TRANSACCIONES
			//cuentaMelba.addTransaction(transaccion1);
			//cuentaMelba.addTransaction(transaccion2);
			//cuentaMelba2.addTransaction(transaccion3);
			//cuentaMelba2.addTransaction(transaccion4);
			//cuentaRodrigo.addTransaction(transaction5);

			//CREO LOAN
			var cuotas = List.of(12, 24, 36, 48, 60);
			var cuotas1 = List.of(6, 12, 24);
			var cuotas2 = List.of(6, 12, 24, 36);
			Loan hipotecario= new Loan("Hipotecario", 500000, cuotas,50);
			Loan personal= new Loan("Personal", 100000, cuotas1,35);
			Loan automotriz= new Loan("Automotriz", 300000, cuotas2,25);

			//CREO PRESTAMOS
			ClientLoan prestamoMelba= new ClientLoan(400000, 60, cliente1, hipotecario);
			ClientLoan prestamoMelba2= new ClientLoan(50000, 12, cliente1, personal);
			ClientLoan prestamoDario= new ClientLoan(100000, 24, cliente3, personal);
			ClientLoan prestamoDario2= new ClientLoan(200000, 36, cliente3, automotriz);

			//ASIGNO PRESTAMO al Cliente //USE EL ADDCLIENTLOAN QUE TENGO DEFINIDO EN CLIENT
			cliente1.addClientLoan(prestamoMelba);
			cliente1.addClientLoan(prestamoMelba2);
			cliente3.addClientLoan(prestamoDario);
			cliente3.addClientLoan(prestamoDario2);

			//ASIGNO CLIENTE AL prestamo //USE EL ADDCLIENTLOAN QUE TENGO DEFINIDO EN LOAN
			hipotecario.addClientLoan(prestamoMelba);
			personal.addClientLoan(prestamoMelba2);
			personal.addClientLoan(prestamoDario);
			automotriz.addClientLoan(prestamoDario2);

			//CREO TARJETAS
			Card gold= new Card(cliente1, CardType.DEBIT, CardColor.GOLD, "4562106389793697", 485, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card titanium= new Card(cliente1, CardType.CREDIT, CardColor.TITANIUM, "4562116387719631", 879, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card silver= new Card(cliente2, CardType.CREDIT, CardColor.SILVER, "4562116877206479", 145, LocalDateTime.now(), LocalDateTime.now().plusYears(5));





			//AGREGO AL REPOSITORIO LOAN
			repository3.save(hipotecario);
			repository3.save(personal);
			repository3.save(automotriz);

			//AGREGO AL REPO...CREO UN CLIENTLOAN REPOSITORY
			repository4.save(prestamoMelba);
			repository4.save(prestamoMelba2);
			repository4.save(prestamoDario);
			repository4.save(prestamoDario2);

			//AGREGO AL REPO LAS TARJETAS
			repository5.save(gold);
			repository5.save(titanium);
			repository5.save(silver);


		};
	}



}
