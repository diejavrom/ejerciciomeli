package com.meli.status.model;

import java.util.Date;

public class TotalPaymentInfoTO {

	private Integer userId;
	private Double totalPayment;
	private Date lastPayment;
	private Integer paymentCount;

	public TotalPaymentInfoTO() {
	}
	
	public TotalPaymentInfoTO(Integer userId, Double totalPayment, Date lastPayment, Integer paymentCount) {
		super();
		this.userId = userId;
		this.totalPayment = totalPayment;
		this.lastPayment = lastPayment;
		this.paymentCount = paymentCount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

	public Date getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(Date lastPayment) {
		this.lastPayment = lastPayment;
	}

	public Integer getPaymentCount() {
		return paymentCount;
	}

	public void setPaymentCount(Integer paymentCount) {
		this.paymentCount = paymentCount;
	}

}