package mx.randalf.hibernate;


import java.util.List;

import javax.naming.NamingException;

import mx.randalf.configuration.exception.ConfigurationException;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

public interface GenericDAO<T, ID> {

	void delete(T entity) throws HibernateException, NamingException,
			ConfigurationException;

	List<T> findAll() throws NamingException, HibernateException,
			ConfigurationException;

	List<T> findAll(List<Order> orders) throws NamingException,
			HibernateException, ConfigurationException;

	T findById(ID id) throws HibernateException, NamingException,
			ConfigurationException;

	public T getById(ID id) throws HibernateException, NamingException,
			ConfigurationException;

	void save(T entity) throws HibernateException, NamingException,
			ConfigurationException;

	void update(T entity) throws HibernateException, NamingException,
			ConfigurationException;
}