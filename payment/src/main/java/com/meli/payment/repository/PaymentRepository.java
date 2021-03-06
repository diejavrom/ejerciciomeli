package com.meli.payment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.meli.payment.model.Payment;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

	public List<Payment> findByUserId(Integer userId);

	public List<Payment> findByIdempKey(String idempKey);

}
