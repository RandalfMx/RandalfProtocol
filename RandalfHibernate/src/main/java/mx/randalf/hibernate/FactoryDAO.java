package mx.randalf.hibernate;


import java.util.UUID;

import javax.naming.NamingException;

//import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.hibernate.exception.HibernateUtilException;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.Session;

public class FactoryDAO {
	private static final Logger log = Logger.getLogger(FactoryDAO.class);

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
			log.debug("Begin of beginTransaction");
			synchronized (syncTransObj) {
				autoTransaction = HibernateUtil
						.getInstance(fileHibernate).isAutoTransaction();
				if (autoTransaction) {
					message = "Creating a new transaction: ";
					HibernateUtil.getInstance(fileHibernate)
							.beginTransaction();
				} else
					message = "Using existing transaction: ";
				message = message
						+ HibernateUtil.getInstance(fileHibernate)
								.getCurrentTransaction().hashCode();
			}
			log.info(message);
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
			log.debug("End of beginTransaction");
		}
		return autoTransaction;
	}

	public static void commitTransaction(boolean autoTransaction) 
			throws HibernateException, HibernateUtilException {
		String message = "";
		try {
			log.debug("Begin of commitTransaction");
			synchronized (syncTransObj) {
				if (HibernateUtil.getInstance(fileHibernate)
						.getCurrentTransaction() != null) {
					message = ""
							+ HibernateUtil.getInstance(fileHibernate)
									.getCurrentTransaction().hashCode();
					if (autoTransaction) {
						message = "Committing a transaction: " + message;
						HibernateUtil.getInstance(fileHibernate)
								.commitTransaction();
					} else {
						message = "No commit needed for transaction: "
								+ message;
					}
				} else {
					message = "No transaction present for commit";
				}
			}
			log.info(message);
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
			log.debug("End of commitTransaction");
		}
	}

	public static void rollbackTransaction(boolean autoTransaction)
			throws HibernateException, HibernateUtilException {
		String message = "";
		try {
			log.debug("Begin of rollbackTransaction");
			synchronized (syncTransObj) {
				if (HibernateUtil.getInstance(fileHibernate)
						.getCurrentTransaction() != null) {
					message = ""
							+ HibernateUtil.getInstance(fileHibernate)
									.getCurrentTransaction().hashCode();
					if (autoTransaction) {
						message = "Rollingback a transaction: " + message;
						HibernateUtil.getInstance(fileHibernate)
								.rollbackTransaction();
					} else {
						message = "No rollback needed for transaction: "
								+ message;
					}
				} else {
					message = "No transaction present for rollback";
				}
			}
			log.info(message);
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
			log.debug("End of rollbackTransaction");
		}
	}

	/**
	 * Metodo utilizzato per eseguire l'inizializzazione degli oggetti presenti all'interno di un risultato di una ricerca che si
	 * riferiscono ad una tabella diversa da quella presa in analisi
	 * 
	 * @param entity Oggetto da inizializzare
	 * @throws NamingException
	 */
	public static void initialize(Object entity)
			throws HibernateException, HibernateUtilException {
		initialize(entity, "hibernate.cfg.xml");
	}

	/**
	 * Metodo utilizzato per eseguire l'inizializzazione degli oggetti presenti all'interno di un risultato di una ricerca che si
	 * riferiscono ad una tabella diversa da quella presa in analisi
	 * 
	 * @param entity Oggetto da inizializzare
	 * @param fHibernate file di configurazione Hibernate da utilizzare (defalt "hibernate.cfg.xml")
	 * @throws NamingException
	 */
	@SuppressWarnings("deprecation")
	public static void initialize(Object entity, String fHibernate)
			throws HibernateException, HibernateUtilException {
		fileHibernate = fHibernate;
		// Per evitare l'errore "Illegally attempted to associate a proxy..."
		synchronized (syncObject) {
			boolean isSessionOpened;
			Session session = null;

			try {
				if (entity != null) {
					if (!Hibernate.isInitialized(entity)) {
						isSessionOpened = HibernateUtil.getInstance(
								fileHibernate).isSessionOpened();
						session = HibernateUtil.getInstance(fileHibernate)
								.getSession();
						try {
							try{
								session.lock(entity, LockMode.NONE);
							} catch (MappingException e){
								session.persist(entity);
							}
							Hibernate.initialize(entity);
						}catch (HibernateException e){
							log.error(e.getMessage(), e);
							throw e;
						} finally {
							if (!isSessionOpened) {
								HibernateUtil.getInstance(fileHibernate)
										.closeSession();
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

}
