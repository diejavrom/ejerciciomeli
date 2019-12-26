package com.meli.currency.exception;

public class CurrencyConfigurationNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2357968387144669930L;

	public CurrencyConfigurationNotFoundException(String msg) {
		super(msg);
	}

}
