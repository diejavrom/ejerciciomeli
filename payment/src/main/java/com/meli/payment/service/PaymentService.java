package com.meli.payment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meli.payment.DateHelper;
import com.meli.payment.api.reponse.TotalPaymentResponse;
import com.meli.payment.api.request.PaymentEvent;
import com.meli.payment.exception.ParamMandatoryException;
import com.meli.payment.exception.PaymentAlreadyProcessedException;
import com.meli.payment.model.Charge;
import com.meli.payment.model.Payment;
import com.meli.payment.model.to.ChargeQueueTO;
import com.meli.payment.model.to.ChargeTO;
import com.meli.payment.model.to.PaymentTO;
import com.meli.payment.repository.PaymentRepository;

/**
 * Servicio encargado de gestionar pagos. 
 */
@Service
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private PaymentRepository paymentRepo;
	
	@Autowired
	private QueueChargeService queueChargeService;
	
	@Autowired
	private ChargeService chargeService;

	@Autowired
	private CurrencyService currencyService;

	/**
	 * Permite crear un pago a partir de un evento. El pago se identifica con <code>idempkey</code>.
	 * @param paymentEvt
	 * @param idempKey
	 * @return el pago creado
	 */
	public Payment createPayment(PaymentEvent paymentEvt, String idempKey) {

		LOGGER.info("Procesando pago para el usuario -> {},  currency -> {}, amount -> {}, idempkey -> {} ", paymentEvt.getUser_id(), paymentEvt.getCurrency(), paymentEvt.getAmount(), idempKey);

		//check paramas + unicidad del pago
		checkPaymentEvt(paymentEvt, idempKey);

		//convierte a la moneda default el monto recibido 
		Double originalAmount = paymentEvt.getAmount();
		Double amountInCurrencyDefault = currencyService.convertToCurrencyDefault(paymentEvt.getCurrency(), originalAmount);

		//se obtiene los cargos con deuda
		List<ChargeTO> pendingCharges = chargeService.getPendingCharges(paymentEvt.getUser_id());
		
		//se agregan los cargos pagados a la lista de cargos del pago
		Payment payment = new Payment(paymentEvt, amountInCurrencyDefault, idempKey);
		Double pagoAmount = payment.getAmount();
		for(ChargeTO chargeTO : pendingCharges) {
			if(pagoAmount > 0) {
				Double amountToUSe = Math.min(pagoAmount, chargeTO.getAmountPending());
				pagoAmount = pagoAmount - amountToUSe;
				payment.payCharge(new Charge(chargeTO, amountToUSe), amountToUSe);
			}
		}

		//se graba el pago
		Payment paymentInserted = paymentRepo.insert(payment);

		//se encola el pago en la cola de cargos
		queueChargeService.enqueuePayment(paymentInserted);

		return paymentInserted;
	}

	/**
	 * Chequea los parámetros del pago y la unicidad del mismo utilizando <code>idempkey</code>
	 * @param paymentEvt
	 * @param idempKey
	 */
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

	/**
	 * Obtiene todos los pagos del usuario
	 * @param user_id
	 * @return la lista de pagos
	 */
	public List<Payment> listByUserId(Integer user_id) {
		
		LOGGER.info("obteniendo lista de pagos del usuario {}", user_id);

		return paymentRepo.findByUserId(user_id);
	}

	/**
	 * Obtiene el resumen de los pagos
	 * @param user_id
	 * @return  el resumen de los pagos
	 */
	public TotalPaymentResponse totalPaymentInfo(Integer user_id) {
		
		LOGGER.info("obteniendo resumen de pagos del usuario {}", user_id);

		double totalPayment = 0d;
		Date lastPayment = null;
		List<Payment> allPayments = paymentRepo.findByUserId(user_id);
		if(!allPayments.isEmpty()) {
			totalPayment = allPayments.stream().map(c -> c.getAmount()).reduce(0d, (ap1 , ap2) -> ap1 + ap2).doubleValue();
			lastPayment = allPayments.stream().map(c -> c.getDateObj()).reduce(DateHelper.getInstance().getMinTimestamp(), (ap1 , ap2) -> DateHelper.getInstance().max(ap1, ap2));
		}
		return new TotalPaymentResponse(user_id, totalPayment, lastPayment, allPayments.size());
	}

	/**
	 * Obtiene la lista de pagos con saldo disponible
	 * @param user_id
	 * @return la lista de pagos con saldo disponible
	 */
	public List<Payment> listPaymentsWithAmountAvailableUserId(Integer user_id) {
		
		LOGGER.info("obteniendo lista de pagos con saldo disponible del usuario {}", user_id);

		return paymentRepo.findPaymentsWithAmountAvailableByUserId(user_id);
	}

	/**
	 * Actualiza los pagos afectados por el cargo
	 * @param chargeTO
	 * @return la lista de pagos afectados
	 */
	public List<Payment> updatePayments(ChargeQueueTO chargeTO) {

		List<Payment> paymentPersisted = new ArrayList<Payment>();

		for(PaymentTO paymentTO : chargeTO.getPayments()) {

			Payment payment = paymentRepo.findById(paymentTO.getId()).get();

			Charge charge = new Charge(chargeTO, paymentTO.getAmountUsed());

			payment.payCharge(charge, paymentTO.getAmountUsed());

			paymentPersisted.add(paymentRepo.save(payment));
		}

		return paymentPersisted;
	}

}