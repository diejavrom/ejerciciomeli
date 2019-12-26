package com.meli.charge.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.meli.charge.model.ChargeType;

public interface ChargeTypeRepository extends MongoRepository<ChargeType, String> {

	public List<ChargeType> findByType(String type);

}
