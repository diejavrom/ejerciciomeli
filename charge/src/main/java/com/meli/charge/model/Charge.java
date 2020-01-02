package com.meli.charge.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.meli.charge.DateHelper;
import com.meli.charge.api.request.ChargeEvent;
import com.meli.charge.exception.ChargeTypeException;
import com.meli.charge.model.enums.EChargeType;

/**
 * Representa un cargo 
 */
@Document(collection = "charges")
public class Charge {

	private String id;
	@Indexed(name="event_id_unique", unique = true)
	private Integer eventId;
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

	/**
	 * Construye un cargo con el evento que lo dio origen y con un monto en la moneda default.
	 * @param chargeEvt
	 * @param amountInDefCurrency
	 */
	public Charge(ChargeEvent chargeEvt, Double amountInDefCurrency) {
		this();
		setEventId(chargeEvt.getEvent_id());
		setUserId(chargeEvt.getUser_id());
		setOriginalAmount(chargeEvt.getAmount());
		setAmount(amountInDefCurrency);
		setAmountPending(amountInDefCurrency);
		setDate(chargeEvt.getDate());
		setDateObj(DateHelper.getInstance().stringToTimestamp(chargeEvt.getDate()));
		setCurrency(chargeEvt.getCurrency());
		setEvent_type(chargeEvt.getEvent_type());
		EChargeType eChargeType = EChargeType.getByName(chargeEvt.getEvent_type());
		if(eChargeType == null) {
			throw new ChargeTypeException(String.format("event_type '%s' inválido", chargeEvt.getEvent_type()));
		}
		setCategory(eChargeType.getCategory().toString());
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
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

	/**
	 * Descuenta <code>amount</code> del salgo pendiente del cargo y agrega
	 * <code>pago</code> a la lista de pagos relacionados
	 * @param pago
	 * @param amount
	 */
	public void payAndRelate(Payment pago, Double amount) {
		if(getAmountPending() < amount) {
			throw new IllegalArgumentException(String.format("El monto del cargo % por saldar es menor al monto recibido por descontar %", getAmountPending(), pago.getAmount()));
		}
		setAmountPending(getAmountPending() - amount);
		getPayments().add(pago);
	}

}