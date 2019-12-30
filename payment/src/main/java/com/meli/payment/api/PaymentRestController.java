package com.meli.payment.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meli.payment.api.reponse.ErrorResponse;
import com.meli.payment.api.reponse.TotalPaymentResponse;
import com.meli.payment.api.request.PaymentEvent;
import com.meli.payment.model.Payment;
import com.meli.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/payment")
public class PaymentRestController {

	@Autowired
	private PaymentService paymentService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Operation(
    		summary = "Permite crear un evento de pago.",
    		responses = {
    			@ApiResponse(description = "el pago creado", responseCode = "200",
    				content = @Content (
    					schema = @Schema(implementation = Payment.class),
    					mediaType = "application/json"
    				)
    			 ),
    			@ApiResponse(responseCode = "400", description = "ante un parámetro inválido, o bien si el pago ya ha sido procesado o si el mismo excede la deuda del usuario"),
    			@ApiResponse(responseCode = "500", description = "ante un error inesperado")
    		}
    )
    public ResponseEntity<Payment> crear(@RequestHeader(value="Idemp-Key") String idempKey, @Valid @RequestBody PaymentEvent paymentEvt) {
    	Payment payment = paymentService.createPayment(paymentEvt, idempKey);
    	return new ResponseEntity<Payment>(payment, HttpStatus.OK);
    }

    @RequestMapping(value = "/list/{user_id}", method = RequestMethod.GET)
    @Operation(
    		summary = "lista todos los pagos del usuario. Cada pago muestra los cargos que fueron pagados con su importe",
    		responses = {
    			@ApiResponse(description = "la lista de cargos", responseCode = "200",
    				content = @Content (
    					schema = @Schema(implementation = Payment.class),
    					mediaType = "application/json"
    				)
    			 ),
    			@ApiResponse(responseCode = "400", description = "ante un parámetro inválido"),
    			@ApiResponse(responseCode = "500", description = "ante un error inesperado")
    		}
    )
    public ResponseEntity<List<Payment>> list(@Valid @PathVariable Integer user_id) {
    	List<Payment> payments = paymentService.listByUserId(user_id);
    	return new ResponseEntity<List<Payment>>(payments, HttpStatus.OK);
    }

    @RequestMapping(value = "/total/{user_id}", method = RequestMethod.GET)
    @Operation(
    		summary = "obtiene información sobre los pagos del usuario: cantidad e importe total + fecha del último pago.",
    		responses = {
    			@ApiResponse(description = "la información resumida de los pagos del usuario", responseCode = "200",
    				content = @Content (
    					schema = @Schema(implementation = Payment.class),
    					mediaType = "application/json"
    				)
    			 ),
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
    public ResponseEntity<TotalPaymentResponse> totalDebt(@Valid @PathVariable Integer user_id) {
    	return new ResponseEntity<TotalPaymentResponse>(paymentService.totalPaymentInfo(user_id), HttpStatus.OK);
    }

}