/**
 * 
 */
package mx.randalf.protocol.amazonS3.minio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.xmlpull.v1.XmlPullParserException;

import com.amazonaws.services.s3.model.S3Object;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import mx.randalf.protocol.amazonS3.exception.RandalfAmazonS3Exception;
import mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3;

/**
 * @author massi
 *
 */
public class RandalfAmazonS3Minio extends IRandalfAmazonS3<MinioClient> {

	private String endPoint = null;
	private String accessKey = null;
	private String secretKey = null;

	/**
	 * 
	 */
	public RandalfAmazonS3Minio(String endPoint, String accessKey, String secretKey) {
		this.endPoint = endPoint;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	/**
	 * @throws RandalfAmazonS3Exception 
	 * @see mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3#sendFile(java.io.File, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendFile(MinioClient minioClient, File fileInput, String contentType, String md5Base64, 
			String md5, String bucketName, String fileOutput) throws RandalfAmazonS3Exception {
		boolean result = false;

		try {
			minioClient.putObject(bucketName, fileOutput, fileInput.getAbsolutePath());
			result = true;
		} catch (InvalidKeyException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InvalidBucketNameException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InsufficientDataException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (NoResponseException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (ErrorResponseException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InternalException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InvalidArgumentException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (IOException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (XmlPullParserException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		return result;
	}

	@Override
	protected boolean exists(MinioClient minioClient, String bucketName, String fileOutput) throws RandalfAmazonS3Exception{
		Iterable<Result<Item>> myObjects = null;
		Item item = null;
		boolean exists = false;

		try {
			myObjects = minioClient.listObjects(bucketName, fileOutput);
			for (Result<Item> result : myObjects) {
				item = result.get();
				if (item.objectName().equals(fileOutput)){
					exists = true;
					break;
				}
			}
		} catch (InvalidKeyException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InvalidBucketNameException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InsufficientDataException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (NoResponseException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (ErrorResponseException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InternalException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (XmlPullParserException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (IOException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		return exists;
	}

	@Override
	protected boolean isValid(MinioClient minioClient, String bucketName, String fileOutput, String md5Base64, String md5) throws RandalfAmazonS3Exception{
		return exists(minioClient, bucketName, fileOutput);
	}

	protected MinioClient openConn(String bucketName) throws RandalfAmazonS3Exception{
		MinioClient minioClient= null;
		boolean isExist = false;

		try {
			minioClient = new MinioClient(endPoint, accessKey, secretKey);

			isExist = minioClient.bucketExists(bucketName);
			if (!isExist) {
				minioClient.makeBucket(bucketName);
			}
		} catch (InvalidEndpointException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InvalidPortException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InvalidBucketNameException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InsufficientDataException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (NoResponseException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (ErrorResponseException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (InternalException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (IOException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (XmlPullParserException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}

		return minioClient;
	}

	@Override
	protected S3Object readInfo(MinioClient storage, String bucketName, String fileInput) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected InputStream getFile(MinioClient storage, String bucketName, String fileInput, Integer start, Integer end)
			throws RandalfAmazonS3Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
