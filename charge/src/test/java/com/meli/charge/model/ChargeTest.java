package com.meli.charge.model;

import org.junit.Assert;
import org.junit.Test;

import com.meli.charge.api.request.ChargeEvent;
import com.meli.charge.model.enums.EChargeType;

public class ChargeTest {

	@Test
	public void testPayAndRelateOk() {
		ChargeEvent chargeEvt = new ChargeEvent();
		chargeEvt.setEvent_type(EChargeType.FIDELIDAD.getName());
		double amountCharge = 100d;
		chargeEvt.setAmount(amountCharge);
		chargeEvt.setDate("2019-12-16T03:00:00.000+0000");
		
		Charge c = new Charge(chargeEvt, amountCharge);

		Payment p = new Payment();
		p.setAmount(amountCharge/2);

		c.payAndRelate(p, (Double)(amountCharge/2));

		Assert.assertEquals(c.getAmountPending(), (Double)(amountCharge/2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPayAndRelateError() {
		ChargeEvent chargeEvt = new ChargeEvent();
		double amountCharge = 100d;
		chargeEvt.setEvent_type(EChargeType.FIDELIDAD.getName());
		chargeEvt.setAmount(amountCharge);
		chargeEvt.setDate("2019-12-16T03:00:00.000+0000");
		
		Charge c = new Charge(chargeEvt, amountCharge);

		Payment p = new Payment();
		p.setAmount(amountCharge + 1);

		c.payAndRelate(p, p.getAmount());

	}

}