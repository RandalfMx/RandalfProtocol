package mx.randalf.hibernate;


import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

import mx.randalf.hibernate.exception.HibernateUtilException;

public interface GenericDAO<T, ID> {

	void delete(T entity) throws HibernateException, HibernateUtilException;

	List<T> findAll(String postFindType) throws HibernateException, HibernateUtilException;

	List<T> findAll(List<Order> orders, String postFindType) throws HibernateException, HibernateUtilException;

	T findById(ID id, String postFindType) throws HibernateException, HibernateUtilException;

	public T getById(ID id, String postFindType) throws HibernateException, HibernateUtilException;

	void save(T entity) throws HibernateException, HibernateUtilException;

	void update(T entity) throws HibernateException, HibernateUtilException;
}