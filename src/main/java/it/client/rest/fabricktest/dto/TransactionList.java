package it.client.rest.fabricktest.dto;

import java.util.List;


/**
 * 
 * DTO for transaction list
 *  
 */
public class TransactionList {
	private List<Transaction> list;

	public List<Transaction> getList() {
		return list;
	}

	public void setList(List<Transaction> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "TransactionList [list=" + list + "]";
	}
	
	
}
