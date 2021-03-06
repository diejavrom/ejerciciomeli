package com.meli.bill.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.meli.bill.model.to.ChargeTO;

/**
 * Representa un cargo de una factura
 */
@Document(collection = "charges")
public class Charge {

	private String id;
	private Integer event_id;
	private Integer userId;
	private Double amount;
	private Double amountPending;
	private Date dateObj;
	private List<InfoPayment> payments;

	public Charge() {
		this.payments = new ArrayList<InfoPayment>();
	}

	public Charge(ChargeTO chargeTO) {
		this();
		setId(chargeTO.getId());
		setEvent_id(chargeTO.getEvent_id());
		setUserId(chargeTO.getUserId());
		setAmount(chargeTO.getAmount());
		setAmountPending(chargeTO.getAmountPending());
		setDateObj(chargeTO.getDateObj());
		if(chargeTO.getPaymentId() != null) {
			getPayments().add(new InfoPayment(chargeTO.getPaymentId(), chargeTO.getPaymentAmount()));
		}
	}

	@Id
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

	private List<InfoPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<InfoPayment> payments) {
		this.payments = payments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Charge other = (Charge) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public InfoPayment getInfoPaymentById(String paymentId) {
		for(InfoPayment ip : getPayments()) {
			if(ip.getId().equals(paymentId)) {
				return ip;
			}
 		}
		return null;
	}

	public void updateWithChargeTOInfo(ChargeTO chargeTO) {
		setAmountPending(chargeTO.getAmountPending());
		if(chargeTO.getPaymentId() != null) {
			getPayments().add(new InfoPayment(chargeTO.getPaymentId(), chargeTO.getPaymentAmount()));
		}
	}

}