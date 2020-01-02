package com.meli.charge.api.response;

import java.util.Date;

/**
 * Respuesta del servicip sobre el resumen de cargos de un usuario.
 *
 */
public class TotalChargeInfoResponse {

	private Integer userId;
	private Double totalCharge; //monto total de cargos
	private Date lastCharge; //decha del último cargo
	private Integer chargesCount; // cantidad total de cargos

	public TotalChargeInfoResponse(Integer userId, Double totalCharge, Date lastCharge, Integer chargesCount) {
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