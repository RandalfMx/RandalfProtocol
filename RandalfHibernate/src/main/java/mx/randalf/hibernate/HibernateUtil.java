/**
 * 
 */
package mx.randalf.hibernate;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.hibernate.exception.HibernateUtilException;

/**
 * @author massi
 * 
 */
public class HibernateUtil {
	private static final Logger log = Logger.getLogger(HibernateUtil.class);

	private Object SyncObj = new Object();

	private static SessionFactory sessionFactory = null;

	private final ThreadLocal<Session> sessionTable = new ThreadLocal<Session>();
	private final ThreadLocal<Transaction> transactionTable = new ThreadLocal<Transaction>();

	private static Hashtable<String, HibernateUtil> instance = null;

	private HibernateUtil(String fileHibernate) throws HibernateException, HibernateUtilException {
		ServiceRegistry serviceRegistry = null;
		Hashtable<?, ?> lists = null;
		Enumeration<?> keys = null;
		Object key = null;
		Configuration configuration = null;
		InitialContext ctx;
		File f = null;
		f = new File(fileHibernate);
		if (!f.exists()) {
			if (HibernateUtil.class.getResource("/" + f.getName()) != null) {
				f = new File(HibernateUtil.class.getResource("/" + f.getName()).getFile());
				if (!f.exists()) {
					f = new File(f.getAbsolutePath().replace("%23", "#"));
					if (!f.exists()) {
						f = new File(f.getAbsolutePath().replace("%20", " "));
					}
				}
			}
		}
		org.hibernate.cfg.Configuration conf = null;
		try {
			try {
				conf = new org.hibernate.cfg.Configuration();
				configuration = conf.configure(f);

				serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
						.build();

				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//        sessionFactory = configuration.buildSessionFactory();
			} catch (HibernateException e) {
				log.error(e.getMessage(), e);
			}
			if (sessionFactory == null) {
				try {

					ctx = new InitialContext();
					lists = ctx.getEnvironment();
					keys = lists.keys();
					while (keys.hasMoreElements()) {
						key = keys.nextElement();
						log.debug("\n" + key + ": " + lists.get(key));
					}
					if (mx.randalf.configuration.Configuration.getValue("dataSource") != null) {
						sessionFactory = (SessionFactory) ctx
								.lookup("java:" + mx.randalf.configuration.Configuration.getValue("dataSource"));
					} else {
						sessionFactory = (SessionFactory) ctx.lookup("java:/hibernate/GEA5/SF");
					}
				} catch (NamingException e) {
					log.error(e.getMessage(), e);
					throw new HibernateUtilException(e.getMessage(), e);
				} catch (ConfigurationException e) {
					log.error(e.getMessage(), e);
					throw new HibernateUtilException(e.getMessage(), e);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw new HibernateUtilException(e.getMessage(), e);
				}
				if (sessionFactory == null) {
					conf = new Configuration();
					configuration = conf.configure();

					serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
							.build();

					sessionFactory = configuration.buildSessionFactory(serviceRegistry);

//					sessionFactory = new Configuration().configure().buildSessionFactory();
					if (sessionFactory == null) {
						conf = new Configuration();
						configuration = conf.configure(f);

						serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
								.build();

						sessionFactory = configuration.buildSessionFactory(serviceRegistry);

//						sessionFactory = new Configuration().configure(f).buildSessionFactory();
					}
				}
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
	}

	public boolean isSessionOpened() {
		boolean ret = false;

		Session session = null;

		synchronized (SyncObj) {
			session = sessionTable.get();
			ret = (session != null);
		}

		return ret;
	}

	public boolean isAutoTransaction() {
		boolean ret = true;

		Transaction transaction = null;

		synchronized (SyncObj) {
			transaction = transactionTable.get();
			ret = (transaction == null);
		}

		return ret;
	}

	public void beginTransaction() {
		Session session = this.getSession();
		Transaction transaction = null;
		synchronized (SyncObj) {
			transaction = transactionTable.get();
			if (transaction == null) {
				transaction = session.beginTransaction();
				transaction.setTimeout(600);
				transactionTable.set(transaction);
			}
		}
	}

	public void rollbackTransaction() {
		Transaction transaction = null;
		synchronized (SyncObj) {
			transaction = transactionTable.get();
		}
		try {
			transaction.rollback();
		} finally {
			synchronized (SyncObj) {
				transactionTable.remove();
			}

			// session.clear();

			this.closeSession();
		}
	}

	public void commitTransaction() {

		Session session = null;
		Transaction transaction = null;
		synchronized (SyncObj) {
			session = sessionTable.get();
			transaction = transactionTable.get();
		}

		try {
			session.flush();

			transaction.commit();
		} finally {
			synchronized (SyncObj) {
				transactionTable.remove();
			}

			// session.clear();

			this.closeSession();
		}
	}

	public Transaction getCurrentTransaction() {
		Transaction transaction = null;
		synchronized (SyncObj) {
			transaction = transactionTable.get();
		}
		return transaction;
	}

	public Session getSession() {
		Session session = null;
		synchronized (SyncObj) {
			session = sessionTable.get();
			if (session == null) {
				try {
					session = getSessionFactory().openSession();
					sessionTable.set(session);
				} catch (HibernateException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (session.isOpen() == false) {
					closeSession();
					session = getSessionFactory().openSession();
					sessionTable.set(session);
				}
			}
		}
		return session;
	}

	public void closeSession() {

		Session session = null;
		synchronized (SyncObj) {
			session = sessionTable.get();
			if (session != null) {

				if (session.isConnected()) {
					session.disconnect();
				}
				if (session.isOpen()) {
					session.close();
				}
				sessionTable.remove();
			}
		}

	}

	public int clearSession(int count) {
		if (++count % 40 == 0) {
			count = 0;
			getSession().flush();
			getSession().clear();
		}
		return count;
	}

	public static HibernateUtil getInstance(String fileHibernate) throws HibernateException, HibernateUtilException {
		try {
			if (instance == null) {
				instance = new Hashtable<String, HibernateUtil>();
			}
			if (instance.get(fileHibernate) == null) {
				instance.put(fileHibernate, new HibernateUtil(fileHibernate));
			}
		} catch (HibernateException e) {
			throw e;
		} catch (HibernateUtilException e) {
			throw e;
		}
		return instance.get(fileHibernate);
	}

	private static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		if (getSessionFactory() != null) {
			getSessionFactory().close();
		}
	}
}
