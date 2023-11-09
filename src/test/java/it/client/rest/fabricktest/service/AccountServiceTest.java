package it.client.rest.fabricktest.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.client.rest.fabricktest.configuration.ConfigurationTestClass;
import it.client.rest.fabricktest.dto.Account;
import it.client.rest.fabricktest.dto.Balance;
import it.client.rest.fabricktest.dto.Creditor;
import it.client.rest.fabricktest.dto.MoneyTransferRequest;
import it.client.rest.fabricktest.dto.MoneyTransferResponse;
import it.client.rest.fabricktest.dto.Response;
import it.client.rest.fabricktest.dto.TransactionList;


/**
 * 
 * Test for accountService
 *  
 */
@SpringBootTest
@TestPropertySource("classpath:application.properties")
@ContextConfiguration(classes=ConfigurationTestClass.class, loader=AnnotationConfigContextLoader.class)
public class AccountServiceTest {

	@Autowired
	AccountServiceImpl accountService;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	 
	/**
     * Retrieves the balance of an account.
     * Should Return Invalid Account Identifier because the AccountId 123456 doesn't exist
     */
	@Test()
	public void shouldReturnInvalidAccountIdentifier() throws JsonMappingException, JsonProcessingException {
		log.info("shouldReturnInvalidAccountIdentifier =>");
		Response<Balance> errorResponse = null;
		
		try{
			Response<Balance> response =accountService.getBalanceById("123456");
		}catch (HttpClientErrorException e) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			TypeReference<Response<Balance>> typeRef  = new TypeReference<Response<Balance>>() {};
			errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), typeRef);
			log.debug(errorResponse.toString());
			
			assert errorResponse.getErrors().stream().anyMatch(s -> s.getCode().equals("REQ004"));
			log.info("shouldReturnInvalidAccountIdentifier <=");
		}
		
	}
	
	/**
     * Retrieves the balance of an account.
     * Should Return the Balance of the Account 14537780
     */
	@Test()
	public void shouldReturnBalance() {
		log.info("shouldReturnBalance =>");

		Response<Balance> response = accountService.getBalanceById("14537780");
		log.debug(response.toString());
		assert !response.getPayload().getBalance().isEmpty();
		log.info("shouldReturnBalance <=");
	}
	
	/**
     * Send a money Transfer Request
     * Should Return API000 Error
     */
	@Test()
	public void shouldGetAPI000Error() throws JsonMappingException, JsonProcessingException {
		log.info("shouldGetAPI000Error =>");
		Response<Balance> errorResponse = null;
		Account account = new Account("IT23A0336844430152923804660","SELBIT2BXXX");
		Creditor creditor = new Creditor("John Doe",account);
		MoneyTransferRequest moneyTransfReq = new MoneyTransferRequest(creditor,"Payment invoice 75/2017","800",LocalDate.now(),"EUR");
		try {
			Response<MoneyTransferResponse> response = accountService.createMoneyTransfer(moneyTransfReq, "14537780");
		}catch (HttpClientErrorException e) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			TypeReference<Response<Balance>> typeRef  = new TypeReference<Response<Balance>>() {};
			errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), typeRef);
			log.debug(errorResponse.toString());
			
			assert errorResponse.getErrors().stream().anyMatch(s -> s.getCode().equals("API000"));
			log.info("shouldGetAPI000Error <=");
		}
	
	}
	
	/**
     * Retrieves the transactions of an Account
     * Should Return the transactions of the Account 14537780 from 2019-01-01 to 2019-12-01
     */
	@Test()
	public void shouldReturnTransactions() {
		log.info("shouldReturnTransactions =>");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
        String fromAccountingStringDate = "2019-01-01";       
        String toAccountingStringDate="2019-12-01";
        
        LocalDate fromAccountingDate = LocalDate.parse(fromAccountingStringDate, formatter);
        LocalDate toAccountingDate = LocalDate.parse(toAccountingStringDate, formatter);
        
		Response<TransactionList> response = accountService.getTransactionByIdAndDate("14537780",fromAccountingDate,toAccountingDate);
		log.debug(response.toString());
		assert response.getPayload().getList().stream().anyMatch(s -> s.getOperationId().equals("00000000282831"));

		log.info("shouldReturnTransactions <=");
	}
	
}
