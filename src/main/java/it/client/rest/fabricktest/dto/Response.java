package it.client.rest.fabricktest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * DTO for reponse
 *  
 */
public class Response<T> {
	
	private String status; 
	private List<Error> errors;
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private T payload;
	
	public Response() {
		
	}
	
	public Response(String status, List<Error> errors, T payload) {
		super();
		this.status = status;
		this.errors = errors;
		this.payload = payload;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Error> getErrors() {
		return errors;
	}
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public T getPayload() {
		return payload;
	}
	public void setPayload(T payload) {
		this.payload = payload;
	}
	
	@Override
	public String toString() {
		return "Response [status=" + status + ", errors=" + errors + ", payload=" + payload + "]";
	}
	
}
