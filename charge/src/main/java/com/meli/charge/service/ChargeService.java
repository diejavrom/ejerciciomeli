package com.meli.charge.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meli.charge.DateHelper;
import com.meli.charge.api.request.ChargeEvent;
import com.meli.charge.api.response.TotalChargeInfoResponse;
import com.meli.charge.api.response.TotalPendingChargeResponse;
import com.meli.charge.exception.ChargeAlreadyProcessedException;
import com.meli.charge.exception.ChargeOutOfDateException;
import com.meli.charge.exception.ChargeTypeException;
import com.meli.charge.exception.ParamMandatoryException;
import com.meli.charge.exception.PaymentExceedsTotalDebtException;
import com.meli.charge.model.Charge;
import com.meli.charge.model.ChargeType;
import com.meli.charge.model.Payment;
import com.meli.charge.repository.ChargeRepository;
import com.meli.charge.repository.ChargeTypeRepository;

@Service
public class ChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeService.class);
	
	@Autowired
	private ChargeRepository chargeRepo;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private ChargeTypeRepository chargeTypeRepo;

	@Autowired
	private QueueBillService queueBillService;

	public Charge createCharge(ChargeEvent chargeEvt) {

		LOGGER.info("Procesando cargo para el usuario -> {},  event_id -> {}, currency -> {}, amount -> {} ", chargeEvt.getUserId(), chargeEvt.getEvent_id(), chargeEvt.getCurrency(), chargeEvt.getAmount());

		checkEventCharge(chargeEvt);

		checkEventChargeOutOfDate(chargeEvt);
	
		Double amountInDefCurrency = currencyService.convertToCurrencyDefault(chargeEvt.getCurrency(), chargeEvt.getAmount());

		Charge charge = new Charge(chargeEvt, amountInDefCurrency, getChargeType(chargeEvt.getEvent_type()));

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
		if(evento.getUserId() == null) {
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
		if(chargeRepo.existsById(evento.getEvent_id())) {
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

	public List<Charge> payChargesWithPayment(Payment payment) {
		List<Charge> chargesPersistedList = new ArrayList<Charge>();
		TotalPendingChargeResponse totalChargeAmountPending = totalChargeAmountPending(payment.getUserId());
		if(totalChargeAmountPending.getTotalPendingCharge() < payment.getAmount()) {
			throw new PaymentExceedsTotalDebtException(String.format("El pago con monto '%1$,.2f' excede la deuda del usuario '%2$,.2f'", payment.getAmount(), totalChargeAmountPending.getTotalPendingCharge()));
		}

		List<Charge> chargeWithDebt = chargeRepo.findAllWithDebt(payment.getUserId());
		List<Charge> chargeListToPersist = new ArrayList<Charge>();
		Double pagoAmount = payment.getAmount();
		for(Charge charge : chargeWithDebt) {
			if(pagoAmount > 0) {
				Double amountToUSe = Math.min(pagoAmount, charge.getAmountPending());
				pagoAmount = pagoAmount - amountToUSe;
				charge.payAndRelate(payment, amountToUSe);
				chargeListToPersist.add(charge);

				Charge chargePersisted = chargeRepo.save(charge);
				chargesPersistedList.add(chargePersisted);
				queueBillService.enqueueCharge(chargePersisted, payment, amountToUSe);
			}
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

	public TotalPendingChargeResponse totalChargeAmountPending(Integer user_id) {
		List<Charge> allCharge = chargeRepo.findAllWithDebt(user_id);
		double totalCharge = 0d;
		if(!allCharge.isEmpty()) {
			totalCharge = allCharge.stream().map(c -> c.getAmountPending()).reduce(0d, (ap1 , ap2) -> ap1 + ap2).doubleValue();
		}
		return new TotalPendingChargeResponse(user_id, totalCharge);
	}

	private ChargeType getChargeType(String type) {
		List<ChargeType> chargeType = chargeTypeRepo.findByType(type);
		if(chargeType.isEmpty()) {
			throw new ChargeTypeException(String.format("No se pudo encontrar un tipo de cargo asociado a %s", type)); 
		} else if(chargeType.size() > 1){
			throw new ChargeTypeException(String.format("Existe más de un tipo de cargo configurado para %s", type)); 
		} else {
			return chargeType.iterator().next();
		}
	}

}