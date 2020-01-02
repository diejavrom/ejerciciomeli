package com.meli.charge.model.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un pago recibido desde la cola de eventos
 * "charge.queue". El mismo posee que cargos (<code>ChargeTO</code>) fueron pagados 
 * con el monto de dicho pago.
 */
public class PaymentTO implements Serializable {

	private static final long serialVersionUID = 3835443690263526093L;

	private String id;
	private Double amount;
	private Double availableAmount;
	private Integer userId;
	private List<ChargeTO> charges;
	
	public PaymentTO() {
		setCharges(new ArrayList<ChargeTO>());
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<ChargeTO> getCharges() {
		return charges;
	}

	public void setCharges(List<ChargeTO> charges) {
		this.charges = charges;
	}

	public Double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Double availableAmount) {
		this.availableAmount = availableAmount;
	}

}