package com.meli.currency.api.response;

public class CurrencyConversionResponse {

	private String from;
	private Double amountTo;
	private Double amountFrom;
	
	public CurrencyConversionResponse() {
	}
	
	public CurrencyConversionResponse(String from, Double amountFrom, Double amountTo) {
		super();
		this.from = from;
		this.amountTo = amountTo;
		this.amountFrom = amountFrom;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Double getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(Double amountTo) {
		this.amountTo = amountTo;
	}

	public Double getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(Double amountFrom) {
		this.amountFrom = amountFrom;
	}

}