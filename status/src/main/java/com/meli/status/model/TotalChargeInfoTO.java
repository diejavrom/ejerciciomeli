package com.meli.status.model;

import java.util.Date;

/**
 * Representa el resumen de cargos provisto por la aplicación charge.
 */
public class TotalChargeInfoTO {

	private Integer userId;
	private Double totalCharge; //monto total de cargos
	private Date lastCharge; // fecha del último cargo
	private Integer chargesCount; // cantidad de cargos

	public TotalChargeInfoTO() {
	}
	
	public TotalChargeInfoTO(Integer userId, Double totalCharge, Date lastCharge, Integer chargesCount) {
		super();
		this.userId = userId;
		this.totalCharge = totalCharge;
		this.lastCharge = lastCharge;
		this.chargesCount = chargesCount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(Double totalCharge) {
		this.totalCharge = totalCharge;
	}

	public Date getLastCharge() {
		return lastCharge;
	}

	public void setLastCharge(Date lastCharge) {
		this.lastCharge = lastCharge;
	}

	public Integer getChargesCount() {
		return chargesCount;
	}

	public void setChargesCount(Integer chargesCount) {
		this.chargesCount = chargesCount;
	}

}