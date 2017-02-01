/**
 * 
 */
package mx.randalf.protocol.amazonS3.googleCloud;

import java.io.File;
import java.io.FileInputStream;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.testing.RemoteStorageHelper;

import mx.randalf.protocol.amazonS3.exception.RandalfAmazonS3Exception;
import mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3;

/**
 * @author massi
 *
 */
public class RandalfAmazonS3GoogleCloud extends IRandalfAmazonS3<Storage> {

	/**
	 * @throws RandalfAmazonS3Exception
	 * 
	 */
	public RandalfAmazonS3GoogleCloud() throws RandalfAmazonS3Exception {
		String googleCredentials = null;

		googleCredentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

		if (googleCredentials == null) {
			throw new RandalfAmazonS3Exception(
					"Non risulta indicata la Variabile d'ambiente [GOOGLE_APPLICATION_CREDENTIALS]");
		}
		// GOOGLE_APPLICATION_CREDENTIALS
		// /Users/massi/Desktop/Lavoro/Sorgenti/Personale/Randalf/RandalfProtocol/RandalfAmazonS3/CredenzialiAmazon/New-MagazziniDigitali-6794d764a97e.json
	}

	/**
	 * @see mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3#sendFile(java.io.File,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendFile(Storage storage, File fileInput, String contentType, String md5Base64, String bucketName,
			String fileOutput) throws RandalfAmazonS3Exception {
		BlobId blobId = null;
		BlobInfo blobInfo = null;
		Blob blob = null;
		FileInputStream fileInputStream = null;
		boolean result = false;

		try {

			blobId = BlobId.of(bucketName, fileOutput);
			fileInputStream = new FileInputStream(fileInput);
			blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
			blob = storage.create(blobInfo, fileInputStream);

			if (blob.getMd5().equals(md5Base64)) {
				result = true;
			}
		} catch (Exception e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	protected boolean exists(Storage storage, String bucketName, String fileOutput, String md5)
			throws RandalfAmazonS3Exception {
		Blob blob = null;
		BlobId blobId = null;
		boolean exists = false;

		blobId = BlobId.of(bucketName, fileOutput);
		blob = storage.get(blobId);

		if (blob != null && blob.exists()) {
			if (blob.getMd5().equals(md5)) {
				exists = true;
			} else {
				throw new RandalfAmazonS3Exception(
						"Il file risulta presente nello Storage ma l chiave MD5 non corrisponde");
			}
		}
		return exists;
	}

	@Override
	protected Storage openConn(String bucketName) throws RandalfAmazonS3Exception {
		RemoteStorageHelper helper = null;
		Storage storage = null;

		helper = RemoteStorageHelper.create();
		storage = helper.getOptions().getService();

		return storage;
	}
}
