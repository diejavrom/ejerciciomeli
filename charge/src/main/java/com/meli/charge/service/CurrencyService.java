package com.meli.charge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.charge.model.to.CurrencyConversionTO;

@Service
public class CurrencyService {

	private static final String URL_CURRENCY_SERVICE = "currency.api.url";

	@Autowired
	private Environment env;

	public Double convertToCurrencyDefault(String name, Double amount) {
		String urlChargePayment = env.getProperty(URL_CURRENCY_SERVICE);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CurrencyConversionTO> forEntity = restTemplate.getForEntity(urlChargePayment + "/convert/" + name + "/" + amount, CurrencyConversionTO.class);
		CurrencyConversionTO totalDebt = forEntity.getBody();
		return totalDebt.getAmountTo();
	}
	
}
