package mx.randalf.hibernate;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.collection.internal.PersistentSet;

//import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.hibernate.exception.HibernateUtilException;

public class FactoryDAO {
	private static final Logger log = LogManager.getLogger(FactoryDAO.class);

	private static Object syncObject = new Object();
	private static Object syncTransObj = new Object();
	private static String fileHibernate = "hibernate.cfg.xml";

	public static String newId() {
		return UUID.randomUUID().toString().toUpperCase();
	}

	public static boolean checkConnection() throws HibernateException, HibernateUtilException {
		try {
			HibernateUtil.getInstance(fileHibernate).getSession();
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new HibernateUtilException(ex.getMessage(), ex);
		}
		return true;
	}

	public static boolean beginTransaction() throws HibernateException, HibernateUtilException {
		return beginTransaction("hibernate.cfg.xml");
	}

	public static boolean beginTransaction(String fHibernate) throws HibernateException, HibernateUtilException {
		fileHibernate = fHibernate;
		boolean autoTransaction = false;
		String message = "";
		try {
			log.debug("\n"+"Begin of beginTransaction");
			synchronized (syncTransObj) {
				autoTransaction = HibernateUtil.getInstance(fileHibernate).isAutoTransaction();
				if (autoTransaction) {
					message = "Creating a new transaction: ";
					HibernateUtil.getInstance(fileHibernate).beginTransaction();
				} else
					message = "Using existing transaction: ";
				message = message + HibernateUtil.getInstance(fileHibernate).getCurrentTransaction().hashCode();
			}
			log.info("\n"+message);
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new HibernateUtilException(e.getMessage(), e);
		} finally {
			log.debug("\n"+"End of beginTransaction");
		}
		return autoTransaction;
	}

	public static void commitTransaction(boolean autoTransaction) throws HibernateException, HibernateUtilException {
		String message = "";
		try {
			log.debug("\n"+"Begin of commitTransaction");
			synchronized (syncTransObj) {
				if (HibernateUtil.getInstance(fileHibernate).getCurrentTransaction() != null) {
					message = "" + HibernateUtil.getInstance(fileHibernate).getCurrentTransaction().hashCode();
					if (autoTransaction) {
						message = "Committing a transaction: " + message;
						HibernateUtil.getInstance(fileHibernate).commitTransaction();
					} else {
						message = "No commit needed for transaction: " + message;
					}
				} else {
					message = "No transaction present for commit";
				}
			}
			log.info("\n"+message);
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new HibernateUtilException(e.getMessage(), e);
		} finally {
			log.debug("\n"+"End of commitTransaction");
		}
	}

	public static void rollbackTransaction(boolean autoTransaction) throws HibernateException, HibernateUtilException {
		String message = "";
		try {
			log.debug("\n"+"Begin of rollbackTransaction");
			synchronized (syncTransObj) {
				if (HibernateUtil.getInstance(fileHibernate).getCurrentTransaction() != null) {
					message = "" + HibernateUtil.getInstance(fileHibernate).getCurrentTransaction().hashCode();
					if (autoTransaction) {
						message = "Rollingback a transaction: " + message;
						HibernateUtil.getInstance(fileHibernate).rollbackTransaction();
					} else {
						message = "No rollback needed for transaction: " + message;
					}
				} else {
					message = "No transaction present for rollback";
				}
			}
			log.info("\n"+message);
		} catch (HibernateException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (HibernateUtilException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new HibernateUtilException(e.getMessage(), e);
		} finally {
			log.debug("\n"+"End of rollbackTransaction");
		}
	}

	/**
	 * Metodo utilizzato per eseguire l'inizializzazione degli oggetti presenti
	 * all'interno di un risultato di una ricerca che si riferiscono ad una
	 * tabella diversa da quella presa in analisi
	 * 
	 * @param entity
	 *            Oggetto da inizializzare
	 * @throws NamingException
	 */
	public static void initialize(Object entity) throws HibernateException, HibernateUtilException {
		initialize(entity, "hibernate.cfg.xml");
	}

	/**
	 * Metodo utilizzato per eseguire l'inizializzazione degli oggetti presenti
	 * all'interno di un risultato di una ricerca che si riferiscono ad una
	 * tabella diversa da quella presa in analisi
	 * 
	 * @param entity
	 *            Oggetto da inizializzare
	 * @param fHibernate
	 *            file di configurazione Hibernate da utilizzare (defalt
	 *            "hibernate.cfg.xml")
	 * @throws NamingException
	 */
	public static void initialize(Object entity, String fHibernate) throws HibernateException, HibernateUtilException {
		fileHibernate = fHibernate;
		// Per evitare l'errore "Illegally attempted to associate a proxy..."
		synchronized (syncObject) {
			boolean isSessionOpened;
			Session session = null;

			try {
				if (entity != null) {
					if (!entity.getClass().getName().equals(PersistentSet.class.getName()) && !Hibernate.isInitialized(entity)) {
						isSessionOpened = HibernateUtil.getInstance(fileHibernate).isSessionOpened();
						session = HibernateUtil.getInstance(fileHibernate).getSession();
						try {
							try {
								session.buildLockRequest(LockOptions.NONE).lock(entity);
//								session.lock(entity, LockMode.NONE);
							} catch (MappingException e) {
								session.persist(entity);
							}
							Hibernate.initialize(entity);
						} catch (HibernateException e) {
							log.error(e.getMessage(), e);
							throw e;
						} finally {
							if (!isSessionOpened) {
								HibernateUtil.getInstance(fileHibernate).closeSession();
							}
						}
					}
				}
			} catch (HibernateException e) {
				log.error(e.getMessage(), e);
				throw e;
			} catch (HibernateUtilException e) {
				log.error(e.getMessage(), e);
				throw e;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new HibernateUtilException(e.getMessage(), e);
			}
		}
	}

	public static boolean isInitialized(Object proxy) {
		boolean ret = false;
		try {
			ret = Hibernate.isInitialized(proxy);
		} catch (Exception e) {
		}
		return ret;
	}

	public static java.sql.Date convertDate(String date){
		String[] st = null;
		GregorianCalendar gc = null;
		

		gc = new GregorianCalendar();
		st = date.split("/");
		gc.set(Calendar.DAY_OF_MONTH, new Integer(st[0]));
		gc.set(Calendar.MONTH, new Integer(st[1]) - 1);
		gc.set(Calendar.YEAR, new Integer(st[2]));
		return new java.sql.Date(gc.getTimeInMillis());
	}

	public static java.sql.Timestamp convertTimestamp(String date){
		String[] st = null;
		GregorianCalendar gc = null;
		

		gc = new GregorianCalendar();
		st = date.split("/");
		gc.set(Calendar.DAY_OF_MONTH, new Integer(st[0]));
		gc.set(Calendar.MONTH, new Integer(st[1]) - 1);
		gc.set(Calendar.YEAR, new Integer(st[2]));
		return new java.sql.Timestamp(gc.getTimeInMillis());
	}

	public static String converDateIta(Timestamp data) {
		String dataITA = "";
		try {
			GregorianCalendar myData = new GregorianCalendar();
			myData.setTimeInMillis(data.getTime());
			dataITA = converDateIta(myData);
		} catch (Exception exc) {
			log.error(exc.getMessage(), exc);
		}

		return dataITA;
	}

	public static String converDateIta(Date data) {
		String dataITA = "";
		GregorianCalendar myData = new GregorianCalendar();
		myData.setTimeInMillis(data.getTime());
		dataITA = converDateIta(myData);
		return dataITA;
	}

	public static String converDateIta(GregorianCalendar myData) {
		String dataITA = "";
		try {
			if (myData.get(Calendar.DAY_OF_MONTH) < 10) {
				dataITA += "0";
			}
			dataITA += myData.get(Calendar.DAY_OF_MONTH);
			dataITA += "/";
			if (myData.get(Calendar.MONTH) + 1 < 10) {
				dataITA += "0";
			}
			dataITA += myData.get(Calendar.MONTH) + 1;
			dataITA += "/";
			dataITA += myData.get(Calendar.YEAR);

		} catch (Exception exc) {
			log.error(exc.getMessage(), exc);
		}

		return dataITA;
	}
}
