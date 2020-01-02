package com.meli.status.api.response;

import java.util.Date;

import com.meli.status.model.TotalChargeInfoTO;
import com.meli.status.model.TotalPaymentInfoTO;

/**
 * Representa el estado de deuda del usuario brindando además un resumen
 * de los pagos y cargos.
 */
public class StatusUsuarioResponse {

	private Integer userId;
	private Integer totalCharge; //cantidad de cargos del usuario
	private Double amountTotalCharge; // monto total de cargos
	private Date lastCharge; // fecha del último cargo ingresado
	private Integer totalPayment; // cantidad de pagos del usuario
	private Double amountTotalPayment; // monto total de pagos
	private Date lastPayment; // fecha del último pago realizado
	private Double debt; // monto de la deuda

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