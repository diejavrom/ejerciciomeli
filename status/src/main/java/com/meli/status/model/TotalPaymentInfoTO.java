package com.meli.status.model;

import java.util.Date;

/**
 * Representa el resumen de pagos provisto por la aplicación payment.
 */
public class TotalPaymentInfoTO {

	private Integer userId;
	private Double totalPayment; // monto total de pagos
	private Date lastPayment; // decha del último pago
	private Integer paymentCount; //total de pagos

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
