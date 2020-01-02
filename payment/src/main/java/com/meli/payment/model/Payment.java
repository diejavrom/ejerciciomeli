package com.meli.payment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.meli.payment.api.request.PaymentEvent;

/**
 * Representa un pago.
 */
@Document(collection = "payments")
public class Payment implements Serializable {

	private static final long serialVersionUID = 642126083896518679L;

	private String id;
	private String currency;
	@Indexed(name="idempkey_unique", unique = true)
	private String idempKey;
	private Double amount;
	private Double originalAmount;
	private Date dateObj;
	private Integer userId;
	private List<Charge> charges;

	public Payment() {
		setCharges(new ArrayList<Charge>());
	}

	public Payment(PaymentEvent paymentEvt, Double amountInCurrencyDefault, String idempKey) {
		this();
		setAmount(amountInCurrencyDefault);
		setOriginalAmount(paymentEvt.getAmount());
		setCurrency(paymentEvt.getCurrency());
		setUserId(paymentEvt.getUser_id());
		setDateObj(new Date());
		setIdempKey(idempKey);
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public void setUserId(Integer user_id) {
		this.userId = user_id;
	}

	public Double getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public Date getDateObj() {
		return dateObj;
	}

	public void setDateObj(Date dateObj) {
		this.dateObj = dateObj;
	}
	
	public String getIdempKey() {
		return idempKey;
	}

	public void setIdempKey(String idempKey) {
		this.idempKey = idempKey;
	}

	public List<Charge> getCharges() {
		return charges;
	}

	public void setCharges(List<Charge> charges) {
		this.charges = charges;
	}

	
}