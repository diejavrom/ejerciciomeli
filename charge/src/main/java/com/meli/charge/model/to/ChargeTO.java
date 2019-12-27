package com.meli.charge.model.to;

import java.io.Serializable;

public class ChargeTO implements Serializable {

	private static final long serialVersionUID = 4531118806583331062L;

	private String id;
	private Double amountUsed;

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
