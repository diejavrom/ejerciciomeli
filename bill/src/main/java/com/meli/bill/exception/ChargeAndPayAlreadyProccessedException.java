package com.meli.bill.exception;

public class ChargeAndPayAlreadyProccessedException extends RuntimeException {

	private static final long serialVersionUID = -8733891519772085119L;

	public ChargeAndPayAlreadyProccessedException(String msg) {
		super(msg);
	}

}
