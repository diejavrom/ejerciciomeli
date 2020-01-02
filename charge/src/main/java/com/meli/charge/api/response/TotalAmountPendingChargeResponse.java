package com.meli.charge.api.response;

/**
 * Respuesta del servicip sobre el resumen de cargos pendientes de un usuario.
 */
public class TotalAmountPendingChargeResponse {

	private Integer userId;
	private Double totalPendingCharge; //total de cargos pendientes

	public TotalAmountPendingChargeResponse(Integer userId, Double totalPendingCharge) {
		super();
		this.userId = userId;
		this.totalPendingCharge = totalPendingCharge;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getTotalPendingCharge() {
		return totalPendingCharge;
	}

	public void setTotalPendingCharge(Double totalPendingCharge) {
		this.totalPendingCharge = totalPendingCharge;
	}

}