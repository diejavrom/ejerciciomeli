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
import com.meli.charge.api.ParamMandatoryException;
import com.meli.charge.api.response.TotalChargeInfoResponse;
import com.meli.charge.api.response.TotalPendingChargeResponse;
import com.meli.charge.exception.ChargeAlreadyProcessedException;
import com.meli.charge.exception.ChargeOutOfDateException;
import com.meli.charge.model.Charge;
import com.meli.charge.model.Payment;
import com.meli.charge.repository.ChargeRepository;

@Service
public class ChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeService.class);
	
	@Autowired
	private ChargeRepository chargeRepo;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private QueueBillService queueService;

	public Charge createCharge(Charge charge) {

		LOGGER.info("Procesando cargo para el usuario -> {},  event_id -> {}, currency -> {}, amount -> {} ");

		checkEventCharge(charge);

		checkChargeOutOfDate(charge);
	
		Double amountEvt = charge.getAmount();
		Double amountInDefCurrency = currencyService.convertToCurrencyDefault(charge.getCurrency(), amountEvt);

		charge.setDateObj(DateHelper.getInstance().stringToTimestamp(charge.getDate()));
		charge.setAmount(amountInDefCurrency);
		charge.setAmountPending(amountInDefCurrency);
		charge.setOriginalAmount(amountEvt);
		Charge chargePersisted = chargeRepo.insert(charge);

		queueService.enqueueCharge(chargePersisted, null, null);

		//TODO: Resolver el tema de tipo de evento
		return chargePersisted;
	}

	private void checkEventCharge(Charge evento) {
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

	public void processPayment(Payment payment) {
		
	}

	private void checkChargeOutOfDate(Charge evento) {
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

	public void payChargesWithPayment(Payment payment) {
		//TODO: Chequear la deuda del usuario
		List<Charge> chargeWithDebt = chargeRepo.findAllWithDebt(payment.getUserId());
		List<Charge> chargeListToPersist = new ArrayList<Charge>();
		Double pagoAmount = payment.getAmount();
		for(Charge charge : chargeWithDebt) {
			if(pagoAmount > 0) {
				Double amountToUSe = Math.min(pagoAmount, charge.getAmountPending());
				pagoAmount = pagoAmount - amountToUSe;
				charge.payAndRelate(payment, amountToUSe);
				chargeListToPersist.add(charge);

				chargeRepo.save(charge);
				queueService.enqueueCharge(charge, payment, amountToUSe);
			}
		}

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

}