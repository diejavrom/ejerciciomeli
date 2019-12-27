package com.meli.charge.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.meli.charge.model.Payment;
import com.meli.charge.service.ChargeService;

@Component
@EnableJms
public class PaymentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumer.class);

	@Autowired
	private Gson gson;

	@Autowired
	private ChargeService chargeService;

    @JmsListener(destination = "charge.queue")
    public void listener(final Message<String> message){
    	String paymentStrRep = message.getPayload();
 
    	LOGGER.info("Se recibió el pago {}", paymentStrRep);

		Payment payment = gson.fromJson(paymentStrRep, Payment.class);
    	chargeService.payChargesWithPayment(payment);
    }

}
