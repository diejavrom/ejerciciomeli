package com.meli.bill.repository;

import java.util.List;

import javax.validation.Valid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.meli.bill.model.Bill;
	
public interface BillRepository extends MongoRepository<Bill, String> {

	public List<Bill> findByUserId(@Valid Integer user_id);

	@Query("{ $and: [ {userId : ?0}, {month : ?1 }, {year : ?2 }] }")
	public List<Bill> findByUserIdMonthYear(Integer userId, Integer month, Integer year);

}