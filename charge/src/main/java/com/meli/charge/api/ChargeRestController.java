package com.meli.charge.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meli.charge.api.request.ChargeEvent;
import com.meli.charge.api.response.TotalChargeInfoResponse;
import com.meli.charge.api.response.TotalPendingChargeResponse;
import com.meli.charge.model.Charge;
import com.meli.charge.service.ChargeService;

@RestController
@RequestMapping(value = "/charge")
public class ChargeRestController {

	@Autowired
	private ChargeService chargeService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Charge> crear(@Valid @RequestBody ChargeEvent chargeEvt) {
    	Charge charge = chargeService.createCharge(chargeEvt);
    	return new ResponseEntity<Charge>(charge, HttpStatus.OK);
    }

    @RequestMapping(value = "/list/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Charge>> list(@Valid @PathVariable Integer user_id) {
    	List<Charge> payments = chargeService.listByUserId(user_id);
    	return new ResponseEntity<List<Charge>>(payments, HttpStatus.OK);
    }

    @RequestMapping(value = "/listpending/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Charge>> listPending(@Valid @PathVariable Integer user_id) {
    	List<Charge> payments = chargeService.listPendingByUserId(user_id);
    	return new ResponseEntity<List<Charge>>(payments, HttpStatus.OK);
    }

    @RequestMapping(value = "/total/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<TotalChargeInfoResponse> totalChargeInfo(@Valid @PathVariable Integer user_id) {
    	return new ResponseEntity<TotalChargeInfoResponse>(chargeService.totalChargeInfo(user_id), HttpStatus.OK);
    }

    @RequestMapping(value = "/totalamountpending/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<TotalPendingChargeResponse> totalChargeAmountPending(@Valid @PathVariable Integer user_id) {
    	return new ResponseEntity<TotalPendingChargeResponse>(chargeService.totalChargeAmountPending(user_id), HttpStatus.OK);
    }

}