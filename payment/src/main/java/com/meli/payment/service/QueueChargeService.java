package com.meli.payment.service;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.meli.payment.model.Payment;

/**
 * Servicio encargado de notificar un pago procesado a la coal de cargos. 
 */
@Service
public class QueueChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueChargeService.class);

	@Autowired
	private Queue queueCharge;

	@Autowired
	private Gson gson;

	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * notifica el pago <code>payment</code> en la cola de cargos.
	 * @param payment
	 */
	public void enqueuePayment(Payment payment) {
		String paymentStr = gson.toJson(payment);

		LOGGER.info("Encolando mensaje {} en cola de cargos", paymentStr);

		jmsTemplate.convertAndSend(queueCharge, paymentStr);
	}

}