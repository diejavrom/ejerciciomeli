package com.meli.charge.api.response;

public class TotalPendingChargeResponse {

	private Integer userId;
	private Double totalPendingCharge;

	public TotalPendingChargeResponse(Integer userId, Double totalPendingCharge) {
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