package com.meli.status.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meli.status.api.response.StatusUsuarioResponse;
import com.meli.status.service.StatusService;

@RestController
@RequestMapping(value = "/status")
public class StatusRestController {

	@Autowired
	private StatusService statusService;

    @RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<StatusUsuarioResponse> status(@Valid @PathVariable Integer user_id) {
    	StatusUsuarioResponse status = statusService.getStatus(user_id);
    	return new ResponseEntity<StatusUsuarioResponse>(status, HttpStatus.OK);
    }

}