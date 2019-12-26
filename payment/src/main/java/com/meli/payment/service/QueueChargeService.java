package com.meli.payment.service;

import javax.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.meli.payment.model.Payment;

@Service
public class QueueChargeService {

	@Autowired
	private Queue queueCharge;

	@Autowired
	private Gson gson;

	@Autowired
	private JmsTemplate jmsTemplate;

	public void enqueuePayment(Payment payment) {
		String paymentStr = gson.toJson(payment);
		jmsTemplate.convertAndSend(queueCharge, paymentStr);
	}

}