package com.meli.payment.exception;

public class PaymentExceedsTotalDebtException extends RuntimeException {

	private static final long serialVersionUID = -6272183866143828085L;

	public PaymentExceedsTotalDebtException(String msg) {
		super(msg);
	}

}
