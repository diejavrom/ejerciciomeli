package com.meli.charge.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.meli.charge.model.Charge;

public interface ChargeRepository extends MongoRepository<Charge, String> {

	public List<Charge> findByUserId(Integer userId);

	@Query("{ $and: [ {amountPending : { $gt: 0 } }, {userId : ?0}] }")
	public List<Charge> findAllWithDebt(Integer userId);

	public List<Charge> findByEventId(Integer event_id);
	

}
