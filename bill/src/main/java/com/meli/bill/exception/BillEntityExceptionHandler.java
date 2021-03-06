package com.meli.bill.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.meli.bill.api.response.ErrorResponse;

@ControllerAdvice
@Component
public class BillEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillEntityExceptionHandler.class);
	
	@RequestMapping
	@ExceptionHandler({ParamMandatoryException.class})
	public ResponseEntity<ErrorResponse> handleParamMandatoryException(ParamMandatoryException ex) {
		HttpStatus badRequest = HttpStatus.BAD_REQUEST;
		LOGGER.error(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(badRequest.value(), ex.getMessage()), badRequest);
	}

	@RequestMapping
	@ExceptionHandler({ChargeAndPayAlreadyProccessedException.class})
	public ResponseEntity<ErrorResponse> handleChargeAndPayAlreadyProccessedException(ChargeAndPayAlreadyProccessedException ex) {
		HttpStatus badRequest = HttpStatus.BAD_REQUEST;
		LOGGER.error(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(badRequest.value(), ex.getMessage()), badRequest);
	}

	@RequestMapping
	@ExceptionHandler({Throwable.class})
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleUnknowException(Throwable ex) {
		HttpStatus error500 = HttpStatus.INTERNAL_SERVER_ERROR;
		LOGGER.error(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(error500.value(), ex.getMessage()), error500);
	}

}