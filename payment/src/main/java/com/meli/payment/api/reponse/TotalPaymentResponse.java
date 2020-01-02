package com.meli.payment.api.reponse;

import java.util.Date;

/**
 * Representa la respuesta del resumen de pagos realizados por un usuario. 
 */
public class TotalPaymentResponse {

	private Integer userId;
	private Double totalPayment; //monto total de pagos realizados
	private Date lastPayment; //último pago realizado
	private Integer paymentCount; // cantidad de pagos realizados

	public TotalPaymentResponse(Integer userId, Double totalPayment, Date lastPayment, Integer paymentCount) {
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