package com.meli.payment.model.to;

import java.io.Serializable;

public class TotalDebtTO implements Serializable {

	private static final long serialVersionUID = -7606412876851538738L;

	private Integer userId;
	private Double totalPendingCharge;

	public TotalDebtTO() {
	}

	public TotalDebtTO(Integer userId, Double totalPendingCharge) {
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
