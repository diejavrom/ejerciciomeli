package com.meli.status.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.status.model.TotalPaymentInfoTO;

@Service
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
	
	private static final String URL_PAYMENT_SERVICE = "payment.api.url";

	@Autowired
	private Environment env;

	public TotalPaymentInfoTO getTotalPayment(Integer userId) {
		String urlPaymentAPI = env.getProperty(URL_PAYMENT_SERVICE) + "/total/" + userId;
		LOGGER.info("Invocando API {}", urlPaymentAPI);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<TotalPaymentInfoTO> forEntity = restTemplate.getForEntity(urlPaymentAPI, TotalPaymentInfoTO.class);
		return forEntity.getBody();
	}

}