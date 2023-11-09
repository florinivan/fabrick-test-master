package it.client.rest.fabricktest.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.client.rest.fabricktest.dto.Account;
import it.client.rest.fabricktest.dto.Balance;
import it.client.rest.fabricktest.dto.Creditor;
import it.client.rest.fabricktest.dto.MoneyTransferRequest;
import it.client.rest.fabricktest.dto.MoneyTransferResponse;
import it.client.rest.fabricktest.dto.Response;
import it.client.rest.fabricktest.dto.TransactionList;
import it.client.rest.fabricktest.service.AccountServiceImpl;

/**
 * 
 * Controller for account
 *  
 */
@RestController
public class AccountController {

	@Autowired
	AccountServiceImpl accountService;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/accounts/{accountId}/balance")
    Response<Balance> getBalanceById(@PathVariable String accountId){
		log.info("");
		log.info("AccountController - getBalanceById accountId: "+ accountId);
		Response<Balance> response = accountService.getBalanceById(accountId);
		return response;
		
	}
	
	
	@GetMapping("/accounts/{accountId}/payments/money-transfers")
    Response<MoneyTransferResponse> createMoneyTransfer(@PathVariable String accountId){
		log.info("");
		log.info("AccountController - createMoneyTransfer accountId: "+ accountId);
		Account account = new Account("IT23A0336844430152923804660","SELBIT2BXXX");
		Creditor creditor = new Creditor("John Doe",account);
		MoneyTransferRequest moneyTransfReq = new MoneyTransferRequest(creditor,"Payment invoice 75/2017","800",LocalDate.now(),"EUR");
		Response<MoneyTransferResponse> response = accountService.createMoneyTransfer(moneyTransfReq, accountId);
		return response;
		
	}
	
	@PostMapping("/accounts/{accountId}/payments/money-transfers")
    Response<MoneyTransferResponse> createMoneyTransfer(@PathVariable String accountId, @RequestBody MoneyTransferRequest moneyTransfReq){
		log.info("");
		log.info("AccountController - createMoneyTransfer accountId: "+ accountId + " ,MoneyTransferRequest: "+ moneyTransfReq);
		
		Response<MoneyTransferResponse> response = accountService.createMoneyTransfer(moneyTransfReq, accountId);
		return response;	
	}
	
	@GetMapping("/accounts/{accountId}/transactions")
    Response<TransactionList> getTransactionByIdAndDate(
			@PathVariable String accountId,
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fromAccountingDate,
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate toAccountingDate){
		
		log.info("");
		log.info("AccountController - getTransactionByIdAndDate accountId: "+ accountId + " ,fromAccountingDate: " + fromAccountingDate + " ,toAccountingDate: "+ toAccountingDate);
		Response<TransactionList> response = accountService.getTransactionByIdAndDate(accountId,fromAccountingDate,toAccountingDate);
		return response;
		
	}
	
	
}
