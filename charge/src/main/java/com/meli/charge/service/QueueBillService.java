package com.meli.charge.service;

import java.io.Serializable;
import java.util.Date;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.meli.charge.model.Charge;
import com.meli.charge.model.Payment;

@Service
public class QueueBillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);
	
	@Autowired
	private Queue queueBill;

	@Autowired
	private Gson gson;

	@Autowired
	private JmsTemplate jmsTemplate;
	
	public void enqueueCharge(Charge charge, Payment payment, Double amountToUSe) {
		ChargeTO chargeTO = new ChargeTO(charge, payment, amountToUSe);
		String chargeStr = gson.toJson(chargeTO);
	
		LOGGER.info("Encolando mensaje {} en cola de facturas", chargeStr);

		jmsTemplate.convertAndSend(queueBill, chargeStr);
		
	}

	@SuppressWarnings("unused")
	private static class ChargeTO implements Serializable {

		private static final long serialVersionUID = 2435817384119643650L;

		private String id;
		private Integer event_id;
		private Integer userId;
		private Double amount;
		private Double amountPending;
		private Date dateObj;
		private String paymentId;
		private Double paymentAmount;

		public ChargeTO(Charge charge, Payment payment, Double amountToUSe) {
			setId(charge.getId());
			setEvent_id(charge.getEvent_id());
			setUserId(charge.getUserId());
			setAmount(charge.getAmount());
			setAmountPending(charge.getAmountPending());
			setDateObj(charge.getDateObj());
			if(payment != null) {
				setPaymentId(payment.getId());
				setPaymentAmount(amountToUSe);
			}
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Integer getEvent_id() {
			return event_id;
		}

		public void setEvent_id(Integer event_id) {
			this.event_id = event_id;
		}

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public Double getAmountPending() {
			return amountPending;
		}

		public void setAmountPending(Double amountPending) {
			this.amountPending = amountPending;
		}

		public Date getDateObj() {
			return dateObj;
		}

		public void setDateObj(Date dateObj) {
			this.dateObj = dateObj;
		}

		public String getPaymentId() {
			return paymentId;
		}

		public void setPaymentId(String paymentId) {
			this.paymentId = paymentId;
		}

		public Double getPaymentAmount() {
			return paymentAmount;
		}

		public void setPaymentAmount(Double paymentAmount) {
			this.paymentAmount = paymentAmount;
		}
		
		
	}

}