package com.meli.charge.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class ChargeEvent {

    @Schema(description = "indica el id del evento", example = "1", required = true)	
	private Integer event_id;
    @Schema(description = "indica el número de usuario", example = "1", required = true)	
	private Integer user_id;
	@Schema(description = "indica el monto a cobrar, con dos decimales", example = "150.10", required = true)	
	private Double amount;
	@Schema(description = "indica el tipo de evento", example = "CLASIFICADO", required = true)	
	private String event_type;
	@Schema(description = "indica la feha y hora en la que ocurrió el evento", example = "2019-05-16T00:00:00", required = true)	
	private String date;
	@Schema(description = "indica la moneda", example = "ARS", required = true)	
	private String currency;

	public ChargeEvent() {
	}

	public Integer getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
