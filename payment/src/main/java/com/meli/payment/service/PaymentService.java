package com.meli.payment.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meli.payment.DateHelper;
import com.meli.payment.api.reponse.TotalPaymentResponse;
import com.meli.payment.api.request.PaymentEvent;
import com.meli.payment.exception.ParamMandatoryException;
import com.meli.payment.exception.PaymentAlreadyProcessedException;
import com.meli.payment.exception.PaymentExceedsTotalDebtException;
import com.meli.payment.model.Charge;
import com.meli.payment.model.Payment;
import com.meli.payment.model.to.ChargeTO;
import com.meli.payment.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepo;
	
	@Autowired
	private QueueChargeService queueChargeService;
	
	@Autowired
	private ChargeService chargeService;

	@Autowired
	private CurrencyService currencyService;

	public Payment createPayment(PaymentEvent paymentEvt, String idempKey) {

		checkPaymentEvt(paymentEvt, idempKey);

		Double originalAmount = paymentEvt.getAmount();
		Double amountInCurrencyDefault = currencyService.convertToCurrencyDefault(paymentEvt.getCurrency(), originalAmount);

		List<ChargeTO> pendingCharges = chargeService.getPendingCharges(paymentEvt.getUser_id());
		Double totalDebt = pendingCharges.stream().map(c -> c.getAmountPending()).reduce(0d, (v1,v2) -> v1+v2 ).doubleValue();
		checkDebtTotal(totalDebt, amountInCurrencyDefault);
	
		Payment payment = new Payment(paymentEvt, amountInCurrencyDefault, idempKey);
		Double pagoAmount = payment.getAmount();
		for(ChargeTO chargeTO : pendingCharges) {
			if(pagoAmount > 0) {
				Double amountToUSe = Math.min(pagoAmount, chargeTO.getAmountPending());
				pagoAmount = pagoAmount - amountToUSe;
				payment.getCharges().add(new Charge(chargeTO, amountToUSe));
			}
		}

		Payment paymentInserted = paymentRepo.insert(payment);

		queueChargeService.enqueuePayment(paymentInserted);

		return paymentInserted;
	}

	private void checkDebtTotal(Double totalDebt, Double amountInCurrencyDefault) {
		if(amountInCurrencyDefault > totalDebt) {
			throw new PaymentExceedsTotalDebtException(String.format("El pago con monto '%1$,.2f' excede la deuda del usuario '%2$,.2f'", amountInCurrencyDefault, totalDebt));
		}
	}

	private void checkPaymentEvt(PaymentEvent paymentEvt, String idempKey) {
		if(paymentEvt.getAmount() == null || paymentEvt.getAmount() <= 0) {
			throw new ParamMandatoryException("amount no puede ser null ni menor o igual que cero");
		}
		if(paymentEvt.getCurrency() == null) {
			throw new ParamMandatoryException("currency no puede ser null");
		}
		if(paymentEvt.getUser_id() == null) {
			throw new ParamMandatoryException("user_id no puede ser null");
		}
		if(!paymentRepo.findByIdempKey(idempKey).isEmpty()) {
			throw new PaymentAlreadyProcessedException(String.format("El pago con identificación '%s' ya fue procesado", idempKey));
		}
	}

	public List<Payment> listByUserId(Integer user_id) {
		return paymentRepo.findByUserId(user_id);
	}

	public TotalPaymentResponse totalPaymentInfo(Integer user_id) {
		double totalPayment = 0d;
		Date lastPayment = null;
		List<Payment> allPayments = paymentRepo.findByUserId(user_id);
		if(!allPayments.isEmpty()) {
			totalPayment = allPayments.stream().map(c -> c.getAmount()).reduce(0d, (ap1 , ap2) -> ap1 + ap2).doubleValue();
			lastPayment = allPayments.stream().map(c -> c.getDateObj()).reduce(DateHelper.getInstance().getMinTimestamp(), (ap1 , ap2) -> DateHelper.getInstance().max(ap1, ap2));
		}
		return new TotalPaymentResponse(user_id, totalPayment, lastPayment, allPayments.size());
	}

}