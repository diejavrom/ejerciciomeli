package com.meli.status.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.status.model.TotalChargeInfoTO;

@Service
public class ChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeService.class);
	
	private static final String URL_CHARGE_SERVICE = "charge.api.url";

	@Autowired
	private Environment env;

	public TotalChargeInfoTO getTotalCharge(Integer userId) {
		String urlChargeAPI = env.getProperty(URL_CHARGE_SERVICE) + "/total/" + userId;

		LOGGER.info("Invocando API {} ", urlChargeAPI);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<TotalChargeInfoTO> forEntity = restTemplate.getForEntity(urlChargeAPI, TotalChargeInfoTO.class);
		return forEntity.getBody();
	}

}
