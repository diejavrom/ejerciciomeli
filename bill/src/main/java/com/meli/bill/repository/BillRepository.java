package com.meli.bill.repository;

import java.util.List;

import javax.validation.Valid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.meli.bill.model.Bill;

@Repository
public interface BillRepository extends MongoRepository<Bill, String> {

	public List<Bill> findByUserId(@Valid Integer user_id);

	@Query("{ $and: [ {userId : ?0}, {month : ?1 }, {year : ?2 }] }")
	public List<Bill> findByUserIdMonthYear(Integer userId, Integer month, Integer year);

	@Query("{ $and: [ {userId : ?0}, { $or:[{year:{$gt:?2}}, { $and:[{year:{$eq:?2}}, {month:{$gte:?1}}]} ]}, {$or:[{year:{$lt:?4}}, {$and:[ { year:{$eq:?4}},{month:{$lte:?3} } ]}]}]}")
	public List<Bill> findByUserIdAndRange(Integer userId, Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo);

	@Query("{ $and: [  {userId : ?0}, { $or:[{year:{$gt:?2}}, { $and:[{year:{$eq:?2}}, {month:{$gte:?1}}]} ]}, {$or:[{year:{$lt:?4}}, {$and:[ { year:{$eq:?4}},{month:{$lte:?3} } ]}]}]}")
	public List<Bill> findWithPendingAmountByUserIdAndRange(Integer userId, Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo);

}