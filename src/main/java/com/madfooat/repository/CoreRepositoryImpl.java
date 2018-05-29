package com.madfooat.repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CoreRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CoreRepository<T, ID> {

    private final EntityManager entityManager;

    public CoreRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public T saveAndRefresh(T object) {
        T t = save(object);
        refresh(t);
        return t;
    }

    @Override
    public List<T> saveAndRefresh(Iterable<T> objects) {
        List<T> list = save(objects);
        refresh(list);
        return list;
    }

    @Override
    public void refresh(T object) {
        entityManager.refresh(object);
    }

    @Override
    public void refresh(Iterable<T> objects) {
        objects.forEach(this::refresh);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <M> List<M> executeQuery(String queryString, Map<String, Object> parameters, Class<M> mappedClass) {
        TypedQuery<M> typedQuery = entityManager.createQuery(queryString, mappedClass);
        if (parameters != null) {
            parameters.forEach(typedQuery::setParameter);
        }
        return typedQuery.getResultList();
    }


    @Override
    public <M> List<M> executeNativeQuery(String sqlQuery, Map<String, Object> parameters, Pageable pageable, Class<M> mappedClass) {
        Query query = entityManager.createNativeQuery(sqlQuery, mappedClass);
        if (parameters != null) {
            parameters.forEach(query::setParameter);
        }
        if (pageable != null) {
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return (List<M>)query.getResultList();
    }

    @Override
    public BigInteger executeNativeCountQuery(String sqlQuery, Map<String, Object> parameters) {
        Query query = entityManager.createNativeQuery(sqlQuery);
        if (parameters != null) {
            parameters.forEach(query::setParameter);
        }
        return (BigInteger) query.getSingleResult();
    }
}
