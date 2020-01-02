package com.meli.charge.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.meli.charge.model.Charge;
import com.meli.charge.model.Payment;

@Service
public class QueuePaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueuePaymentService.class);
	
	@Autowired
	private Gson gson;

	@Autowired
	private JmsTemplate jmsTemplate;

	public void enqueueCharge(Charge charge) {
		Queue queue = new ActiveMQQueue("payment.queue");
		
		ChargeTO chargeTO = new ChargeTO(charge);
		String chargeStr = gson.toJson(chargeTO);
	
		LOGGER.info("Encolando mensaje {} en cola de pagos", chargeStr);

		jmsTemplate.convertAndSend(queue, chargeStr);
		
	}

	@SuppressWarnings("unused")
	private static class ChargeTO implements Serializable {

		private static final long serialVersionUID = 2435817384119643650L;

		private String id;
		private Integer event_id;
		private Integer userId;
		private Double amount;
		private Double amountPending;
		private String event_type;
		private String category;
		private Date dateObj;
		private List<PaymentTO> payments;

		public ChargeTO(Charge charge) {
			setId(charge.getId());
			setEvent_id(charge.getEventId());
			setUserId(charge.getUserId());
			setAmount(charge.getAmount());
			setAmountPending(charge.getAmountPending());
			setDateObj(charge.getDateObj());
			setEvent_type(charge.getEvent_type());
			setCategory(charge.getCategory());
			//payments
			setPayments(new ArrayList<PaymentTO>());
			for(Payment payment : charge.getPayments()) {
				getPayments().add(new PaymentTO(payment.getId(), payment.getAmount()));
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

		public List<PaymentTO> getPayments() {
			return payments;
		}

		public void setPayments(List<PaymentTO> payments) {
			this.payments = payments;
		}

		public String getEvent_type() {
			return event_type;
		}

		public void setEvent_type(String event_type) {
			this.event_type = event_type;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}
		
	}

	@SuppressWarnings("unused")
	private static class PaymentTO implements Serializable {
		
		private static final long serialVersionUID = -6982138288658285496L;

		private String id;
		private Double amountUsed;

		public PaymentTO(String id, Double amountUsed) {
			super();
			this.id = id;
			this.amountUsed = amountUsed;
		}
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		public Double getAmountUsed() {
			return amountUsed;
		}

		public void setAmountUsed(Double amountUsed) {
			this.amountUsed = amountUsed;
		}

	}

}