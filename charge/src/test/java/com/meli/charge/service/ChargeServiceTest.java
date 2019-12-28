package com.meli.charge.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.meli.charge.api.response.TotalChargeInfoResponse;
import com.meli.charge.api.response.TotalPendingChargeResponse;
import com.meli.charge.exception.ChargeOutOfDateException;
import com.meli.charge.exception.PaymentExceedsTotalDebtException;
import com.meli.charge.model.Charge;
import com.meli.charge.model.ChargeType;
import com.meli.charge.model.Payment;
import com.meli.charge.model.to.ChargeTO;
import com.meli.charge.model.to.PaymentTO;
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

		ChargeType chargeType = new ChargeType(category, eventType);

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
		Assert.assertEquals(event_id, createdCharge.getEventId());
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

		PaymentTO payment = new PaymentTO();
		payment.setAmount(amount);
		payment.setUserId(userId);
	
		ChargeEvent chargeEvt = new BuilderEvtCharge()
				.withAmount(amount - 10)
			    .withCurrency(currency)
			    .withDate("2019-12-16T03:00:00.000+0000")
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();
		
		ChargeType chargeType = new ChargeType("SERVICIOS", eventType);

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

		PaymentTO payment = new PaymentTO();
		payment.setId("ksaklasklaslk");
		payment.setAmount(amount);
		payment.setUserId(userId);

		String idCharge = "jkl";
		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setAmountUsed(100d);
		chargeTO.setId(idCharge);
		payment.getCharges().add(chargeTO);

		ChargeEvent chargeEvt = new BuilderEvtCharge()
				.withAmount(2*amount)
			    .withCurrency(currency)
			    .withDate("2019-12-16T03:00:00.000+0000")
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();

		ChargeType chargeType = new ChargeType("SERVICIOS", eventType);

		Charge charge = new Charge(chargeEvt, chargeEvt.getAmount(), chargeType);
		charge.setId(idCharge);

		when(chargeRepo.findById(idCharge)).thenReturn(Optional.of(charge));
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
	}

	@Test
	public void testTotalChargeInfo() {
		Integer userId = 12345;
		String currency = "ARS";
		Double amount = 100d;
		Integer event_id = 1234;
		String eventType = "PUBLICIDAD";

		ChargeEvent chargeEvt = new BuilderEvtCharge()
				.withAmount(2*amount)
			    .withCurrency(currency)
			    .withDate("2019-12-16T03:00:00.000+0000")
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();

		ChargeType chargeType = new ChargeType("SERVICIOS", eventType);

		List<Charge> chargeResult = new ArrayList<Charge>();
		Charge charge = new Charge(chargeEvt, chargeEvt.getAmount(), chargeType);
		chargeResult.add(charge);

		chargeEvt = new BuilderEvtCharge()
				.withAmount(2*amount)
			    .withCurrency(currency)
			    .withDate("2019-12-18T03:00:00.000+0000")
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();

		Charge charge2 = new Charge(chargeEvt, chargeEvt.getAmount()*3, chargeType);
		chargeResult.add(charge2);

		Double totalExpected = chargeResult.stream().map(ch -> ch.getAmount()).reduce(0d, (c1,c2) -> c1+c2);

		when(chargeRepo.findByUserId(userId)).thenReturn(chargeResult);

		TotalChargeInfoResponse totalChargeInfo = chargeService.totalChargeInfo(userId);

		Assert.assertEquals(totalChargeInfo.getTotalCharge(), totalExpected);
		Assert.assertEquals(totalChargeInfo.getChargesCount(), (Integer)chargeResult.size());
		Assert.assertEquals(totalChargeInfo.getLastCharge(), charge2.getDateObj());
		
	}

	@Test
	public void testTotalChargeWithDebtInfo() {
		Integer userId = 12345;
		String currency = "ARS";
		Double amount = 100d;
		Integer event_id = 1234;
		String eventType = "PUBLICIDAD";

		ChargeEvent chargeEvt = new BuilderEvtCharge()
				.withAmount(2*amount)
			    .withCurrency(currency)
			    .withDate("2019-12-16T03:00:00.000+0000")
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();

		ChargeType chargeType = new ChargeType("SERVICIOS", eventType);

		List<Charge> chargeResult = new ArrayList<Charge>();
		Charge charge = new Charge(chargeEvt, chargeEvt.getAmount(), chargeType);
		chargeResult.add(charge);

		chargeEvt = new BuilderEvtCharge()
				.withAmount(2*amount)
			    .withCurrency(currency)
			    .withDate("2019-12-18T03:00:00.000+0000")
			    .withEvent_id(event_id)
			    .withUserId(userId)
			    .withEvent_type(eventType)
			    .build();

		Charge charge2 = new Charge(chargeEvt, chargeEvt.getAmount()*3, chargeType);
		chargeResult.add(charge2);

		Double totalExpected = chargeResult.stream().map(ch -> ch.getAmountPending()).reduce(0d, (c1,c2) -> c1+c2);

		when(chargeRepo.findAllWithDebt(userId)).thenReturn(chargeResult);

		TotalPendingChargeResponse totalChargeInfo = chargeService.totalChargeAmountPending(userId);

		Assert.assertEquals(totalChargeInfo.getTotalPendingCharge(), totalExpected);
		
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
			ce.setUser_id(userId);
			ce.setEvent_type(event_type);
			return ce;
		}
		
	}
}