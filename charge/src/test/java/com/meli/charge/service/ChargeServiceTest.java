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
	public void testCreateChargeFromEventOk() {
		Double amount = 100d;
		String currency = "ARS";
		Integer event_id = 1234;
		Integer userId = 12223;
		String eventType = "PUBLICIDAD";
		String category = "SERVICIOS";

		ChargeEvent chargeEvt = new BuilderEvtCharge()
			.withAmount(amount)
		    .withCurrency(currency)
		    .withDate("2019-12-16T03:00:00.000+0000")
		    .withEvent_id(event_id)
		    .withUserId(userId)
		    .withEvent_type(eventType)
		    .build();

		ChargeType chargeType = new ChargeType();
		chargeType.setType(eventType);
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
		Double amount = 100d;
		String currency = "ARS";
		Date dateOutOfRange = new Date(DateHelper.getInstance().getNow().getTime() + DateHelper.ONE_DAY*30);
		String date = DateHelper.getInstance().dateToString(dateOutOfRange);
		Integer event_id = 1234;
		Integer userId = 12223;
		String eventType = "PUBLICIDAD";
		
		ChargeEvent chargeEvt = new BuilderEvtCharge()
				.withAmount(amount)
			    .withCurrency(currency)
			    .withDate(date)
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();
		
		chargeService.createCharge(chargeEvt);
	}

	@Test(expected = PaymentExceedsTotalDebtException.class)
	public void testReceivePaymentExceedsDebt() {
		Integer userId = 289;
		String currency = "ARS";
		double amount = 100d;
		Integer event_id = 1234;
		String eventType = "PUBLICIDAD";

		Payment payment = new Payment();
		payment.setAmount(amount);
		payment.setUserId(userId);
		payment.setCurrency(currency);
	
		ChargeEvent chargeEvt = new BuilderEvtCharge()
				.withAmount(amount - 10)
			    .withCurrency(currency)
			    .withDate("2019-12-16T03:00:00.000+0000")
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();
		
		ChargeType chargeType = new ChargeType();
		chargeType.setType(eventType);
		chargeType.setCategory("SERVICIOS");

		Charge charge = new Charge(chargeEvt, chargeEvt.getAmount(), chargeType);

		when(chargeRepo.findAllWithDebt(userId)).thenReturn(Collections.singletonList(charge));

		chargeService.payChargesWithPayment(payment);
	}

	@Test
	public void testReceivePaymentOk() {
		Integer userId = 289;
		String currency = "ARS";
		Double amount = 100d;
		Integer event_id = 1234;
		String eventType = "PUBLICIDAD";

		Payment payment = new Payment();
		payment.setId("ksaklasklaslk");
		payment.setAmount(amount);
		payment.setUserId(userId);
		payment.setCurrency(currency);

		ChargeEvent chargeEvt = new BuilderEvtCharge()
				.withAmount(2*amount)
			    .withCurrency(currency)
			    .withDate("2019-12-16T03:00:00.000+0000")
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();

		ChargeType chargeType = new ChargeType();
		chargeType.setType(eventType);
		chargeType.setCategory("SERVICIOS");

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


	private static class BuilderEvtCharge {
		
		private Double amount;
		private String currency;
		private String date;
		private Integer event_id;
		private Integer userId;
		private String event_type;
		
		public BuilderEvtCharge() {
		}

		public BuilderEvtCharge withAmount(Double amount) {
			this.amount = amount;
			return this;
		}
		
		public BuilderEvtCharge withCurrency(String currency) {
			this.currency = currency;
			return this;
		}
		
		public BuilderEvtCharge withDate(String date) {
			this.date = date;
			return this;
		}

		public BuilderEvtCharge withEvent_id(Integer event_id) {
			this.event_id = event_id;
			return this;
		}

		public BuilderEvtCharge withUserId(Integer userId) {
			this.userId = userId;
			return this;
		}

		public BuilderEvtCharge withEvent_type(String event_type) {
			this.event_type = event_type;
			return this;
		}

		public ChargeEvent build() {
			ChargeEvent ce = new ChargeEvent();
			ce.setAmount(amount);
			ce.setCurrency(currency);
			ce.setDate(date);
			ce.setEvent_id(event_id);
			ce.setUserId(userId);
			ce.setEvent_type(event_type);
			return ce;
		}
		
	}
}