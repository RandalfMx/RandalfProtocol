/**
 * 
 */
package mx.randalf.protocol.amazonS3.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import mx.randalf.digest.MD5;
import mx.randalf.protocol.amazonS3.exception.RandalfAmazonS3Exception;

/**
 * @author massi
 *
 */
public abstract class IRandalfAmazonS3<S> {

	public boolean sendFile(File fileInput, String contentType, String bucketName, String fileOutput)
			throws RandalfAmazonS3Exception{
		S storage = null;
		boolean result = false;
		MD5 md5 = null;
		String md5Base64 = null;

		try {
			storage = openConn(bucketName);
			
			md5 = new MD5();
			md5Base64 = md5.getDigest64Base(fileInput);
			if (!exists(storage, bucketName, fileOutput, md5Base64)){
				if (sendFile(storage, fileInput, contentType, md5Base64, bucketName, fileOutput)){
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
		}
		return result;
		
	}

	protected abstract boolean sendFile(S storage, File fileInput, String contentType, String md5Base64, 
			String bucketName, String fileOutput) throws RandalfAmazonS3Exception;

	protected abstract boolean exists(S storage, String bucketName, String fileOutput, String md5)
			throws RandalfAmazonS3Exception;

	protected abstract S openConn(String bucketName) throws RandalfAmazonS3Exception;

}
