package com.meli.payment.service;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.meli.payment.exception.PaymentExceedsTotalDebtException;
import com.meli.payment.model.Payment;
import com.meli.payment.repository.PaymentRepository;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = PaymentService.class)
public class PaymentServiceTest {

	@Autowired
	@InjectMocks
	private PaymentService paymentService;

	@Mock
	private ChargeService chargeService;

	@Mock
	private CurrencyService currencyService;
	
	@Mock
	private QueueChargeService queueChargeService;
	
	@Mock
	private PaymentRepository paymentRepo;

	@Test(expected=PaymentExceedsTotalDebtException.class)
	public void testCreatePaymentExceedsTotalDebtException() {
		Integer userId = 1234;
		Double totalDebt = 200d;
		String currency = "ARS";

		when(chargeService.getTotalCharge(userId)).thenReturn(totalDebt);
		when(paymentRepo.existsById(ArgumentMatchers.any(String.class))).thenReturn(false);
		double paymentAmount = totalDebt*2;
		when(currencyService.convertToCurrencyDefault(currency, paymentAmount)).thenReturn(paymentAmount);
//		doNothing().when(queueChargeService).enqueuePayment(ArgumentMatchers.any(Payment.class));

		Payment payment = new Payment();
		payment.setId("alalklakslk");
		payment.setAmount(paymentAmount);
		payment.setCurrency(currency);
		payment.setUserId(userId);
		
		paymentService.createPayment(payment);
	}

}
