package com.meli.payment.model;

import com.meli.payment.model.to.ChargeTO;

/**
 * Representa un cargo relacionado a un pago.
 */
public class Charge {

	private String id;
	private Double amount;
	private Double amountUsed;
	private String event_type;
	private String category;

	public Charge() {
	}
	
	public Charge(ChargeTO chargeTO, Double amountUsed) {
		setId(chargeTO.getId());
		setAmount(chargeTO.getAmount());
		setAmountUsed(amountUsed);
		setEvent_type(chargeTO.getEvent_type());
		setCategory(chargeTO.getCategory());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmountUsed() {
		return amountUsed;
	}

	public void setAmountUsed(Double amountUsed) {
		this.amountUsed = amountUsed;
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
