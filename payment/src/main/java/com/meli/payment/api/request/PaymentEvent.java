package com.meli.payment.api.request;

public class PaymentEvent {

	private Double amount;
	private String currency;
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
