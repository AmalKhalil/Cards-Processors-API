package com.madfooat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.madfooat.model.Batch;

@Repository
public interface BatchRepository extends CrudRepository<Batch, Long> {

	
}
