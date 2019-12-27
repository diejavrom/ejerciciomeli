package com.meli.charge.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.meli.charge.DateHelper;
import com.meli.charge.api.request.ChargeEvent;
import com.meli.charge.exception.ChargeOutOfDateException;
import com.meli.charge.exception.PaymentExceedsTotalDebtException;
import com.meli.charge.model.Charge;
import com.meli.charge.model.ChargeType;
import com.meli.charge.model.Payment;
import com.meli.charge.repository.ChargeRepository;
import com.meli.charge.repository.ChargeTypeRepository;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ChargeServiceTest.class)
public class ChargeServiceTest {

	@Autowired
	@InjectMocks
	private ChargeService chargeService;

	@Mock
	private ChargeRepository chargeRepo;

	@Mock
	private ChargeTypeRepository chargeTypeRepo;

	@Mock
	private QueueBillService queueBillService;
	
	@Mock
	private CurrencyService currencyService;

	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testCreateChargeFromEvent() {
		ChargeEvent chargeEvt = new ChargeEvent();
		Double amount = 100d;
		chargeEvt.setAmount(amount);
		String currency = "ARS";
		chargeEvt.setCurrency(currency);
		chargeEvt.setDate("2019-12-16T03:00:00.000+0000");
		Integer event_id = 1234;
		chargeEvt.setEvent_id(event_id);
		Integer userId = 12223;
		chargeEvt.setUserId(userId);
		String eventType = "PUBLICIDAD";
		chargeEvt.setEvent_type(eventType);

		ChargeType chargeType = new ChargeType();
		chargeType.setType(eventType);
		String category = "SERVICIOS";
		chargeType.setCategory(category);

		when(chargeRepo.insert(ArgumentMatchers.any(Charge.class))).thenAnswer(new Answer<Charge>() {
		    public Charge answer(InvocationOnMock invocation) {
		        return (Charge)invocation.getArguments()[0];
		    }
		});

		when(currencyService.convertToCurrencyDefault(currency, amount)).thenReturn(amount);
		when(chargeTypeRepo.findByType(ArgumentMatchers.anyString())).thenReturn(Collections.singletonList(chargeType));

		doNothing().when(queueBillService).enqueueCharge(ArgumentMatchers.any(Charge.class), ArgumentMatchers.any(Payment.class), ArgumentMatchers.any(Double.class));

		Charge createdCharge = chargeService.createCharge(chargeEvt);

		Assert.assertEquals(amount, createdCharge.getAmount());
		Assert.assertEquals(amount, createdCharge.getAmountPending());
		Assert.assertEquals(currency, createdCharge.getCurrency());
		Assert.assertEquals(category, createdCharge.getCategory());
		Assert.assertEquals(eventType, createdCharge.getEvent_type());
		Assert.assertEquals(event_id, createdCharge.getEvent_id());
		Assert.assertEquals(userId, createdCharge.getUserId());
			
	}

	@Test(expected = ChargeOutOfDateException.class)
	public void testCreateChargeFromEventOutOfDate() {
		ChargeEvent chargeEvt = new ChargeEvent();
		Double amount = 100d;
		chargeEvt.setAmount(amount);
		String currency = "ARS";
		chargeEvt.setCurrency(currency);
	
		Date dateOutOfRange = new Date(DateHelper.getInstance().getNow().getTime() + DateHelper.ONE_DAY*30);
		chargeEvt.setDate(DateHelper.getInstance().dateToString(dateOutOfRange));
		
		Integer event_id = 1234;
		chargeEvt.setEvent_id(event_id);
		Integer userId = 12223;
		chargeEvt.setUserId(userId);
		String eventType = "PUBLICIDAD";
		chargeEvt.setEvent_type(eventType);

		ChargeType chargeType = new ChargeType();
		chargeType.setType(eventType);
		String category = "SERVICIOS";
		chargeType.setCategory(category);

		chargeService.createCharge(chargeEvt);
	}

	@Test(expected = PaymentExceedsTotalDebtException.class)
	public void testReceivePaymentExceedsDebt() {
		Integer userId = 289;
		String currency = "ARS";
		double amount = 100d;

		Payment payment = new Payment();
		payment.setAmount(amount);
		payment.setUserId(userId);
		payment.setCurrency(currency);
	
		ChargeEvent chargeEvt = new ChargeEvent();
		chargeEvt.setAmount(amount - 10);
		chargeEvt.setCurrency(currency);
		chargeEvt.setDate("2019-12-16T03:00:00.000+0000");
		Integer event_id = 1234;
		chargeEvt.setEvent_id(event_id);
		chargeEvt.setUserId(userId);
		String eventType = "PUBLICIDAD";
		chargeEvt.setEvent_type(eventType);

		ChargeType chargeType = new ChargeType();
		chargeType.setType(eventType);
		String category = "SERVICIOS";
		chargeType.setCategory(category);

		Charge charge = new Charge(chargeEvt, chargeEvt.getAmount(), chargeType);

		when(chargeRepo.findAllWithDebt(userId)).thenReturn(Collections.singletonList(charge));

		chargeService.payChargesWithPayment(payment);
	}

	@Test
	public void testReceivePaymentOk() {
		Integer userId = 289;
		String currency = "ARS";
		Double amount = 100d;

		Payment payment = new Payment();
		payment.setId("ksaklasklaslk");
		payment.setAmount(amount);
		payment.setUserId(userId);
		payment.setCurrency(currency);
	
		ChargeEvent chargeEvt = new ChargeEvent();
		chargeEvt.setAmount(2*amount);
		chargeEvt.setCurrency(currency);
		chargeEvt.setDate("2019-12-16T03:00:00.000+0000");
		Integer event_id = 1234;
		chargeEvt.setEvent_id(event_id);
		chargeEvt.setUserId(userId);
		String eventType = "PUBLICIDAD";
		chargeEvt.setEvent_type(eventType);

		ChargeType chargeType = new ChargeType();
		chargeType.setType(eventType);
		String category = "SERVICIOS";
		chargeType.setCategory(category);

		Charge charge = new Charge(chargeEvt, chargeEvt.getAmount(), chargeType);

		when(chargeRepo.findAllWithDebt(userId)).thenReturn(Collections.singletonList(charge));
		when(chargeRepo.save(ArgumentMatchers.any(Charge.class))).thenAnswer(new Answer<Charge>() {
		    public Charge answer(InvocationOnMock invocation) {
		        return (Charge)invocation.getArguments()[0];
		    }
		});
		doNothing().when(queueBillService).enqueueCharge(ArgumentMatchers.any(Charge.class), ArgumentMatchers.any(Payment.class), ArgumentMatchers.any(Double.class));

		List<Charge> result = chargeService.payChargesWithPayment(payment);
		Charge chargeResult = result.iterator().next();

		Assert.assertEquals(chargeResult.getAmountPending(), amount);
		Assert.assertTrue(chargeResult.getPayments().contains(payment));
	}

}