package com.meli.currency;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.meli.currency.model.CurrencyConversion;
import com.meli.currency.repository.CurrencyConversionRepository;

@Component
public class CurrencyConversionLoader implements ApplicationRunner {

	@Autowired
	private CurrencyConversionRepository currencyConversionRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//currency conversion
		Timestamp now = DateHelper.getInstance().getNow();

		//USD
		CurrencyConversion currency = new CurrencyConversion();
		currency.setDescription("Dólar");
		currency.setName("USD");
		currency.setDef(false);
		currency.setConversionFactor(63d);
		currency.setSince(now);

		currencyConversionRepo.save(currency);

		//ARS
		currency = new CurrencyConversion();
		currency.setDescription("Peso Argentino");
		currency.setName("ARS");
		currency.setDef(true);
		currency.setConversionFactor(1d);
		currency.setSince(now);

		currencyConversionRepo.save(currency);

	}

}