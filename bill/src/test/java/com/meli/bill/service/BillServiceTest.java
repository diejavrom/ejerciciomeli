package com.meli.bill.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
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

import com.meli.bill.DateHelper;
import com.meli.bill.exception.ChargeAndPayAlreadyProccessedException;
import com.meli.bill.exception.ParamMandatoryException;
import com.meli.bill.model.Bill;
import com.meli.bill.model.Charge;
import com.meli.bill.model.to.ChargeTO;
import com.meli.bill.repository.BillRepository;


@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = BillService.class)
public class BillServiceTest {

	@Autowired
	@InjectMocks
	private BillService billService;
	
	@Mock
	private BillRepository billRepo;
	
	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testGetBillFound() {
		Integer userId = 1234;
		Integer m = 1;
		Integer y = 2019;
		Bill bill = new Bill();
		bill.setId("123456xxxx");
		bill.setMonth(m);
		bill.setYear(y);

		when(billRepo.findByUserIdMonthYear(userId, m, y)).thenReturn(Collections.singletonList(bill));
		
		Bill billRecovery = billService.getBillByUserMonthYear(userId, m, y);
		Assert.assertEquals(billRecovery, bill);
	}

	@Test
	public void testGetBillNotFound() {
		Integer userId = 1234;
		Integer m = 1;
		Integer y = 2019;

		when(billRepo.findByUserIdMonthYear(userId, m, y)).thenReturn(Collections.emptyList());

		Bill billRecovery = billService.getBillByUserMonthYear(userId, m, y);
		Assert.assertNull(billRecovery);
	}

	@Test(expected = IllegalStateException.class)
	public void testMoreThanOneBillFound() {
		Integer userId = 1234;
		Integer m = 1;
		Integer y = 2019;

		List<Bill> bills = new ArrayList<Bill>();
		Bill bill = new Bill();
		bill.setId("123456xxxx");
		bill.setMonth(m);
		bill.setYear(y);
		bills.add(bill);

		Bill bill1 = new Bill();
		bill1.setId("123456xxxxzzz");
		bill1.setMonth(m);
		bill1.setYear(y);
		bills.add(bill1);

		when(billRepo.findByUserIdMonthYear(userId, m, y)).thenReturn(bills);
		
		billService.getBillByUserMonthYear(userId, m, y);
		
	}

	@Test(expected = ChargeAndPayAlreadyProccessedException.class)
	public void testAddExistingCharge() {
		Integer userId = 1234;
		Integer m = 12;
		Integer y = 2019;
		Double amountCharge = 100d;
		Bill billReturn = new Bill();
		billReturn.setMonth(m);
		billReturn.setYear(y);
		billReturn.setUserId(userId);

		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setDateObj(DateHelper.getInstance().stringToTimestamp("2019-12-16T00:00:00"));
		chargeTO.setEvent_id(1234);
		chargeTO.setAmount(amountCharge);
		chargeTO.setUserId(userId);
		chargeTO.setAmountPending(amountCharge - amountCharge/2);
		chargeTO.setId("dskjhdskjshdl");
		chargeTO.setPaymentId("asasas");
		chargeTO.setPaymentAmount(50d);

		billReturn.updateCharge(new Charge(chargeTO));

		when(billRepo.findByUserIdMonthYear(userId, m, y)).thenReturn(Collections.singletonList(billReturn));
		when(billRepo.save(ArgumentMatchers.any(Bill.class))).thenReturn(billReturn);

		billService.addChargeToBill(chargeTO);
	}

	@Test
	public void testAddChargeTOExistingBill() {
		Integer userId = 1234;
		Integer m = 12;
		Integer y = 2019;
		Double amountCharge = 100d;

		Bill bill = new Bill();
		bill.setMonth(m);
		bill.setYear(y);
		bill.setUserId(userId);

		ChargeTO chargeTO = new ChargeTO();
		chargeTO.setDateObj(DateHelper.getInstance().stringToTimestamp("2019-12-16T00:00:00"));
		chargeTO.setEvent_id(1234);
		chargeTO.setAmount(amountCharge);
		chargeTO.setUserId(userId);
		chargeTO.setAmountPending(amountCharge - amountCharge/2);
		chargeTO.setId("dskjhdskjshdl");

		when(billRepo.findByUserIdMonthYear(userId, m, y)).thenReturn(Collections.singletonList(bill));
		when(billRepo.save(ArgumentMatchers.any(Bill.class))).thenAnswer(new Answer<Bill>() {
		    public Bill answer(InvocationOnMock invocation) {
		        return (Bill)invocation.getArguments()[0];
		    }
		});

		Bill billPersisted = billService.addChargeToBill(chargeTO);
		Assert.assertEquals(billPersisted.getAmount(), amountCharge);
		Assert.assertEquals(billPersisted.getPendingAmount(), (Double)(amountCharge/2));
	}

	@Test(expected = ParamMandatoryException.class)
	public void testAddChargeWithNullParam() {
		billService.addChargeToBill(null);
	}

}