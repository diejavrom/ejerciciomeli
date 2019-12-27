package com.meli.bill.model;

import org.junit.Assert;
import org.junit.Test;

import com.meli.bill.model.to.ChargeTO;

public class BillTest {

	@Test
	public void testAmountInvariant() {
		Bill b = new Bill();

		Double a1 = 70d;
		Charge c1 = new Charge();
		c1.setId("1234");
		c1.setAmount(a1);
		c1.setAmountPending(a1/2);
		b.updateCharge(c1);

		Double a2 = 120d;
		Charge c2 = new Charge();
		c2.setId("1235");
		c2.setAmount(a2);
		c2.setAmountPending(a2/2);
		b.updateCharge(c2);

		Assert.assertEquals(b.getAmount(), (Double) (a1+a2));
		Assert.assertEquals(b.getPendingAmount(), (Double) ((a1+a2)/2));
		
	}

	@Test
	public void testExistencialityCharge() {
		Bill b = new Bill();
		
		Charge c1 = new Charge();
		String id1 = "1234";
		c1.setId(id1);
		c1.setAmount(20d);
		b.updateCharge(c1);
		
		String id2 = "1235";

		Assert.assertTrue(b.getChargeById(id1) != null);
		Assert.assertTrue(b.getChargeById(id2) == null);
	}

	@Test
	public void testExistencialityChargeAndPayment() {
		Bill b = new Bill();
	
		Charge c1 = new Charge();
		String idCharge1 = "1234";
		c1.setId(idCharge1);
		c1.setAmount(20d);
		b.updateCharge(c1);

		String idPayment = "kldild";
		InfoPayment ip = new InfoPayment(idPayment, 100d);
		
		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setPaymentAmount(ip.getAmount());
		chargeTO.setPaymentId(ip.getId());

		c1.updateWithChargeTOInfo(chargeTO);

		Assert.assertTrue(b.existsChargeWithPayment(idCharge1, idPayment));
		Assert.assertTrue(!b.existsChargeWithPayment("lkdkldlkd", "sklslkslks"));
	}

}