package com.meli.charge.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.meli.charge.DateHelper;
import com.meli.charge.api.request.ChargeEvent;
import com.meli.charge.api.response.TotalChargeInfoResponse;
import com.meli.charge.api.response.TotalAmountPendingChargeResponse;
import com.meli.charge.exception.ChargeAlreadyProcessedException;
import com.meli.charge.exception.ChargeOutOfDateException;
import com.meli.charge.exception.ParamMandatoryException;
import com.meli.charge.exception.PaymentExceedsTotalDebtException;
import com.meli.charge.model.Charge;
import com.meli.charge.model.Payment;
import com.meli.charge.model.to.ChargeTO;
import com.meli.charge.model.to.PaymentTO;
import com.meli.charge.repository.ChargeRepository;

@Service
public class ChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeService.class);

	@Autowired
	private ChargeRepository chargeRepo;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private QueueBillService queueBillService;

	public Charge createCharge(ChargeEvent chargeEvt) {

		LOGGER.info("Procesando cargo para el usuario -> {},  event_id -> {}, currency -> {}, amount -> {} ", chargeEvt.getUser_id(), chargeEvt.getEvent_id(), chargeEvt.getCurrency(), chargeEvt.getAmount());

		checkEventCharge(chargeEvt);

		checkEventChargeOutOfDate(chargeEvt);
	
		Double amountInDefCurrency = currencyService.convertToCurrencyDefault(chargeEvt.getCurrency(), chargeEvt.getAmount());

		Charge charge = new Charge(chargeEvt, amountInDefCurrency);

		Charge chargePersisted = chargeRepo.insert(charge);

		queueBillService.enqueueCharge(chargePersisted, null, null);

		return chargePersisted;
	}

	private void checkEventCharge(ChargeEvent evento) {
		if(evento == null) {
			throw new ParamMandatoryException("evento no puede ser null");
		}
		if(evento.getEvent_id() == null) {
			throw new ParamMandatoryException("event_id no puede ser null");
		}
		if(evento.getUser_id() == null) {
			throw new ParamMandatoryException("user_id no puede ser null");
		}
		if(evento.getEvent_type() == null || evento.getEvent_type().trim().isEmpty()) {
			throw new ParamMandatoryException("event_type no puede ser null");
		}
		if(evento.getCurrency() == null || evento.getCurrency().trim().isEmpty()) {
			throw new ParamMandatoryException("currency no puede ser null o vacío");
		}
		if(evento.getAmount() == null || evento.getAmount() <= 0) {
			throw new ParamMandatoryException("amount no puede ser null o vacío");
		}
		if(evento.getDate() == null || evento.getDate().trim().isEmpty()) {
			throw new ParamMandatoryException("date no puede ser vacío");
		}
		
		if(!chargeRepo.findByEventId(evento.getEvent_id()).isEmpty()) {
			throw new ChargeAlreadyProcessedException(String.format("El evento de cargo con ID '%d' ya fue procesado anteriormente", evento.getEvent_id()));
		}
	}

	private void checkEventChargeOutOfDate(ChargeEvent evento) {
		Timestamp dateEvento = DateHelper.getInstance().stringToTimestamp(evento.getDate());
		Timestamp now = DateHelper.getInstance().getNow();
		Integer mesEvt = DateHelper.getInstance().getMonth(dateEvento);
		Integer anioEvt = DateHelper.getInstance().getYear(dateEvento);
		Integer mesAct = DateHelper.getInstance().getMonth(now);
		Integer anioAct = DateHelper.getInstance().getYear(now);
		if(!mesEvt.equals(mesAct) || !anioEvt.equals(anioAct)) {
			throw new ChargeOutOfDateException(String.format("El cargo no corresponde al mes en curso: '%s'", evento.getDate()));
		} else if(now.before(dateEvento)) {
			throw new ChargeOutOfDateException(String.format("El cargo tiene una fecha futura: '%s'", evento.getDate()));
		}
	}

	public List<Charge> listByUserId(Integer user_id) {
		return chargeRepo.findByUserId(user_id);
	}

	public List<Charge> payChargesWithPayment(PaymentTO paymentTO) {
		List<Charge> chargesPersistedList = new ArrayList<Charge>();
		TotalAmountPendingChargeResponse totalChargeAmountPending = totalChargeAmountPending(paymentTO.getUserId());
		if(totalChargeAmountPending.getTotalPendingCharge() < paymentTO.getAmount()) {
			throw new PaymentExceedsTotalDebtException(String.format("El pago con monto '%1$,.2f' excede la deuda del usuario '%2$,.2f'", paymentTO.getAmount(), totalChargeAmountPending.getTotalPendingCharge()));
		}

		Payment payment = new Payment(paymentTO.getId(), paymentTO.getAmount(), paymentTO.getUserId());
		for(ChargeTO chargeTO : paymentTO.getCharges()) {
			Charge charge = chargeRepo.findById(chargeTO.getId()).get();
			charge.payAndRelate(payment, chargeTO.getAmountUsed());
			Charge chargePersisted = chargeRepo.save(charge);
			chargesPersistedList.add(chargePersisted);
			queueBillService.enqueueCharge(chargePersisted, payment, chargeTO.getAmountUsed());
		}

		return chargesPersistedList;
	}

	public TotalChargeInfoResponse totalChargeInfo(Integer user_id) {
		List<Charge> allCharge = chargeRepo.findByUserId(user_id);
		double totalCharge = 0d;
		Date lastCharge = null;
		if(!allCharge.isEmpty()) {
			totalCharge = allCharge.stream().map(c -> c.getAmount()).reduce(0d, (ap1 , ap2) -> ap1 + ap2).doubleValue();
			lastCharge = allCharge.stream().map(c -> c.getDateObj()).reduce(DateHelper.getInstance().getMinTimestamp(), (ap1 , ap2) -> DateHelper.getInstance().max(ap1, ap2));
		}
		return new TotalChargeInfoResponse(user_id, totalCharge, lastCharge, allCharge.size());
	}

	public TotalAmountPendingChargeResponse totalChargeAmountPending(Integer user_id) {
		List<Charge> allCharge = findAllWithDebtSorted(user_id);
		double totalCharge = 0d;
		if(!allCharge.isEmpty()) {
			totalCharge = allCharge.stream().map(c -> c.getAmountPending()).reduce(0d, (ap1 , ap2) -> ap1 + ap2).doubleValue();
		}
		return new TotalAmountPendingChargeResponse(user_id, totalCharge);
	}

	private List<Charge> findAllWithDebtSorted(Integer user_id) {
		return chargeRepo.findAllWithDebt(user_id, Sort.by(Direction.ASC, "dateObj"));
	}

	public List<Charge> listPendingByUserId(Integer user_id) {
		return findAllWithDebtSorted(user_id);
	}

}