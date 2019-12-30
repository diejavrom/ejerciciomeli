package com.meli.charge.api.response;

public class TotalAmountPendingChargeResponse {

	private Integer userId;
	private Double totalPendingCharge;

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