package com.meli.charge.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;

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

import com.meli.charge.api.request.ChargeEvent;
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
	
}