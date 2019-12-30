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
import com.meli.currency.exception.response.ErrorResponse;
import com.meli.currency.service.CurrencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyRestController {

	@Autowired
	private CurrencyService currencyService;

    @Operation(
    		summary = "Permite convertir un importe en una cierta moneda (currency) a la moneda default que maneja el sistema.",
    		responses = {
    			@ApiResponse(description = "El importe convertido a la moneda default definida en el sistema", responseCode = "200"),
    			@ApiResponse(responseCode = "400", description = "ante un parámetro inválido",
    			content = @Content (
    					schema = @Schema(implementation = ErrorResponse.class),
    					mediaType = "application/json"
    				)),
    			@ApiResponse(responseCode = "500", description = "ante un error inesperado",
    			content = @Content (
    					schema = @Schema(implementation = ErrorResponse.class),
    					mediaType = "application/json"
    				))
    		}
    )
    @RequestMapping(value = "/convert/{currency}/{amount}", method = RequestMethod.GET)
    public ResponseEntity<CurrencyConversionResponse> convert(@Valid @PathVariable String currency, @Valid @PathVariable Double amount) {
    	return new ResponseEntity<CurrencyConversionResponse>(new CurrencyConversionResponse(currency, amount, currencyService.convertToCurrencyDefault(currency, amount)), HttpStatus.OK);
    }

}