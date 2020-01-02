package com.meli.currency.api.response;

/**
 * Representa el resultado de una conversión
 */
public class CurrencyConversionResponse {

	private String from; //currency origen
	private Double amountFrom; //monto convertido
	private Double amountTo; //monto convertido a la moneda default

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
