package com.meli.payment.model.to;

import java.io.Serializable;

/**
 * Representa un pago recibido desde la cola de eventos "payment.queue". El
 * mismo posee que cargos (<code>ChargeTO</code>) fueron pagados con el monto de
 * dicho pago.
 */
public class PaymentTO implements Serializable {

	private static final long serialVersionUID = 3835443690263526093L;

	private String id;
	private Double amountUsed;
	private Integer userId;

	public PaymentTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getAmountUsed() {
		return amountUsed;
	}

	public void setAmountUsed(Double amountUsed) {
		this.amountUsed = amountUsed;
	}

}