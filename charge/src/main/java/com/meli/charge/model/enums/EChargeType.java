package com.meli.charge.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum EChargeType {

	CLASIFICADO("CLASIFICADO", EChargeCategory.MARKETPLACE), VENTA("VENTA", EChargeCategory.MARKETPLACE),
	ENVIO("ENVÍO", EChargeCategory.MARKETPLACE), CREDITO("CRÉDITO", EChargeCategory.SERVICIOS),
	FIDELIDAD("FIDELIDAD", EChargeCategory.SERVICIOS), PUBLICIDAD("PUBLICIDAD", EChargeCategory.SERVICIOS),
	MERCADOPAGO("MERCADOPAGO", EChargeCategory.EXTERNO), MERCADOSHOP("MERCADOSHOP", EChargeCategory.EXTERNO);

	private String name;
	private EChargeCategory category;

	private EChargeType(String name, EChargeCategory category) {
		this.name = name;
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EChargeCategory getCategory() {
		return category;
	}

	public void setCategory(EChargeCategory category) {
		this.category = category;
	}

	public static EChargeType getByName(String name) {
		if (name == null || name.isEmpty()) return null;
		return getKeyMap().get(name);
	}
	
	private static Map<String, EChargeType> keyMap;
	
	private static Map<String, EChargeType> getKeyMap() {
		if (keyMap == null) {
			keyMap = new HashMap<String, EChargeType>();
			EChargeType values[] = values();
			for (int i = 0; i < values.length; i++) {
				keyMap.put(values[i].getName(), values[i]);
			}
		}
		return keyMap;
	}

}