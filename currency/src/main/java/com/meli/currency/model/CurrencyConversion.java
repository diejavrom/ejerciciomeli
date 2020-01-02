package com.meli.currency.model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.meli.currency.exception.ParamMandatoryException;

/**
 * Configuración del valor de una moneda. 
 */
@Document(collection = "currency")
public class CurrencyConversion {

	private String id;
	private Date since; //fecha desde
	private Date until; // fecha hasta, puede ser null
	private Double conversionFactor; //factor de conversión con respecto a la moneda default
	private String name; // nombre de la moneda
	private String description; //descrión
	private Boolean def; // flag que indica si la moneda es la default del sistema

	public CurrencyConversion() {
	}
	
	public CurrencyConversion(Date since, Date until, Double conversionFactor, String name, String description, Boolean def) {
		super();
		this.since = since;
		this.until = until;
		this.conversionFactor = conversionFactor;
		this.name = name;
		this.description = description;
		this.def = def;
	}
	
	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}

	public Date getUntil() {
		return until;
	}

	public void setUntil(Date until) {
		this.until = until;
	}

	public Double getConversionFactor() {
		return conversionFactor;
	}

	public void setConversionFactor(Double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDef() {
		return def;
	}

	public void setDef(Boolean def) {
		this.def = def;
	}

	public boolean isBetween(Timestamp now) {
		long nowL = now.getTime();
		long sinceL = since.getTime();
		return (until == null && nowL >= sinceL) || (nowL >= sinceL && nowL <= until.getTime());
	}

	public Double convert(Double amount) {
		if(amount == null) {
			throw new ParamMandatoryException("amount no debe ser null");
		}
		return getConversionFactor() * amount;
	}

}