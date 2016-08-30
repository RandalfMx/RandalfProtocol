package mx.randalf.hibernate;


import java.util.UUID;

import javax.naming.NamingException;

import mx.randalf.configuration.exception.ConfigurationException;

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

	public static boolean checkConnection() throws Exception {
		try {
			HibernateUtil.getInstance(fileHibernate, null).getSession();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}
		return true;
	}

	public static boolean beginTransaction()
			throws NamingException,
				ConfigurationException {
		return beginTransaction("hibernate.cfg.xml");
	}

	public static boolean beginTransaction(String fHibernate)
			throws NamingException,
				ConfigurationException {
		fileHibernate = fHibernate;
		boolean autoTransaction = false;
		String message = "";
		try {
			log.debug("Begin of beginTransaction");
			synchronized (syncTransObj) {
				autoTransaction = HibernateUtil
						.getInstance(fileHibernate, null).isAutoTransaction();
				if (autoTransaction) {
					message = "Creating a new transaction: ";
					HibernateUtil.getInstance(fileHibernate, null)
							.beginTransaction();
				} else
					message = "Using existing transaction: ";
				message = message
						+ HibernateUtil.getInstance(fileHibernate, null)
								.getCurrentTransaction().hashCode();
			}
			log.info(message);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			log.debug("End of beginTransaction");
		}
		return autoTransaction;
	}

	public static void commitTransaction(boolean autoTransaction)
			throws NamingException, ConfigurationException {
		String message = "";
		try {
			log.debug("Begin of commitTransaction");
			synchronized (syncTransObj) {
				if (HibernateUtil.getInstance(fileHibernate, null)
						.getCurrentTransaction() != null) {
					message = ""
							+ HibernateUtil.getInstance(fileHibernate, null)
									.getCurrentTransaction().hashCode();
					if (autoTransaction) {
						message = "Committing a transaction: " + message;
						HibernateUtil.getInstance(fileHibernate, null)
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
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			log.debug("End of commitTransaction");
		}
	}

	public static void rollbackTransaction(boolean autoTransaction)
			throws NamingException, ConfigurationException {
		String message = "";
		try {
			log.debug("Begin of rollbackTransaction");
			synchronized (syncTransObj) {
				if (HibernateUtil.getInstance(fileHibernate, null)
						.getCurrentTransaction() != null) {
					message = ""
							+ HibernateUtil.getInstance(fileHibernate, null)
									.getCurrentTransaction().hashCode();
					if (autoTransaction) {
						message = "Rollingback a transaction: " + message;
						HibernateUtil.getInstance(fileHibernate, null)
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
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw e;
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
	 * @throws ConfigurationException
	 */
	public static void initialize(Object entity)
		throws NamingException,
			ConfigurationException {
		initialize(entity, "hibernate.cfg.xml");
	}

	/**
	 * Metodo utilizzato per eseguire l'inizializzazione degli oggetti presenti all'interno di un risultato di una ricerca che si
	 * riferiscono ad una tabella diversa da quella presa in analisi
	 * 
	 * @param entity Oggetto da inizializzare
	 * @param fHibernate file di configurazione Hibernate da utilizzare (defalt "hibernate.cfg.xml")
	 * @throws NamingException
	 * @throws ConfigurationException
	 */
	public static void initialize(Object entity, String fHibernate)
		throws NamingException,
			ConfigurationException {
		fileHibernate = fHibernate;
		// Per evitare l'errore "Illegally attempted to associate a proxy..."
		synchronized (syncObject) {
			boolean isSessionOpened;
			Session session = null;

			try {
				if (entity != null) {
					if (!Hibernate.isInitialized(entity)) {
						isSessionOpened = HibernateUtil.getInstance(
								fileHibernate, null).isSessionOpened();
						session = HibernateUtil.getInstance(fileHibernate, null)
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
								HibernateUtil.getInstance(fileHibernate, null)
										.closeSession();
							}
						}
					}
				}
			} catch (NamingException e) {
				log.error(e.getMessage(), e);
				throw e;
			} catch (ConfigurationException e) {
				log.error(e.getMessage(), e);
				throw e;
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
