package com.meli.charge.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.charge.model.to.PaymentTO;

/**
 * 
 * Servicio para comunicarse con la aplicación payment.
 *
 */
@Service
public class PaymentService {

	private static final String URL_PAYMENT_SERVICE = "payment.api.url";
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private Environment env;

	@Autowired
	private RestTemplate restTemplate;
    
	/**
	 * Obtiene los cargos con deuda invocando a la api de la aplicación charge
	 * @param userId
	 * @return los cargos con deuda
	 */
	public List<PaymentTO> getPaymentsWithAvailableAmount(Integer userId) {
		String urlChargePendingAPI = String.format("%s/listavailable/%d", env.getProperty(URL_PAYMENT_SERVICE), userId);

		LOGGER.info("Invocando API {} ", urlChargePendingAPI);

		ResponseEntity<PaymentTO[]> forEntity = restTemplate.getForEntity(urlChargePendingAPI, PaymentTO[].class);
		return Arrays.asList(forEntity.getBody());
	}

}
