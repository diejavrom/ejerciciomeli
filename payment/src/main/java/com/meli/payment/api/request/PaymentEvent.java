package com.meli.payment.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa el evento de un pago realizado 
 */
public class PaymentEvent {

    @Schema(description = "indica el monto abonado con dos decimales", example = "150.10", required = true)	
	private Double amount;
    @Schema(description = "indica la moneda del monto", example = "ARS", required = true)	
	private String currency;
    @Schema(description = "indica el número de usuario", example = "1", required = true)	
	private Integer user_id;

	public PaymentEvent() {
	}

	public PaymentEvent(Double amount, String currency, Integer user_id) {
		super();
		this.amount = amount;
		this.currency = currency;
		this.user_id = user_id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

}
