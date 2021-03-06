package com.meli.payment;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.meli.payment.exception.ParamMandatoryException;

public class DateHelper {

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static DateHelper instance = new DateHelper();
	private static SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat(DATE_FORMAT);

	private DateHelper() {
	}

	public static DateHelper getInstance() {
		return instance;
	}

	public Timestamp stringToTimestamp(String dateStr) {
		try {
			return new Timestamp(ISO8601DATEFORMAT.parse(dateStr).getTime());
		} catch (ParseException e) {
			throw new IllegalArgumentException(String.format("date '%s' debe estar en el formato %s", dateStr, DATE_FORMAT));
		}
	}

	public Integer getMonth(Date date) {
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate.getMonthValue();
	}

	public Integer getYear(Date date) {
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate.getYear();
	}

	public Timestamp getNow() {
		return new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getMinTimestamp() {
		return new Timestamp(Long.MIN_VALUE);
	}

	public Date max(Date t1, Date t2) {
		if(t1.getTime() <= t2.getTime()) {
			return t2;
		} else {
			return t1;
		}
	}

	public void checkRangoFecha(Timestamp from, Timestamp to) {
		if(from == null) {
			throw new ParamMandatoryException("from no puede ser null");
		}
		if(to == null) {
			throw new ParamMandatoryException("to no puede ser null");
		}
		if(from.after(to)) {
			throw new ParamMandatoryException(String.format("from '%s' debe ser anterior o igual a desde '%s'", from, to));
		}
	}

	public boolean isMenorOIgual(Integer mes1, Integer anio1, Integer mes2, Integer anio2) {
		return anio1 < anio2 || (anio1.equals(anio2) && mes1 <= mes2);  
	}

}