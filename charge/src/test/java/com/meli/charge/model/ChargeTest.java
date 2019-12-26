package com.meli.charge.model;

import org.junit.Assert;
import org.junit.Test;

import com.meli.charge.api.request.ChargeEvent;

public class ChargeTest {

	@Test
	public void testPayAndRelateOk() {
		ChargeEvent chargeEvt = new ChargeEvent();
		double amountCharge = 100d;
		chargeEvt.setAmount(amountCharge);
		chargeEvt.setDate("2019-12-16T03:00:00.000+0000");
		
		ChargeType chargeType = new ChargeType();
		chargeType.setCategory("MARKETPLACE");
		chargeType.setType("CLASIFICADO");
		
		Charge c = new Charge(chargeEvt, amountCharge, chargeType);

		Payment p = new Payment();
		p.setAmount(amountCharge/2);

		c.payAndRelate(p, (Double)(amountCharge/2));

		Assert.assertEquals(c.getAmountPending(), (Double)(amountCharge/2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPayAndRelateError() {
		ChargeEvent chargeEvt = new ChargeEvent();
		double amountCharge = 100d;
		chargeEvt.setAmount(amountCharge);
		chargeEvt.setDate("2019-12-16T03:00:00.000+0000");

		ChargeType chargeType = new ChargeType();
		chargeType.setCategory("MARKETPLACE");
		chargeType.setType("CLASIFICADO");
		
		Charge c = new Charge(chargeEvt, amountCharge, chargeType);

		Payment p = new Payment();
		p.setAmount(amountCharge + 1);

		c.payAndRelate(p, p.getAmount());

	}

}