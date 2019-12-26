package com.meli.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.payment.model.to.TotalDebtTO;

@Service
public class ChargeService {

	private static final String URL_CHARGE_SERVICE = "charge.api.url";

	@Autowired
	private Environment env;

	public Double getTotalCharge(Integer userId) {
		String urlChargeAPI = env.getProperty(URL_CHARGE_SERVICE);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<TotalDebtTO> forEntity = restTemplate.getForEntity(urlChargeAPI + "/totalamountpending/" + userId, TotalDebtTO.class);
		TotalDebtTO totalDebt = forEntity.getBody();
		return totalDebt.getTotalPendingCharge();
	}
	
}
