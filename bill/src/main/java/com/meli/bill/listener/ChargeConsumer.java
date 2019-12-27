package com.meli.bill.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.meli.bill.model.to.ChargeTO;
import com.meli.bill.service.BillService;

@Component
@EnableJms
public class ChargeConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeConsumer.class);

	@Autowired
	private Gson gson;

	@Autowired
	private BillService billService;

    @JmsListener(destination = "bill.queue")
    public void listener(final Message<String> message) {
    	String chargeStrRep = message.getPayload();
    	LOGGER.info("Se recibió un evento de cargo {} ", chargeStrRep);
		ChargeTO chargeTO = gson.fromJson(chargeStrRep, ChargeTO.class);
    	billService.receiveCharge(chargeTO);
    }

}
