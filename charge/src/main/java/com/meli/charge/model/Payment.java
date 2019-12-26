package com.meli.charge.model;

import java.io.Serializable;

public class Payment implements Serializable {

	private static final long serialVersionUID = -8929567302784045440L;

	private String id;
	private Double amount;
	private String currency;
	private Integer userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}