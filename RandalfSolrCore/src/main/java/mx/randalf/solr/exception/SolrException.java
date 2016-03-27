/**
 * 
 */
package mx.randalf.solr.exception;

/**
 * Classe utilizzata per la gestione degli errori per il Solr
 * @author massi
 *
 */
public class SolrException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2350243908413388534L;

	/**
	 * 
	 */
	public SolrException() {
	}

	/**
	 * @param message
	 */
	public SolrException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SolrException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SolrException(String message, Throwable cause) {
		super(message, cause);
	}

}
