package com.meli.charge.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chargestypes")
public class ChargeType {

	public String category;
	public String type;

	public ChargeType() {
	}

	public ChargeType(String category, String type) {
		super();
		this.category = category;
		this.type = type;
	}
	
	@Id
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
