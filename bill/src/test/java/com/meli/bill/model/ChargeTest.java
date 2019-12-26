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

		Assert.assertTrue(c.getInfoPaymentById(idPayment) != null);
		Assert.assertTrue(c.getInfoPaymentById("12345") == null);
	}

	@Test
	public void testAmountPayment() {
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
