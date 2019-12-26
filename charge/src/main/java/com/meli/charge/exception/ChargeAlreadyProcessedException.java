package com.meli.charge.exception;

public class ChargeAlreadyProcessedException extends RuntimeException {

	private static final long serialVersionUID = -6272183866143828085L;

	public ChargeAlreadyProcessedException(String msg) {
		super(msg);
	}

}
