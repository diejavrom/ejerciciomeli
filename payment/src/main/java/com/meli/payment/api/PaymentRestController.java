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

import com.meli.payment.api.reponse.TotalPaymentResponse;
import com.meli.payment.api.request.PaymentEvent;
import com.meli.payment.model.Payment;
import com.meli.payment.service.PaymentService;

@RestController
@RequestMapping(value = "/payment")
public class PaymentRestController {

	@Autowired
	private PaymentService paymentService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Payment> crear(@RequestHeader(value="Idemp-Key") String idempKey, @Valid @RequestBody PaymentEvent paymentEvt) {
    	Payment payment = paymentService.createPayment(paymentEvt, idempKey);
    	return new ResponseEntity<Payment>(payment, HttpStatus.OK);
    }

    @RequestMapping(value = "/list/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Payment>> list(@Valid @PathVariable Integer user_id) {
    	List<Payment> payments = paymentService.listByUserId(user_id);
    	return new ResponseEntity<List<Payment>>(payments, HttpStatus.OK);
    }

    @RequestMapping(value = "/total/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<TotalPaymentResponse> totalDebt(@Valid @PathVariable Integer user_id) {
    	return new ResponseEntity<TotalPaymentResponse>(paymentService.totalPaymentInfo(user_id), HttpStatus.OK);
    }

}