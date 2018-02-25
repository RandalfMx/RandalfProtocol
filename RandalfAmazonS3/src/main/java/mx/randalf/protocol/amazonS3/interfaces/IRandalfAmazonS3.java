/**
 * 
 */
package mx.randalf.protocol.amazonS3.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import com.amazonaws.services.s3.model.S3Object;

import mx.randalf.digest.MD5;
import mx.randalf.protocol.amazonS3.exception.RandalfAmazonS3Exception;

/**
 * @author massi
 *
 */
public abstract class IRandalfAmazonS3<S> {
	private String md5Base64 = null;
	private String md5 = null;

	/**
	 * Metodo utilizzato per inviare un file sull' storage S3
	 * 
	 * @param fileInput
	 * @param contentType
	 * @param bucketName
	 * @param fileOutput
	 * @return
	 * @throws RandalfAmazonS3Exception
	 */
	public boolean sendFile(File fileInput, String contentType, String bucketName, String fileOutput)
			throws RandalfAmazonS3Exception{
		S storage = null;
		boolean result = false;
		MD5 md5 = null;

		try {
			storage = openConn(bucketName);
			
			md5 = new MD5(fileInput);
			this.md5Base64 = md5.getDigest64Base();
			this.md5 = md5.getDigest();
			if (!isValid(storage, bucketName, fileOutput, this.md5Base64, this.md5)){
				if (sendFile(storage, fileInput, contentType, this.md5Base64, this.md5, bucketName, fileOutput)){
					result=true;
				}
			} else {
				result = true;
			}
		} catch (RandalfAmazonS3Exception e) {
			throw e;
		} catch (NoSuchAlgorithmException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (IOException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (Exception e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		return result;
		
	}

	/**
	 * Metodo utilizzato per scaricare un file completo presente nell'archivio S3
	 * 
	 * @param bucketName Indica il contenitore di riferimento
	 * @param fileInput Indica il nome del file da scaricare
	 * @return
	 * @throws RandalfAmazonS3Exception
	 */
	public InputStream getFile(String bucketName, String fileInput) throws RandalfAmazonS3Exception{
		return getFile(bucketName, fileInput, null, null);
	}

	/**
	 * Metodo utilizzato per scaricare una parte di un file presente nell'archivio S3
	 * 
	 * @param bucketName Indica il contenitore di riferimento
	 * @param fileInput Indica il nome del file da scaricare
	 * @param start Indica il byte di partenza in formato 0 based
	 * @param end Indica il byte di arrivo in formato 0 based
	 * @return
	 * @throws RandalfAmazonS3Exception
	 */
	public InputStream getFile(String bucketName, String fileInput, Integer start, Integer end) throws RandalfAmazonS3Exception{
		S storage = null;

		try {
			storage = openConn(bucketName);

			if (exists(storage, bucketName, fileInput)){
				return getFile(storage, bucketName, fileInput, start, end);
			} else {
				throw new RandalfAmazonS3Exception("Il file ["+fileInput+"] non è presente in base dati");
			}
		} catch (RandalfAmazonS3Exception e) {
			throw e;
		}
	}

	public S3Object readInfo(String bucketName, String fileInput) throws RandalfAmazonS3Exception{
		S storage = null;

		try {
			storage = openConn(bucketName);

			if (exists(storage, bucketName, fileInput)){
				return readInfo(storage, bucketName, fileInput);
			} else {
				throw new RandalfAmazonS3Exception("Il file ["+fileInput+"] non è presente in base dati");
			}
		} catch (RandalfAmazonS3Exception e) {
			throw e;
		}
	}

	public boolean exists(String bucketName, String fileInput)
			throws RandalfAmazonS3Exception{
		S storage = null;

		try {
			storage = openConn(bucketName);

			return exists(storage, bucketName, fileInput);
		} catch (RandalfAmazonS3Exception e) {
			throw e;
		}
	}

	public boolean isValid(String bucketName, String fileOutput, String md5Base64, String md5)
			throws RandalfAmazonS3Exception{
		S storage = null;

		try {
			storage = openConn(bucketName);

			return isValid(storage, bucketName, fileOutput, md5Base64, md5);
		} catch (RandalfAmazonS3Exception e) {
			throw e;
		}
	}
	
	protected abstract S3Object readInfo(S storage, String bucketName, String fileInput);

	protected abstract InputStream getFile(S storage, String bucketName, String fileInput, Integer start, Integer end)  throws RandalfAmazonS3Exception;

	protected abstract boolean sendFile(S storage, File fileInput, String contentType, String md5Base64,
			String md5, String bucketName, String fileOutput) throws RandalfAmazonS3Exception;

	protected abstract boolean exists(S storage, String bucketName, String fileOutput)
			throws RandalfAmazonS3Exception;

	protected abstract  boolean isValid(S storage, String bucketName, String fileOutput, String md5Base64, String md5)
			throws RandalfAmazonS3Exception;

	protected abstract S openConn(String bucketName) throws RandalfAmazonS3Exception;

	/**
	 * @return the md5Base64
	 */
	public String getMd5Base64() {
		return md5Base64;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

}
