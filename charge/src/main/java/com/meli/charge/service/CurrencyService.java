package com.meli.charge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.charge.model.to.CurrencyConversionTO;

/**
 * Servicio para comunicarse con la aplicación currency 
 */
@Service
public class CurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

	private static final String URL_CURRENCY_SERVICE = "currency.api.url";

	@Autowired
	private Environment env;

	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Convierte el monto <code>amount</code> en la moneda <code>currency</code> a la moneda default invocando a la api de la aplicación currency.
	 * @param currency
	 * @param amount
	 * return <code>amount/<code> convertido a la moneda default 
	 */
	public Double convertToCurrencyDefault(String currency, Double amount) {
		String urlCurrencyPayment = String.format("%s/convert/%s/%s", env.getProperty(URL_CURRENCY_SERVICE), currency, amount);

		LOGGER.info("Invocando API {}", urlCurrencyPayment);

		ResponseEntity<CurrencyConversionTO> forEntity = restTemplate.getForEntity(urlCurrencyPayment, CurrencyConversionTO.class);
		CurrencyConversionTO ccTO = forEntity.getBody();
		return ccTO.getAmountTo();
	}

}
