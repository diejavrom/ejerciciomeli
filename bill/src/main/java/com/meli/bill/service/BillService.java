package com.meli.bill.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meli.bill.DateHelper;
import com.meli.bill.exception.ChargeAndPayAlreadyProccessedException;
import com.meli.bill.exception.ParamMandatoryException;
import com.meli.bill.model.Bill;
import com.meli.bill.model.Charge;
import com.meli.bill.model.to.ChargeTO;
import com.meli.bill.repository.BillRepository;

@Service
public class BillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillService.class);

	@Autowired
	private BillRepository billRepo;

	public Bill getBillByUserMonthYear(Integer userId, Integer month, Integer year) {
		LOGGER.info("Obteniendo factura para el usuario -> {} en mes -> {} y año -> {}", userId, month, year);

		checkParams(userId, month, year);
		
		List<Bill> bills = billRepo.findByUserIdMonthYear(userId, month, year);
		if(bills.isEmpty()) {
			return null;
		} else if(bills.size() > 1) {
			throw new IllegalStateException(String.format("Existe más de una factura para el mes '%d' y año '%d' asociada al usuario '%d'", month, year, userId));
		} else {
			return bills.iterator().next();
		}
	}

	private void checkParams(Integer userId, Integer month, Integer year) {
		if(userId == null) {
			throw new ParamMandatoryException("El id de usuario no puede ser null");
		}
		if(month == null) {
			throw new ParamMandatoryException("month no puede ser null");
		}
		if(year == null) {
			throw new ParamMandatoryException("year no puede ser null");
		}
		if(month < 1 || month > 12) {
			throw new ParamMandatoryException("month debe estar entre 1 y 12");
		}
	}

	public List<Bill> getBills(Integer user_id) {
		if(user_id == null) {
			throw new ParamMandatoryException("El id de usuario no puede ser null");
		}

		LOGGER.info("Obteniendo todas las facturas para el usuario {}", user_id);

		return billRepo.findByUserId(user_id);
	}

	public List<Bill> getBillsByUserIdAndRange(Integer userId, Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo) {

		LOGGER.info("Obteniendo todas las facturas para el usuario {} en el rango {}/{} - {}/{}", userId, monthFrom, yearFrom, monthTo, yearTo);

		checkParams(userId, monthFrom, yearFrom, monthTo, yearTo);

		return billRepo.findByUserIdAndRange(userId, monthFrom, yearFrom, monthTo, yearTo);
	}

	private void checkParams(Integer userId, Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo) {
		if(userId == null) {
			throw new ParamMandatoryException("El id de usuario no puede ser null");
		}
		if(monthFrom == null) {
			throw new ParamMandatoryException("month_from no puede ser null");
		}
		if(yearFrom == null) {
			throw new ParamMandatoryException("year_from no puede ser null");
		}
		if(monthTo == null) {
			throw new ParamMandatoryException("month_to no puede ser null");
		}
		if(yearTo == null) {
			throw new ParamMandatoryException("year_to from no puede ser null");
		}
		if(monthFrom < 1 || monthFrom > 12) {
			throw new ParamMandatoryException("month_from debe estar entre 1 y 12");
		}
		if(monthTo < 1 || monthTo > 12) {
			throw new ParamMandatoryException("month_to debe estar entre 1 y 12");
		}
		if(!DateHelper.getInstance().lowerThanEqual(monthFrom, yearFrom, monthTo, yearTo)) {
			throw new ParamMandatoryException(String.format("month_from/year_from debe '%d/%d' ser menor o igual que 'month_to/year_to' %d/%d", monthFrom, yearFrom, monthTo, yearTo));
		}
	}

	public Bill receiveCharge(ChargeTO chargeTO) {

		checkChargeParam(chargeTO);

		LOGGER.info("Procesando cargo Id -> {}, amountPending ->  {}",  chargeTO.getId(), chargeTO.getAmountPending());

		Timestamp now = DateHelper.getInstance().getNow();
		Integer month = DateHelper.getInstance().getMonth(now);
		Integer year = DateHelper.getInstance().getYear(now);
		Integer userId = chargeTO.getUserId();
		Bill bill = getBillByUserMonthYear(userId, month, year);
		if(bill == null) {
			bill = new Bill(userId, month, year);
		}
	
		if(chargeTO.getPaymentId() != null) {
			boolean existsChargeWithPayment = bill.existsChargeWithPayment(chargeTO.getId(), chargeTO.getPaymentId());
			if(existsChargeWithPayment) {
				throw new ChargeAndPayAlreadyProccessedException(String.format("El cargo con Id '%s' y pago con Id '%s' ya fueron procesados ", chargeTO.getId(), chargeTO.getPaymentId())); 
			}
		}

		Charge charge = bill.getChargeById(chargeTO.getId());
		if(charge == null) {
			charge = new Charge(chargeTO);
		} else {
			charge.updateWithChargeTOInfo(chargeTO);
		}

		bill.updateCharge(charge);

		return billRepo.save(bill);
	}

	private void checkChargeParam(ChargeTO chargeTO) {
		if(chargeTO == null) {
			throw new ParamMandatoryException("El cargo no puede ser null");
		}
		if(chargeTO.getId() == null) {
			throw new ParamMandatoryException("El ID del cargo no puede ser null");
		}
		if(chargeTO.getAmount() == null) {
			throw new ParamMandatoryException("El monto del cargo no puede ser null");
		}
		if(chargeTO.getAmountPending() == null) {
			throw new ParamMandatoryException("El monto pendiente del cargo no puede ser null");
		}
		if(chargeTO.getEvent_id() == null) {
			throw new ParamMandatoryException("El evento del cargo no puede ser null");
		}
		if(chargeTO.getUserId() == null) {
			throw new ParamMandatoryException("El usuario del cargo no puede ser null");
		}
		if(chargeTO.getDateObj() == null) {
			throw new ParamMandatoryException("La fecha del cargo no puede ser null");
		}
	}

}