package com.meli.bill.model;

import org.junit.Assert;
import org.junit.Test;

import com.meli.bill.model.to.ChargeTO;

public class ChargeTest {

	@Test
	public void testExistencialityPayment() {
		Charge c = new Charge();
		double amount = 10d;
		c.setAmount(amount);
		c.setAmountPending(amount);
		
		String idPayment = "1234";
		InfoPayment ip = new InfoPayment(idPayment, amount/2);
		
		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setPaymentAmount(ip.getAmount());
		chargeTO.setPaymentId(ip.getId());

		c.updateWithChargeTOInfo(chargeTO);

		Assert.assertNotNull(c.getInfoPaymentById(idPayment));
		Assert.assertNull(c.getInfoPaymentById("12345"));
	}

	@Test
	public void testAmountInvariantPayment() {
		Charge c = new Charge();
		double amount = 100d;
		c.setAmount(amount);
		c.setAmountPending(amount);

		String idPayment = "1234";
		InfoPayment ip = new InfoPayment(idPayment, amount/2);

		ChargeTO chargeTO = new ChargeTO();
		Double amountTO = 30d;
		chargeTO.setAmountPending(amountTO);
		chargeTO.setPaymentAmount(ip.getAmount());
		chargeTO.setPaymentId(ip.getId());

		c.updateWithChargeTOInfo(chargeTO);

		Assert.assertEquals(c.getAmountPending(), amountTO);
	}

}
