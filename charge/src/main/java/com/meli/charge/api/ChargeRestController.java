package com.meli.charge.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meli.charge.api.request.ChargeEvent;
import com.meli.charge.api.response.ErrorResponse;
import com.meli.charge.api.response.TotalAmountPendingChargeResponse;
import com.meli.charge.api.response.TotalChargeInfoResponse;
import com.meli.charge.model.Charge;
import com.meli.charge.service.ChargeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/charge")
public class ChargeRestController {

	@Autowired
	private ChargeService chargeService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Operation(
    		summary = "Permite crear un evento de cargo.",
    		responses = {
    			@ApiResponse(description = "el cargo creado", responseCode = "200",
    				content = @Content (
    					schema = @Schema(implementation = Charge.class),
    					mediaType = "application/json"
    				)
    			 ),
    			@ApiResponse(responseCode = "400", description = "ante un parámetro inválido, o bien si el cargo ya ha sido procesado"),
    			@ApiResponse(responseCode = "500", description = "ante un error inesperado")
    		}
    )
    public ResponseEntity<Charge> createCharge(@Valid @RequestBody ChargeEvent chargeEvt) {
    	Charge charge = chargeService.createCharge(chargeEvt);
    	return new ResponseEntity<Charge>(charge, HttpStatus.OK);
    }

    @Operation(
    		summary = "lista los cargos de un usuario. Cada cargo muestra los pagos asociados.",
    		responses = {
    			@ApiResponse(description = "la lista de cargos", responseCode = "200"),
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
    @RequestMapping(value = "/list/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Charge>> list(@Valid @PathVariable Integer user_id) {
    	List<Charge> payments = chargeService.listByUserId(user_id);
    	return new ResponseEntity<List<Charge>>(payments, HttpStatus.OK);
    }

    @Operation(
    		summary = "lista sólo los cargos pendientes de pago de un usuario.",
    		responses = {
    			@ApiResponse(description = "la lista de cargos", responseCode = "200"),
    			@ApiResponse(responseCode = "400", description = "ante un parámetro inválido",     				
    			content = @Content (
    					schema = @Schema(implementation = ErrorResponse.class),
    					mediaType = "application/json"
    				)),
    			@ApiResponse(responseCode = "500", description = "ante un error inesperado")
    		}
    )
    @RequestMapping(value = "/listpending/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Charge>> listPending(@Valid @PathVariable Integer user_id) {
    	List<Charge> payments = chargeService.listPendingByUserId(user_id);
    	return new ResponseEntity<List<Charge>>(payments, HttpStatus.OK);
    }

    @Operation(
    		summary = "obtiene información de todos los cargos del usuario: cantidad e importe total + fecha del último cargo.",
    		responses = {
    			@ApiResponse(description = "la información resumida de todos los cargos del usuario", responseCode = "200"),
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
    @RequestMapping(value = "/total/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<TotalChargeInfoResponse> totalChargeInfo(@Valid @PathVariable Integer user_id) {
    	return new ResponseEntity<TotalChargeInfoResponse>(chargeService.totalChargeInfo(user_id), HttpStatus.OK);
    }

    @Operation(
    		summary = "obtiene el importe total que adeuda el usuario ",
    		responses = {
    			@ApiResponse(description = "info con el importe total de la deuda", responseCode = "200"),
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
    @RequestMapping(value = "/totalamountpending/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<TotalAmountPendingChargeResponse> totalChargeAmountPending(@Valid @PathVariable Integer user_id) {
    	return new ResponseEntity<TotalAmountPendingChargeResponse>(chargeService.totalChargeAmountPending(user_id), HttpStatus.OK);
    }

}