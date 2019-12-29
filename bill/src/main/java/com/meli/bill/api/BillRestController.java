package com.meli.bill.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/list/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Bill>> listByRange(@Valid @PathVariable Integer user_id, @RequestParam(name = "month_from") Integer monthFrom, @RequestParam(name = "year_from") Integer yearFrom, @RequestParam(name = "month_to") Integer monthTo, @RequestParam(name = "year_to") Integer yearTo) {
    	List<Bill> bills = billService.getBillsByUserIdAndRange(user_id, monthFrom, yearFrom, monthTo, yearTo);
    	return new ResponseEntity<List<Bill>>(bills, HttpStatus.OK);
    }

}