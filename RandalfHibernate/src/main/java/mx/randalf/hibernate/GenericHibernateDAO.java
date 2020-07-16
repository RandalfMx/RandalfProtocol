/**
 * 
 */
package mx.randalf.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
//import org.hibernate.query.NativeQuery;

import mx.randalf.hibernate.exception.HibernateUtilException;

/**
 * @author massi
 * 
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable> implements GenericDAO<T, ID> {

	private static final Logger log = LogManager.getLogger(GenericHibernateDAO.class);

	private Session session = null;

	private Transaction transaction = null;

	private boolean clear = false;

	private boolean flush = true;

	private Class<T> persistentClass;

	private int page = 0;

	private int pageSize = 0;

	protected String fileHibernate = "hibernate.cfg.xml";

	@SuppressWarnings("unchecked")
	public GenericHibernateDAO() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public void beginTransaction() throws HibernateException, HibernateUtilException {
		try {
			session = HibernateUtil.getInstance(fileHibernate).getSession();
			if (session != null) {
				if (isToBeTransacted()) {
					try {
						transaction = session.beginTransaction();
						transaction.setTimeout(600);
					} catch (Exception ex) {
						log.error(ex.getMessage(), ex);
						HibernateUtil.getInstance(fileHibernate).closeSession();
					}
				}
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	private boolean isToBeTransacted() throws HibernateException, HibernateUtilException {
		boolean result = false;

		try {
			result = HibernateUtil.getInstance(fileHibernate).isAutoTransaction();
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		}

		return result;
	}

	public void delete(T entity) throws HibernateException, HibernateUtilException {

		try {
			beginTransaction();

			try {
				session.delete(entity);
				flush();
				commitTransaction();
			} catch (HibernateException e) {

				rollbackTransaction();
				log.error(e.getMessage(), e);
				throw e;
			} catch (Exception ex) {
				rollbackTransaction();
				log.error(ex.getMessage(), ex);
				throw new HibernateException(ex.getMessage(), ex);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
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

	public Session getSession() {
		return session;
	}

	public void setFlush(boolean flush) {
		this.flush = flush;
	}

	public void rollbackTransaction() throws HibernateException, HibernateUtilException {
		try {
			if (isToBeTransacted()) {
				transaction.rollback();
				clear();
				HibernateUtil.getInstance(fileHibernate).closeSession();
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}

	}

	public void commitTransaction() throws HibernateException, HibernateUtilException {
		try {
			if (isToBeTransacted()) {
				try {
					transaction.commit();
					clear();
					HibernateUtil.getInstance(fileHibernate).closeSession();
				} catch (HibernateException e) {
					log.error(e.getMessage(), e);
					throw e;
				}
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
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

	public List<T> findAll() throws HibernateException, HibernateUtilException {
		return findAll(null);
	}

	public List<T> findAll(List<Order> orders, int page, int pageSize) throws HibernateException, HibernateUtilException{
		List<T> list = null;

		try {
			setPage(page);
			setPageSize(pageSize);
			list = findAll(orders);
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(List<Order> orders) throws HibernateException, HibernateUtilException {

		List<T> result = null;
		Criteria crit = null;

		try {
			try {

				beginTransaction();
				crit = createCriteria();
				initTableJoin(crit);
				if (orders != null) {
					for (Order order : orders) {
						crit.addOrder(order);
					}
				}
				paging(crit);
				result = crit.list();
				postFind(result);
				commitTransaction();
			} catch (HibernateException ex) {
				rollbackTransaction();
				log.error(ex.getMessage(), ex);
				throw ex;
			} catch (HibernateUtilException e) {
				rollbackTransaction();
				log.error(e.getMessage(), e);
				throw e;
			} catch (Exception ex) {
				rollbackTransaction();
				log.error(ex.getMessage(), ex);
				throw new HibernateException(ex.getMessage(), ex);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}

		return result;
	}

	private void postFind(List<T> result) {
	  if (result != null) {
	    for (T t : result) {
	      postFind(t);
	    }
	  }
  }

  protected void postFind(T t) {
  }

  protected void initTableJoin(Criteria crit) {
	}

	public T findById(ID id) throws HibernateException, HibernateUtilException {
		return getById(id);
	}

	protected void paging(Criteria crit) {
		if ((pageSize > 0) && (page > 0)) {
			int first = ((page - 1) * pageSize);
			crit.setFirstResult(first);
			crit.setMaxResults(pageSize);
		}
	}

	@SuppressWarnings("unchecked")
	public T getById(ID id) throws HibernateException, HibernateUtilException {
		T result = null;

		try {
			try {
				beginTransaction();
				Criteria crit = createCriteria();
				crit.add(Restrictions.eq("id", id));
				paging(crit);
				result = (T) crit.uniqueResult();
				postFind(result);
				commitTransaction();
			} catch (HibernateException ex) {
				rollbackTransaction();
				log.error(ex.getMessage(), ex);
				throw ex;
			} catch (HibernateUtilException e) {
				rollbackTransaction();
				log.error(e.getMessage(), e);
				throw e;
			} catch (Exception ex) {
				rollbackTransaction();
				log.error(ex.getMessage(), ex);
				throw new HibernateException(ex.getMessage(), ex);
			}
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}

	public void save(T entity) throws HibernateException, HibernateUtilException {

		try {
			try {
				beginTransaction();
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
			} catch (HibernateUtilException e) {
				rollbackTransaction();
				throw e;
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);

				rollbackTransaction();
				throw new HibernateException(ex.getMessage(), ex);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}

	}

	public void update(T entity) throws HibernateException, HibernateUtilException{

		try {
			try {
				beginTransaction();
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
			} catch (HibernateUtilException ex) {
				log.error(ex.getMessage(), ex);
				rollbackTransaction();
				throw ex;
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				rollbackTransaction();
				throw new HibernateException(ex.getMessage(), ex);
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
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
//		NativeQuery query = null;
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

	public Integer max(String key) throws HibernateException, HibernateUtilException {
		return max(key,null);
	}

	public Integer max(String key, Criteria criteria) throws HibernateException, HibernateUtilException {
		Criteria crit = null;
		Integer result = null;

		try {
			beginTransaction();
			if (criteria != null){
				crit = criteria;
			} else {
				crit = createCriteria();
			}
			crit.setProjection(Projections.max(key));
			result = (Integer) crit.uniqueResult();
			commitTransaction();
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}

	public Long rowCount() throws HibernateException, HibernateUtilException {
		Criteria crit = null;
		Long result = null;

		try {
			beginTransaction();
			crit = createCriteria();
			result = (Long) crit.uniqueResult();
			commitTransaction();
		} catch (HibernateException e) {
			rollbackTransaction();
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			rollbackTransaction();
			log.error(e.getMessage(), e);
			throw e;
		}
		return result;
	}
	public Long rowCount(Criteria criteria) throws HibernateException, HibernateUtilException {
		Long result = null;

		criteria.setProjection(Projections.rowCount());
		result = (Long) criteria.uniqueResult();
		return result;
	}
}
