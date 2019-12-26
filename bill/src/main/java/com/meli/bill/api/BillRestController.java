package com.meli.bill.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meli.bill.model.Bill;
import com.meli.bill.service.BillService;

@RestController
@RequestMapping(value = "/bill")
public class BillRestController {

	@Autowired
	private BillService billService;

    @RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Bill>> list(@Valid @PathVariable Integer user_id) {
    	List<Bill> bills = billService.getBills(user_id);
    	return new ResponseEntity<List<Bill>>(bills, HttpStatus.OK);
    }

}