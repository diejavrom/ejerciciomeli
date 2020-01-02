package com.meli.payment.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.meli.payment.api.reponse.TotalPaymentResponse;
import com.meli.payment.api.request.PaymentEvent;
import com.meli.payment.model.Payment;
import com.meli.payment.model.to.ChargeTO;
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

	@Test
	public void testCreatePaymentExceedsTotalDebt() {
		Integer userId = 1234;
		String currency = "ARS";
		Double amountCharge = 100d;

		List<ChargeTO> chargeTOList = new ArrayList<ChargeTO>();
		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setAmount(amountCharge);
		chargeTO.setAmountPending(amountCharge);
		chargeTO.setId("id1");
		chargeTOList.add(chargeTO);
		
		chargeTO = new ChargeTO();
		chargeTO.setAmount(amountCharge);
		chargeTO.setAmountPending(amountCharge);
		chargeTO.setId("id2");
		chargeTOList.add(chargeTO);

		Double amountPayment = 250d;

		when(chargeService.getPendingCharges(userId)).thenReturn(chargeTOList);
		when(paymentRepo.findByIdempKey(ArgumentMatchers.any(String.class))).thenReturn(Collections.emptyList());
		when(currencyService.convertToCurrencyDefault(currency, amountPayment)).thenReturn(amountPayment);
		doNothing().when(queueChargeService).enqueuePayment(ArgumentMatchers.any(Payment.class));

		when(paymentRepo.insert(ArgumentMatchers.any(Payment.class))).thenAnswer(new Answer<Payment>() {
		    public Payment answer(InvocationOnMock invocation) {
		        return (Payment)invocation.getArguments()[0];
		    }
		});
	
		PaymentEvent paymentEvt = new PaymentEvent(amountPayment, currency, userId);

		Payment paymentCreated = paymentService.createPayment(paymentEvt, "alalklakslk");
		Double totalMountUsed = paymentCreated.getCharges().stream().map(ch -> ch.getAmountUsed()).reduce(0d, (a1,a2) -> a1+a2);
		Assert.assertEquals(paymentCreated.getAvailableAmount(), (Double)(amountPayment - totalMountUsed));
	}

	@Test
	public void testCreatePaymentCancelTotalDebt() {
		Integer userId = 1234;
		Double totalDebt = 200d;
		String currency = "ARS";
		double amountCharge = 100d;

		List<ChargeTO> chargeTOList = new ArrayList<ChargeTO>();
		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setAmount(amountCharge);
		chargeTO.setAmountPending(amountCharge);
		chargeTO.setId("id1");
		chargeTOList.add(chargeTO);
		
		chargeTO = new ChargeTO();
		chargeTO.setAmount(amountCharge);
		chargeTO.setAmountPending(amountCharge);
		chargeTO.setId("id2");
		chargeTOList.add(chargeTO);

		when(chargeService.getPendingCharges(userId)).thenReturn(chargeTOList);
		when(paymentRepo.findByIdempKey(ArgumentMatchers.any(String.class))).thenReturn(Collections.emptyList());
		when(currencyService.convertToCurrencyDefault(currency, totalDebt)).thenReturn(totalDebt);
		doNothing().when(queueChargeService).enqueuePayment(ArgumentMatchers.any(Payment.class));

		when(paymentRepo.insert(ArgumentMatchers.any(Payment.class))).thenAnswer(new Answer<Payment>() {
		    public Payment answer(InvocationOnMock invocation) {
		        return (Payment)invocation.getArguments()[0];
		    }
		});
	
		PaymentEvent paymentEvt = new PaymentEvent(totalDebt, currency, userId);

		Payment paymentCreated = paymentService.createPayment(paymentEvt, "alalklakslk");
		Double totalMountUsed = paymentCreated.getCharges().stream().map(ch -> ch.getAmountUsed()).reduce(0d, (a1,a2) -> a1+a2);
		Assert.assertEquals(totalDebt, totalMountUsed);
		Assert.assertEquals(paymentCreated.getAvailableAmount(), new Double(0));
		Assert.assertEquals(chargeTOList.size(), paymentCreated.getCharges().size());
	}

	@Test
	public void testCreatePaymentCancelParcialDebt() {
		Integer userId = 1234;
		String currency = "ARS";
		double amountCharge = 100d;

		List<ChargeTO> chargeTOList = new ArrayList<ChargeTO>();
		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setAmount(amountCharge);
		chargeTO.setAmountPending(amountCharge);
		chargeTO.setId("id1");
		chargeTOList.add(chargeTO);
		
		chargeTO = new ChargeTO();
		chargeTO.setAmount(amountCharge);
		chargeTO.setAmountPending(amountCharge);
		chargeTO.setId("id2");
		chargeTOList.add(chargeTO);
		Double amountPayment = 150d;

		when(chargeService.getPendingCharges(userId)).thenReturn(chargeTOList);
		when(paymentRepo.findByIdempKey(ArgumentMatchers.any(String.class))).thenReturn(Collections.emptyList());
		when(currencyService.convertToCurrencyDefault(currency, amountPayment)).thenReturn(amountPayment);
		doNothing().when(queueChargeService).enqueuePayment(ArgumentMatchers.any(Payment.class));

		when(paymentRepo.insert(ArgumentMatchers.any(Payment.class))).thenAnswer(new Answer<Payment>() {
		    public Payment answer(InvocationOnMock invocation) {
		        return (Payment)invocation.getArguments()[0];
		    }
		});
	
		PaymentEvent paymentEvt = new PaymentEvent(amountPayment, currency, userId);

		Payment paymentCreated = paymentService.createPayment(paymentEvt, "alalklakslk");
		Double totalMountUsed = paymentCreated.getCharges().stream().map(ch -> ch.getAmountUsed()).reduce(0d, (a1,a2) -> a1+a2);
		Assert.assertEquals(amountPayment, totalMountUsed);
		Assert.assertEquals(chargeTOList.size(), paymentCreated.getCharges().size());
	}

	@Test
	public void testTotalPaymentInfo() {
		Integer userId = 12345;

		List<Payment> paymentsResult = new ArrayList<Payment>();
		Payment payment1 = new Payment();
		payment1.setDateObj(new Date());
		payment1.setAmount(100d);
		paymentsResult.add(payment1);
		
		Payment payment2 = new Payment();
		payment2.setAmount(350d);
		payment2.setDateObj(new Date());
		paymentsResult.add(payment2);

		when(paymentRepo.findByUserId(userId)).thenReturn(paymentsResult);
		Double totalExpected = paymentsResult.stream().map(p -> p.getAmount()).reduce(0d, (p1,p2) -> p1+p2);

		TotalPaymentResponse totalPaymentInfo = paymentService.totalPaymentInfo(userId);

		Assert.assertEquals(totalPaymentInfo.getTotalPayment(), totalExpected);
		Assert.assertEquals(totalPaymentInfo.getPaymentCount(), (Integer)paymentsResult.size());
		Assert.assertEquals(totalPaymentInfo.getLastPayment(), payment2.getDateObj());
		
	}

}
