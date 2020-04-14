/**
 * 
 */
package mx.randalf.protocol.amazonS3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.testing.RemoteStorageHelper;
import com.google.cloud.storage.testing.RemoteStorageHelper.StorageHelperException;

/**
 * @author massi
 *
 */
public class GoogleCloudStorageTest {

	/**
	 * 
	 */
	public GoogleCloudStorageTest() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// GoogleCredential credential = null;
		RemoteStorageHelper helper = null;
		Storage storage = null;
		StorageOptions storageOptions = null;
		File file = null;
		FileInputStream fileInputStream = null;
		BlobId blobId = null;
		Blob blob = null;
		BlobInfo blobInfo = null;
		String bucketName = "magazzinidigitali-150411.appspot.com";
		String blobName = "6e/1b/e1/69/11/d2/4c/96/6e1be169-11d2-4c96-91c7-8f814dd018caC20161102.tar";

		try {
			// credential = GoogleCredential.getApplicationDefault();

			helper = RemoteStorageHelper.create();
			storage = helper.getOptions().getService();
			storageOptions = storage.getOptions();
//			System.out.println("connectTimeout: " + storageOptions.connectTimeout() + "\r\tgetApplicationName: "
//					+ storageOptions.getApplicationName() + "\r\tgetClock: " + storageOptions.getClock()
//					+ "\r\tgetConnectTimeout: " + storageOptions.getConnectTimeout() + "\r\tgetCredentials: "
//					+ storageOptions.getCredentials() + "\r\tgetHost: " + storageOptions.getHost());
			System.out.println("getApplicationName: "
					+ storageOptions.getApplicationName() + "\r\tgetClock: " + storageOptions.getClock()
					 + "\r\tgetCredentials: "
					+ storageOptions.getCredentials() + "\r\tgetHost: " + storageOptions.getHost());

			blobId = BlobId.of(bucketName, blobName);
			blob = storage.get(blobId);
			if (blob != null && blob.exists()) {
				System.out.println("Name: "+blob.getName()+
						"\r\tgetMediaLink: "+blob.getMediaLink());
				URL signedUrl = storage.signUrl(BlobInfo.newBuilder(bucketName, blobName).build(), 600,
					     TimeUnit.SECONDS);
				System.out.println("signedUrl: "+signedUrl.toString());
			} else {
				file = new File(
						"/mnt/volume1/Storage2/6e/1b/e1/69/11/d2/4c/96/6e1be169-11d2-4c96-91c7-8f814dd018caC20161102.tar");
				fileInputStream = new FileInputStream(file);
				blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/tar").build();
				blob = storage.create(blobInfo, fileInputStream);
				System.out.println("MD5: " + blob.getMd5());
			}
		} catch (StorageHelperException e) {
			e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
