package com.meli.bill.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bills")
@CompoundIndexes({
    @CompoundIndex(name = "bill_unique", unique=true, def = "{'userId' : 1, 'month': 1, 'year' : 1}")
})
public class Bill {

	private String id;

	private Integer userId;
	private Double amount;
	private Integer month;
	private Integer year;
	private List<Charge> charges;

	public Bill() {
		setCharges(new ArrayList<Charge>());
		setAmount(0d);
	}

	public Bill(Integer idUsuario, Integer month, Integer year) {
		this();
		setUserId(idUsuario);
		setMonth(month);
		setYear(year);
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getAmount() {
		return amount;
	}

	private void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPendingAmount() {
		return getCharges().stream().mapToDouble(c -> c.getAmountPending()).sum();
	}

	public List<Charge> getCharges() {
		return charges;
	}

	private void setCharges(List<Charge> charges) {
		this.charges = charges;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void updateCharge(Charge charge) {
		removeCharge(charge);
		getCharges().add(charge);
		setAmount(getAmount() + charge.getAmount());
	}

	private void removeCharge(Charge charge) {
		boolean remove = getCharges().remove(charge);
		if (remove) {
			setAmount(getAmount() - charge.getAmount());
		}
	}

	public Charge getChargeById(String chargeId) {
		for (Charge c : getCharges()) {
			if (c.getId().equals(chargeId)) {
				return c;
			}
		}
		return null;
	}

	public boolean existsChargeWithPayment(String chargeId, String paymentId) {
		Charge charge = getChargeById(chargeId);
		return charge != null && charge.getInfoPaymentById(paymentId) != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		Bill other = (Bill) obj;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

}