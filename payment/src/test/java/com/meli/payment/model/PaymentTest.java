package com.meli.payment.model;

import org.junit.Assert;
import org.junit.Test;

import com.meli.payment.model.to.ChargeTO;

public class PaymentTest {

	@Test
	public void addCharge() {
		Double amountPayment = 100d;
		Double amountCharge = 20d;
		Double amountUsed = 20d;

		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setAmount(amountCharge);
		chargeTO.setAmountPending(amountCharge);
		
		Charge charge = new Charge(chargeTO, amountUsed);
		charge.setId("1234456");

		Payment payment = new Payment();
		payment.setAmount(amountPayment);
		payment.setAvailableAmount(amountPayment);
		
		payment.payCharge(charge, amountUsed);

		Assert.assertEquals(payment.getAvailableAmount(), (Double)(amountPayment-amountUsed));
		Assert.assertTrue(payment.getCharges().contains(charge));
		
		
	}
	
	
}
