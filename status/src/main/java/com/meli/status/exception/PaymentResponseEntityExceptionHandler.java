package com.meli.status.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.meli.status.api.response.ErrorResponse;

@ControllerAdvice
@Component
public class PaymentResponseEntityExceptionHandler {

	@RequestMapping
	@ExceptionHandler({ParamMandatoryException.class})
	public ResponseEntity<ErrorResponse> handleParamMandatoryException(ParamMandatoryException ex) {
		HttpStatus badRequest = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(badRequest.value(), ex.getMessage()), badRequest);
	}

	@RequestMapping
	@ExceptionHandler({Throwable.class})
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleUnknowException(Throwable ex) {
		HttpStatus error500 = HttpStatus.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(error500.value(), ex.getMessage()), error500);
	}

}