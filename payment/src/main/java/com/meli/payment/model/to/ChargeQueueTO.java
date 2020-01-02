package com.meli.payment.model.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChargeQueueTO implements Serializable {

	private static final long serialVersionUID = 5529005193407957762L;

	private String id;
	private Double amount;
	private String event_type;
	private String category;
	private List<PaymentTO> payments;

	public ChargeQueueTO() {
		this.payments = new ArrayList<PaymentTO>();
	}

	public List<PaymentTO> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentTO> payments) {
		this.payments = payments;
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