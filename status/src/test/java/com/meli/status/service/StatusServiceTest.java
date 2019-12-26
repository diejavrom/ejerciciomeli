package com.meli.status.service;

import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.meli.status.api.response.StatusUsuarioResponse;
import com.meli.status.exception.ParamMandatoryException;
import com.meli.status.model.TotalChargeInfoTO;
import com.meli.status.model.TotalPaymentInfoTO;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = StatusService.class)
public class StatusServiceTest {

	@Autowired
	@InjectMocks
	private StatusService statusService;

	@Mock
	private PaymentService paymentService;

	@Mock
	private ChargeService chargeService;

	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testDebtWithNotNullParam() {
		Integer userId = 1234;
	
		double amountPayment = 550d;
		TotalPaymentInfoTO totalPaymentInfoTO = new TotalPaymentInfoTO(userId, amountPayment, new Date(), 3);
		when(paymentService.getTotalPayment(userId)).thenReturn(totalPaymentInfoTO);
	
		double amountCharge = 1050d;
		TotalChargeInfoTO totalChargeInfoTO = new TotalChargeInfoTO(userId, amountCharge, new Date(), 6);
		when(chargeService.getTotalCharge(userId)).thenReturn(totalChargeInfoTO);

		StatusUsuarioResponse status = statusService.getStatus(userId);
		Assert.assertEquals(status.getDebt(), (Double)(amountCharge - amountPayment));
	}

	@Test(expected = ParamMandatoryException.class)
	public void testDebtWithNullParam() {
		statusService.getStatus(null);
	}

}