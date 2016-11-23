/**
 * 
 */
package mx.randalf.hibernate.exception;

/**
 * @author massi
 *
 */
public class HibernateUtilException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1250041062239253406L;

	/**
	 * @param message
	 */
	public HibernateUtilException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HibernateUtilException(String message, Throwable cause) {
		super(message, cause);
	}

}
