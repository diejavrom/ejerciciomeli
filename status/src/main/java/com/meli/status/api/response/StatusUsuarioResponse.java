package com.meli.status.api.response;

import java.util.Date;

import com.meli.status.model.TotalChargeInfoTO;
import com.meli.status.model.TotalPaymentInfoTO;

public class StatusUsuarioResponse {

	private Integer userId;
	private Integer totalCharge;
	private Double amountTotalCharge;
	private Date lastCharge;
	private Integer totalPayment;
	private Double amountTotalPayment;
	private Date lastPayment;
	private Double debt;

	public StatusUsuarioResponse(Integer userId, TotalChargeInfoTO totalInfoCharge, TotalPaymentInfoTO totalPaymentInfo) {
		setUserId(userId);
		setAmountTotalCharge(totalInfoCharge.getTotalCharge());
		setLastCharge(totalInfoCharge.getLastCharge());
		setTotalCharge(totalInfoCharge.getChargesCount());
		setAmountTotalPayment(totalPaymentInfo.getTotalPayment());
		setLastPayment(totalPaymentInfo.getLastPayment());
		setTotalPayment(totalPaymentInfo.getPaymentCount());
		setDebt(getAmountTotalPayment() - getAmountTotalCharge());
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(Integer totalCharge) {
		this.totalCharge = totalCharge;
	}

	public Double getAmountTotalCharge() {
		return amountTotalCharge;
	}

	public void setAmountTotalCharge(Double amountTotalCharge) {
		this.amountTotalCharge = amountTotalCharge;
	}

	public Date getLastCharge() {
		return lastCharge;
	}

	public void setLastCharge(Date lastCharge) {
		this.lastCharge = lastCharge;
	}

	public Integer getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Integer totalPayment) {
		this.totalPayment = totalPayment;
	}

	public Double getAmountTotalPayment() {
		return amountTotalPayment;
	}

	public void setAmountTotalPayment(Double amountTotalPayment) {
		this.amountTotalPayment = amountTotalPayment;
	}

	public Date getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(Date lastPayment) {
		this.lastPayment = lastPayment;
	}

	public Double getDebt() {
		return debt;
	}

	public void setDebt(Double debt) {
		this.debt = debt;
	}

}