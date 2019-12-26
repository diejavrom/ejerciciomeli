package com.meli.currency.model;

import org.junit.Assert;
import org.junit.Test;

import com.meli.currency.DateHelper;
import com.meli.currency.exception.ParamMandatoryException;

public class CurrencyConversionTest {

	@Test(expected = ParamMandatoryException.class)
	public void testConvertWithNullParam() {
		final CurrencyConversion cc = new CurrencyConversion(DateHelper.getInstance().getNow(), null, 63d, "USD", "Dólar", false);
		cc.convert(null);
	}

	@Test
	public void testConvertWithNotNullParam() {
		Double factorConversion = 63d;
		final CurrencyConversion cc = new CurrencyConversion(DateHelper.getInstance().getNow(), null, factorConversion, "USD", "Dólar", false);
		Double amount = 100d;
		Double amountResult = amount * factorConversion;
		Assert.assertEquals(amountResult, cc.convert(amount));;
	}

}