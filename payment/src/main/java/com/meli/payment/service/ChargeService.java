package com.meli.payment.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.payment.model.to.ChargeTO;
import com.meli.payment.model.to.TotalDebtTO;

@Service
public class ChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeService.class);

	private static final String URL_CHARGE_SERVICE = "charge.api.url";

	@Autowired
	private Environment env;

	@Autowired
	private RestTemplate restTemplate;

	public Double getTotalCharge(Integer userId) {
		String urlChargeAPI = String.format("%s/totalamountpending/%d", env.getProperty(URL_CHARGE_SERVICE), userId);
	
		LOGGER.info("Invocando API {} ", urlChargeAPI);

		ResponseEntity<TotalDebtTO> forEntity = restTemplate.getForEntity(urlChargeAPI, TotalDebtTO.class);
		TotalDebtTO totalDebt = forEntity.getBody();
		return totalDebt.getTotalPendingCharge();
	}

	public List<ChargeTO> getPendingCharges(Integer userId) {
		String urlChargePendingAPI = String.format("%s/listpending/%d", env.getProperty(URL_CHARGE_SERVICE), userId);

		LOGGER.info("Invocando API {} ", urlChargePendingAPI);

		ResponseEntity<ChargeTO[]> forEntity = restTemplate.getForEntity(urlChargePendingAPI, ChargeTO[].class);
		List<ChargeTO> charges = Arrays.asList(forEntity.getBody());
		return charges;
	}

}