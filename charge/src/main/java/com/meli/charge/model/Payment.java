package com.meli.charge.model;

import java.io.Serializable;

/**
 * Representa un pago realizado a un cargo.
 *
 */
public class Payment implements Serializable {

	private static final long serialVersionUID = -8929567302784045440L;

	private String id;
	private Double amount;
	private Integer userId;

	public Payment(String id, Double amount, Integer userId) {
		super();
		this.id = id;
		this.amount = amount;
		this.userId = userId;
	}

	public Payment() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}