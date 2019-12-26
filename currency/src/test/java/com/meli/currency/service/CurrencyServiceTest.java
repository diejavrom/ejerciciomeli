package com.meli.currency.service;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.meli.currency.DateHelper;
import com.meli.currency.exception.CurrencyConfigurationNotFoundException;
import com.meli.currency.exception.ParamMandatoryException;
import com.meli.currency.model.CurrencyConversion;
import com.meli.currency.repository.CurrencyConversionRepository;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = CurrencyService.class)
public class CurrencyServiceTest {

	@Autowired
	@InjectMocks
	private CurrencyService currencyService;

	@Mock
	private CurrencyConversionRepository currencyRepo;

	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

	@Test(expected=CurrencyConfigurationNotFoundException.class)
	public void testWithNonCurrencyConfigured() {
        String currency = "USD";
		when(currencyRepo.findByName(currency)).thenReturn(null);
		currencyService.convertToCurrencyDefault(currency, 20d);
	}

	@Test(expected=CurrencyConfigurationNotFoundException.class)
	public void testWithNonDefaultCurrencyConfigured() {
        String currency = "ARS";
		when(currencyRepo.findByDef(true)).thenReturn(null);
		currencyService.convertToCurrencyDefault(currency, 20d);
	}

    @Test(expected = ParamMandatoryException.class)
    public void testAmountParamMandatory() {
    	currencyService.convertToCurrencyDefault("USD", null);
    }

    @Test(expected = ParamMandatoryException.class)
    public void testCurrencyParamMandatory() {
    	currencyService.convertToCurrencyDefault(null, 20d);
    }

	@Test
	public void testConvertCurrency() {
		Double amount = 50d;
		Double factorConversion = 63d;
		String currency = "USD";
		final CurrencyConversion cc = new CurrencyConversion(DateHelper.getInstance().getNow(), null, factorConversion, currency, "Dólar", false);
		when(currencyRepo.findByName(currency)).thenReturn(Collections.singletonList(cc));
		
		Double result = currencyService.convertToCurrencyDefault(currency, amount);

		Assert.assertEquals(result, (Double)(amount*factorConversion));
		
	}

}