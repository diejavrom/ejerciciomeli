package com.meli.currency.api;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.meli.currency.api.response.CurrencyConversionResponse;
import com.meli.currency.service.CurrencyService;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyRestController {

	@Autowired
	private CurrencyService currencyService;

    @RequestMapping(value = "/convert/{name}/{amount}", method = RequestMethod.GET)
    public ResponseEntity<CurrencyConversionResponse> convert(@Valid @PathVariable String name, @Valid @PathVariable Double amount) {
    	return new ResponseEntity<CurrencyConversionResponse>(new CurrencyConversionResponse(name, amount, currencyService.convertToCurrencyDefault(name, amount)), HttpStatus.OK);
    }

}