package com.madfooat.repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface CoreRepository<T, ID extends Serializable>
		extends PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T> {

	T saveAndRefresh(T object);

	List<T> saveAndRefresh(Iterable<T> objects);

	void refresh(T object);

	void refresh(Iterable<T> objects);

	<M> List<M> executeQuery(String queryString, Map<String, Object> parameters, Class<M> mappedClass);

	List<T> findAll();

	<M> List<M> executeNativeQuery(String sqlQuery, Map<String, Object> parameters, Pageable pageable,
			Class<M> mappedClass);

	BigInteger executeNativeCountQuery(String sqlQuery, Map<String, Object> parameters);
}
