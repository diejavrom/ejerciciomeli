package com.meli.bill.exception;

public class ParamMandatoryException extends RuntimeException {

	private static final long serialVersionUID = -8733891519772085119L;

	public ParamMandatoryException(String msg) {
		super(msg);
	}

}
