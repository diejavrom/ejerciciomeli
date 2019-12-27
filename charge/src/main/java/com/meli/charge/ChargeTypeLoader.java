package com.meli.charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.meli.charge.model.ChargeType;
import com.meli.charge.repository.ChargeTypeRepository;

@Component
public class ChargeTypeLoader implements ApplicationRunner {

	@Autowired
	private ChargeTypeRepository tipoCargoRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		ChargeType tp = new ChargeType();
		tp.setType("VENTA");
		tp.setCategory("MARKETPLACE");
		tipoCargoRepo.save(tp);

		tp = new ChargeType();
		tp.setType("ENVÍO");
		tp.setCategory("MARKETPLACE");
		tipoCargoRepo.save(tp);

		tp = new ChargeType();
		tp.setType("CLASIFICADO");
		tp.setCategory("MARKETPLACE");
		tipoCargoRepo.save(tp);

		tp = new ChargeType();
		tp.setType("CRÉDITO");
		tp.setCategory("SERVICIOS");
		tipoCargoRepo.save(tp);

		tp = new ChargeType();
		tp.setType("FIDELIDAD");
		tp.setCategory("SERVICIOS");
		tipoCargoRepo.save(tp);

		tp = new ChargeType();
		tp.setType("PUBLICIDAD");
		tp.setCategory("SERVICIOS");
		tipoCargoRepo.save(tp);

		tp = new ChargeType();
		tp.setType("MERCADOSHOP");
		tp.setCategory("EXTERNO");
		tipoCargoRepo.save(tp);

		tp = new ChargeType();
		tp.setType("MERCADOPAGO");
		tp.setCategory("EXTERNO");
		tipoCargoRepo.save(tp);

	}

}