package com.meli.charge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.charge.model.to.CurrencyConversionTO;

@Service
public class CurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

	private static final String URL_CURRENCY_SERVICE = "currency.api.url";

	@Autowired
	private Environment env;

	@Autowired
	private RestTemplate restTemplate;
	
	public Double convertToCurrencyDefault(String name, Double amount) {
		String urlCurrencyPayment = String.format("%s/convert/%s/%s", env.getProperty(URL_CURRENCY_SERVICE), name, amount);

		LOGGER.info("Invocando API {}", urlCurrencyPayment);

		ResponseEntity<CurrencyConversionTO> forEntity = restTemplate.getForEntity(urlCurrencyPayment, CurrencyConversionTO.class);
		CurrencyConversionTO totalDebt = forEntity.getBody();
		return totalDebt.getAmountTo();
	}

}
