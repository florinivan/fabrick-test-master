package it.client.rest.fabricktest.service;

import static java.lang.String.format;

import java.time.LocalDate;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import it.client.rest.fabricktest.dto.Balance;
import it.client.rest.fabricktest.dto.MoneyTransferRequest;
import it.client.rest.fabricktest.dto.MoneyTransferResponse;
import it.client.rest.fabricktest.dto.Response;
import it.client.rest.fabricktest.dto.TransactionList;

/**
 * 
 * Service account
 * 
 */
@Service
public class AccountServiceImpl implements IAccountService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${platfr.io.url}")
	private String BASE_URL;

	@Autowired
	private RestTemplate restTemplate;

	public Response<Balance> getBalanceById(String accountId) {

		String serviceUrl = BASE_URL + format("/api/gbs/banking/v4.0/accounts/%s/balance", accountId);

		HttpHeaders requestHeaders = getHeaderForFabrickTest();
		HttpEntity<Response<Balance>> entity = new HttpEntity<>(requestHeaders);
		
		ParameterizedTypeReference<Response<Balance>> responseType = new ParameterizedTypeReference<Response<Balance>>() {};

		ResponseEntity<Response<Balance>> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, entity,
				responseType);

		HttpStatus status = response.getStatusCode();
		if (status != HttpStatus.OK) {
			log.info("There was an error calling: {}", serviceUrl);
			log.info(response.getBody().toString());
		}
		return response.getBody();
	}

	public Response<MoneyTransferResponse> createMoneyTransfer(MoneyTransferRequest moneyTransferRequest,
                                                               String accountId) {

		String serviceUrl = BASE_URL + format("/api/gbs/banking/v4.0/accounts/%s/payments/money-transfers", accountId);

		HttpHeaders requestHeaders = getHeaderForFabrickTest();
		HttpEntity<MoneyTransferRequest> entity = new HttpEntity<>(moneyTransferRequest, requestHeaders);
		
		ParameterizedTypeReference<Response<MoneyTransferResponse>> responseType = new ParameterizedTypeReference<Response<MoneyTransferResponse>>() {};
		
		ResponseEntity<Response<MoneyTransferResponse>> response = restTemplate.exchange(serviceUrl,
				HttpMethod.POST, entity, responseType);

		HttpStatus status = response.getStatusCode();
		if (status != HttpStatus.OK) {
			log.info("There was an error calling: {}", serviceUrl);
			log.info(response.toString());
		}
		return response.getBody();
	}

	public Response<TransactionList> getTransactionByIdAndDate(String accountId, LocalDate fromAccountingDate,
                                                               LocalDate toAccountingDate) {

		String serviceUrl = BASE_URL + format("/api/gbs/banking/v4.0/accounts/%s/transactions", accountId);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrl)
				.queryParam("fromAccountingDate", fromAccountingDate.toString())
				.queryParam("toAccountingDate", toAccountingDate.toString());

		
		ParameterizedTypeReference<Response<TransactionList>> responseType = new ParameterizedTypeReference<Response<TransactionList>>() {};
		
		HttpHeaders requestHeaders = getHeaderForFabrickTest();
		HttpEntity<Response<TransactionList>> entity = new HttpEntity<>(requestHeaders);

		ResponseEntity<Response<TransactionList>> response = restTemplate.exchange(builder.toUriString(),
				HttpMethod.GET, entity, responseType);

		HttpStatus status = response.getStatusCode();
		if (status != HttpStatus.OK) {
			log.info("There was an error calling: {}", serviceUrl);
			log.info(response.getBody().toString());
		}
		return response.getBody();
	}
		
	
	private HttpHeaders getHeaderForFabrickTest() {
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		requestHeaders.set("Auth-Schema", "S2S");
		requestHeaders.set("apikey", "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP");
		return requestHeaders;
		
	}
}
