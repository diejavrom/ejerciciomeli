package com.meli.status.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meli.status.api.response.StatusUsuarioResponse;
import com.meli.status.exception.ParamMandatoryException;
import com.meli.status.model.TotalChargeInfoTO;
import com.meli.status.model.TotalPaymentInfoTO;

@Service
public class StatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private PaymentService paymentService;

	public StatusUsuarioResponse getStatus(Integer user_id) {
		
		LOGGER.info("Consultando status del usuario {} ", user_id);

		if(user_id == null) {
			throw new ParamMandatoryException("user_id no puede ser null");
		}

		TotalChargeInfoTO totalCharge = chargeService.getTotalCharge(user_id);
		TotalPaymentInfoTO totalPayment = paymentService.getTotalPayment(user_id);
		return new StatusUsuarioResponse(user_id, totalCharge, totalPayment);
	}

}