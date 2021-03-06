package com.meli.currency.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.meli.currency.model.CurrencyConversion;

@Repository
public interface CurrencyConversionRepository extends MongoRepository<CurrencyConversion, String> {

	public List<CurrencyConversion> findByName(String name);

	public List<CurrencyConversion> findByDef(Boolean def);

}
