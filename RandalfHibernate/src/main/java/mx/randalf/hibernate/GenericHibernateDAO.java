/**
 * 
 */
package mx.randalf.hibernate;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.naming.NamingException;

import mx.randalf.configuration.exception.ConfigurationException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author massi
 * 
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable>
		implements GenericDAO<T, ID> {

	private static final Logger log = Logger
			.getLogger(GenericHibernateDAO.class);

	private Session session = null;

	private Transaction transaction = null;

	private boolean clear = false;

	private boolean flush = true;

	private Class<T> persistentClass;

	private int page = 0;

	private int pageSize = 0;

	protected String fileHibernate = "hibernate.cfg.xml";
	protected HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public GenericHibernateDAO(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void beginTransaction() throws NamingException,
			ConfigurationException {
		session = HibernateUtil.getInstance(fileHibernate, hibernateTemplate)
				.getSession();
		if (session != null) {
			if (isToBeTransacted()) {
				try {
					transaction = session.beginTransaction();
					transaction.setTimeout(600);
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
					HibernateUtil.getInstance(fileHibernate, hibernateTemplate)
							.closeSession();
				}
			}
		}
	}

	private boolean isToBeTransacted()
			throws NamingException,
			ConfigurationException {
		boolean result = false;
		
		try {
			result = HibernateUtil.getInstance(fileHibernate, hibernateTemplate)
					.isAutoTransaction();
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
		return result;
	}

	public void delete(T entity) throws HibernateException, NamingException,
			ConfigurationException {

		beginTransaction();

		try {
			session.delete(entity);
			flush();
		} catch (HibernateException e) {

			rollbackTransaction();
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception ex) {
			rollbackTransaction();
			log.error(ex.getMessage(), ex);
			throw new HibernateException(ex.getMessage(), ex);
		} finally {
		}
		commitTransaction();
	}

	private void flush() throws HibernateException, ConstraintViolationException {
		if (flush) {
			try {
				session.flush();
			} catch (ConstraintViolationException e) {
				throw e;
			} catch (HibernateException e) {
				throw e;
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				throw new HibernateException(ex.getMessage(), ex);
			}
		}
	}

	public Session getSession(){
		return session;
	}

	public void setFlush(boolean flush) {
		this.flush = flush;
	}

	public void rollbackTransaction() throws NamingException,
			HibernateException, ConfigurationException {
		if (isToBeTransacted()) {
			transaction.rollback();
			clear();
			HibernateUtil.getInstance(fileHibernate, hibernateTemplate)
					.closeSession();
		}

	}

	public void commitTransaction() throws HibernateException, NamingException,
			ConfigurationException {
		if (isToBeTransacted()) {
			try {
				transaction.commit();
				clear();
				HibernateUtil.getInstance(fileHibernate, hibernateTemplate)
						.closeSession();
			} catch (HibernateException e) {
				log.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	private void clear() throws HibernateException {
		if (clear) {
			try {
				session.clear();
			} catch (HibernateException ex) {
				log.error(ex.getMessage(), ex);
				throw ex;
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				throw new HibernateException(ex.getMessage(), ex);
			}
		}
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public List<T> findAll() throws NamingException, HibernateException,
			ConfigurationException {
		return findAll(null);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(List<Order> orders) throws NamingException,
			HibernateException, ConfigurationException {

		List<T> result = null;
		Criteria crit = null;

		try {

			beginTransaction();
			crit = createCriteria();
			if (orders != null) {
				for (Order order : orders) {
					crit.addOrder(order);
				}
			}
			paging(crit);
			result = crit.list();
		} catch (HibernateException ex) {
			rollbackTransaction();
			log.error(ex.getMessage(), ex);
			throw ex;
		} catch (Exception ex) {
			rollbackTransaction();
			log.error(ex.getMessage(), ex);
			throw new HibernateException(ex.getMessage(), ex);
		} finally {
			commitTransaction();
		}

		return result;
	}

	@SuppressWarnings({ "unchecked" })
	public T findById(ID id) throws HibernateException, NamingException,
			ConfigurationException {

		T entity = null;
		beginTransaction();

		try {
			entity = (T) session.load(getPersistentClass(), id);
			session.lock(entity, LockMode.NONE);
			Hibernate.initialize(entity);
		} catch (ObjectNotFoundException ex) {
			entity = null;
		} catch (HibernateException ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new HibernateException(ex.getMessage(), ex);
		} finally {
			commitTransaction();
		}

		return entity;
	}

	protected void paging(Criteria crit) {
		if ((pageSize > 0) && (page > 0)) {
			int first = ((page - 1) * pageSize);
			crit.setFirstResult(first);
			crit.setMaxResults(pageSize);
		}
	}

	@SuppressWarnings("unchecked")
	public T getById(ID id) throws HibernateException, NamingException,
			ConfigurationException {
		T result = null;
		beginTransaction();

		try {
			Criteria crit = createCriteria();
			crit.add(Restrictions.eq("id", id));
			paging(crit);
			result = (T) crit.uniqueResult();
		} catch (HibernateException ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new HibernateException(ex.getMessage(), ex);
		} finally {
			commitTransaction();
		}
		return result;
	}

	public void save(T entity) throws HibernateException, NamingException,
			ConfigurationException, ConstraintViolationException {

		beginTransaction();
		try {
			session.save(entity);
			flush();
			commitTransaction();
		} catch (ConstraintViolationException e) {
			rollbackTransaction();
			throw e;
		} catch (HibernateException ex) {
			log.error(ex.getMessage(), ex);
			rollbackTransaction();
			throw ex;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);

			rollbackTransaction();
			throw new HibernateException(ex.getMessage(), ex);
		}

	}

	public void update(T entity) throws HibernateException, NamingException,
			ConfigurationException, ConstraintViolationException {

		beginTransaction();
		try {
			session.saveOrUpdate(entity);
			flush();
			commitTransaction();
		} catch (ConstraintViolationException e) {
			rollbackTransaction();
			throw e;
		} catch (HibernateException ex) {
			log.error(ex.getMessage(), ex);
			rollbackTransaction();
			throw ex;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollbackTransaction();
			throw new HibernateException(ex.getMessage(), ex);
		}

	}

	public Criteria createCriteria() {
		return session.createCriteria(getPersistentClass());
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int executeUpdate(String sql) throws HibernateException {
		SQLQuery query = null;
		int result = 0;

		query = session.createSQLQuery(sql);
		result = query.executeUpdate();
		return result;
	}

	public static String genMD5Key(String key) {
		String result = null;

		result = DigestUtils.md5Hex(key);
		return result;
	}

	public String getFileHibernate() {
		return fileHibernate;
	}
}
