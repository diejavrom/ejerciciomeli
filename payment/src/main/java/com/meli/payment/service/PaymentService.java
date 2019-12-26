package com.meli.payment.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meli.payment.DateHelper;
import com.meli.payment.api.reponse.TotalPaymentResponse;
import com.meli.payment.exception.ParamMandatoryException;
import com.meli.payment.exception.PaymentAlreadyProcessedException;
import com.meli.payment.exception.PaymentExceedsTotalDebtException;
import com.meli.payment.model.Payment;
import com.meli.payment.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepo;
	
	@Autowired
	private QueueChargeService queueService;
	
	@Autowired
	private ChargeService chargeService;

	@Autowired
	private CurrencyService currencyService;

	public String createPayment(Payment payment) {

		checkPayment(payment);

		Double originalAmount = payment.getAmount();
		Double amountInCurrencyDefault = currencyService.convertToCurrencyDefault(payment.getCurrency(), originalAmount);

		checkDebtTotal(payment.getUserId(), amountInCurrencyDefault);

		payment.setAmount(amountInCurrencyDefault);
		payment.setOriginalAmount(originalAmount);
		payment.setDateObj(new Date(System.currentTimeMillis()));
		Payment paymentInserted = paymentRepo.insert(payment);

		queueService.enqueuePayment(paymentInserted);
		
		return paymentInserted.getId();
	}

	private void checkDebtTotal(Integer userId, Double amountInCurrencyDefault) {
		Double totalDebt = chargeService.getTotalCharge(userId);

		if(amountInCurrencyDefault > totalDebt) {
			throw new PaymentExceedsTotalDebtException(String.format("El pago con monto '%1$,.2f' excede la deuda del usuario '%2$,.2f'", amountInCurrencyDefault, totalDebt));
		}
	}

	private void checkPayment(Payment payment) {
		if(payment.getAmount() == null || payment.getAmount() <= 0) {
			throw new ParamMandatoryException("amount no puede ser null ni menor o igual que cero");
		}
		if(payment.getCurrency() == null) {
			throw new ParamMandatoryException("currency no puede ser null");
		}
		if(payment.getUserId() == null) {
			throw new ParamMandatoryException("user_id no puede ser null");
		}
		
		if(paymentRepo.existsById(payment.getId())) {
			throw new PaymentAlreadyProcessedException(String.format("Ya existe un pago con ID '%s'", payment.getId()));
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