package com.meli.payment.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.meli.payment.model.to.ChargeQueueTO;
import com.meli.payment.service.PaymentService;

@Component
@EnableJms
public class PaymentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumer.class);

	@Autowired
	private Gson gson;

	@Autowired
	private PaymentService paymentService;

    @JmsListener(destination = "payment.queue")
    public void listener(final Message<String> message){
    	String paymentStrRep = message.getPayload();
 
    	LOGGER.info("Se recibió una actualización de pago  {}", paymentStrRep);

    	ChargeQueueTO chargeQueueTO = gson.fromJson(paymentStrRep, ChargeQueueTO.class);
    	paymentService.updatePayments(chargeQueueTO);
    }

}
