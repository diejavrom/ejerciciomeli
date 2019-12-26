package com.meli.currency.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.meli.currency.DateHelper;
import com.meli.currency.exception.CurrencyConfigurationNotFoundException;
import com.meli.currency.exception.ParamMandatoryException;
import com.meli.currency.model.CurrencyConversion;
import com.meli.currency.repository.CurrencyConversionRepository;

@Service
public class CurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

	@Autowired
	private CurrencyConversionRepository currencyRepo;

	public Double convertToCurrencyDefault(String name, Double amount) {

		checkParams(name, amount);

		Timestamp now = DateHelper.getInstance().getNow();
		CurrencyConversion ccDefault = getCurrencyDefault(now);

		if(ccDefault.getName().equals(name)) {

			LOGGER.info("Param en currency default {}", (name + " " + amount));

			return amount;
		} else {
			List<CurrencyConversion> currencyConversionList = currencyRepo.findByName(name);
			CurrencyConversion ccCurrent = null;
			for(CurrencyConversion cc : currencyConversionList) {
				if(cc.isBetween(now)) {
					ccCurrent = cc;
					break;
				}
			}
			if(ccCurrent == null) {
				throw new CurrencyConfigurationNotFoundException(String.format("No se encontró una configuración vigente para currency '%s'", name));
			} else {
				Double result = ccCurrent.convert(amount);
				
				LOGGER.info("Convirtiendo {} -> {} ", (name + " " + amount), (result + " " + ccDefault.getName()));

				return result;
			}
		}
		
	}

	private CurrencyConversion getCurrencyDefault(Timestamp now) {
		List<CurrencyConversion> allDefaults = currencyRepo.findByDef(true);
		CurrencyConversion ccDefault = null;
		for(CurrencyConversion cc : allDefaults) {
			if(cc.isBetween(now)) {
				ccDefault = cc;
				break;
			}
		}
		if(ccDefault == null) {
			throw new CurrencyConfigurationNotFoundException("No se encontró una configuración default vigente");
		}
		return ccDefault;
	}

	private void checkParams(String name, Double amount) {
		if(name == null || name.trim().isEmpty()) {
			throw new ParamMandatoryException("name no puede ser null");
		}
		if(amount == null || amount < 0) {
			throw new ParamMandatoryException("amount no puede ser null ni negativo");
		}
	}

}