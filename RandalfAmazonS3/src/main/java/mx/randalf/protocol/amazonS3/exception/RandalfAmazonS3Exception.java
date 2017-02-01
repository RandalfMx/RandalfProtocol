/**
 * 
 */
package mx.randalf.protocol.amazonS3.exception;

/**
 * @author massi
 *
 */
public class RandalfAmazonS3Exception extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6128179712915751763L;

	/**
	 * @param message
	 */
	public RandalfAmazonS3Exception(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RandalfAmazonS3Exception(String message, Throwable cause) {
		super(message, cause);
	}

}
