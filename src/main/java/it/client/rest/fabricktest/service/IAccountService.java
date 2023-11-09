package it.client.rest.fabricktest.service;

import it.client.rest.fabricktest.dto.Balance;
import it.client.rest.fabricktest.dto.MoneyTransferRequest;
import it.client.rest.fabricktest.dto.MoneyTransferResponse;
import it.client.rest.fabricktest.dto.Response;

/**
 * 
 * interface for service account
 *  
 */
public interface IAccountService {
	
	Response<Balance> getBalanceById(String id);
	Response<MoneyTransferResponse> createMoneyTransfer(MoneyTransferRequest moneyTransferRequest, String accountId);
}
