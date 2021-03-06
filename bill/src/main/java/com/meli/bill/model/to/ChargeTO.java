package com.meli.bill.model.to;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa una notificación de un cargo nuevo ingresado
 * o bien una actualización de pago del mismo.
 */
public class ChargeTO implements Serializable {

	private static final long serialVersionUID = 2435817384119643650L;

	private String id;
	private Integer event_id;
	private Integer userId;
	private Double amount;
	private Double amountPending;
	private Date dateObj;
	private String paymentId;
	private Double paymentAmount;

	public ChargeTO() {
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