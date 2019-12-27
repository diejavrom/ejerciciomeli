package com.meli.charge.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.meli.charge.DateHelper;
import com.meli.charge.api.request.ChargeEvent;

@Document(collection = "charges")
public class Charge {

	private String id;
	@Indexed(name="event_id_unique", unique = true)
	private Integer event_id;
	private Integer userId;
	private Double originalAmount;
	private Double amount;
	private Double amountPending;
	private String event_type;
	private String category;
	private String date;
	private Date dateObj;
	private String currency;
	private List<Payment> payments;

	public Charge() {
		this.payments = new ArrayList<Payment>();
	}

	public Charge(ChargeEvent chargeEvt, Double amountInDefCurrency, ChargeType chargeType) {
		this();
		setEvent_id(chargeEvt.getEvent_id());
		setUserId(chargeEvt.getUserId());
		setOriginalAmount(chargeEvt.getAmount());
		setAmount(amountInDefCurrency);
		setAmountPending(amountInDefCurrency);
		setEvent_type(chargeEvt.getEvent_type());
		setDate(chargeEvt.getDate());
		setDateObj(DateHelper.getInstance().stringToTimestamp(chargeEvt.getDate()));
		setCurrency(chargeEvt.getCurrency());
		setCategory(chargeType.getCategory());
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
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

	public Double getAmountPending() {
		return amountPending;
	}

	private void setAmountPending(Double amountPending) {
		this.amountPending = amountPending;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getDateObj() {
		return dateObj;
	}

	private void setDateObj(Date dateObj) {
		this.dateObj = dateObj;
	}

	public Double getOriginalAmount() {
		return originalAmount;
	}

	private void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getCategory() {
		return category;
	}

	private void setCategory(String category) {
		this.category = category;
	}

	public void payAndRelate(Payment pago, Double amount) {
		if(getAmountPending() < amount) {
			throw new IllegalArgumentException(String.format("El monto del cargo % por saldar es menor al monto recibido por descontar %", getAmountPending(), pago.getAmount()));
		}
		setAmountPending(getAmountPending() - amount);
		getPayments().add(pago);
	}

}