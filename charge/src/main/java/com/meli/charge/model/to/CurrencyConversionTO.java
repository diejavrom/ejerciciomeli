package com.meli.charge.model.to;

/**
 * 
 * TO que contiene el resultado de una invocación del servicio de conversión 
 * de la aplicación currency
 */
public class CurrencyConversionTO {

	private String from; //currency original
	private Double amountFrom; //monto original
	private Double amountTo; //monto en currency default
	
	public CurrencyConversionTO() {
	}
	
	public CurrencyConversionTO(String from, Double amountFrom, Double amountTo) {
		super();
		this.from = from;
		this.amountFrom = amountFrom;
		this.amountTo = amountTo;
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
