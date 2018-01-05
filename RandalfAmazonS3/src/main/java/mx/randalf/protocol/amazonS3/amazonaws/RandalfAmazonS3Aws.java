/**
 * 
 */
package mx.randalf.protocol.amazonS3.amazonaws;

import java.io.File;
import java.io.InputStream;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import mx.randalf.protocol.amazonS3.RandalfAmazonS3;
import mx.randalf.protocol.amazonS3.exception.RandalfAmazonS3Exception;
import mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3;

/**
 * @author massi
 *
 */
public class RandalfAmazonS3Aws extends IRandalfAmazonS3<AmazonS3> {

//	private String profileName = null;
	private String endPoint = null;
	private Regions regions = null;
	private String accessKey = null;
	private String secretKey = null;

	/**
	 * 
	 */
	public RandalfAmazonS3Aws(String endPoint, Regions regions, String accessKey,
			String secretKey) {
		this.endPoint = endPoint;
		this.regions = regions;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	/**
	 * @see mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3#sendFile(java.io.File,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendFile(AmazonS3 storage, File fileInput, String contentType, String md5Base64, String bucketName,
			String fileOutput) throws RandalfAmazonS3Exception {
		boolean result = false;
		PutObjectRequest putObjectRequest = null;
		PutObjectResult putObjectResult = null;

		try {
			putObjectRequest = new PutObjectRequest(bucketName, fileOutput, fileInput);

			putObjectResult = storage.putObject(putObjectRequest);
			
			System.out.println("md5Base64: "+md5Base64);
			RandalfAmazonS3.printPutObjectResult(putObjectResult);

			if (putObjectResult.getContentMd5().equals(md5Base64)) {
				result = true;
			}
		} catch (Exception e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		return result;
	}

	@Override
	protected boolean exists(AmazonS3 storage, String bucketName, String fileOutput)
			throws RandalfAmazonS3Exception {
		boolean exists = false;

		try {
			if (storage.doesObjectExist(bucketName, fileOutput)) {
				exists = true;
			}
		} catch (AmazonServiceException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (SdkClientException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		return exists;
	}

	@Override
	protected boolean isValid(AmazonS3 storage, String bucketName, String fileOutput, String md5Base64, String md5)
			throws RandalfAmazonS3Exception {
		boolean exists = false;
		S3Object s3Object = null;

		try {
			if (exists(storage, bucketName, fileOutput)) {
				s3Object = storage.getObject(new GetObjectRequest(bucketName, fileOutput));
				if ((s3Object.getObjectMetadata().getContentMD5() != null && 
						s3Object.getObjectMetadata().getContentMD5().equals(md5Base64)) ||
						(s3Object.getObjectMetadata().getETag() != null && 
							s3Object.getObjectMetadata().getETag().equals(md5))) {
					exists = true;
				} else {
					throw new RandalfAmazonS3Exception(
							"Il file risulta presente nello Storage ma la chiave MD5 non corrisponde");
				}
			}
		} catch (RandalfAmazonS3Exception e) {
			throw e;
		} catch (AmazonServiceException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (SdkClientException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		return exists;
	}

	@Override
	protected AmazonS3 openConn(String bucketName) throws RandalfAmazonS3Exception {
		BasicAWSCredentials creds = null;
		AmazonS3 s3Client = null;
		EndpointConfiguration endpointConfiguration = null;

		creds = new BasicAWSCredentials(accessKey, secretKey);
		if (endPoint == null) {
			s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
					.withRegion(regions).build();
		} else {
			//-Dhttps.protocols=TLSv1.2
			endpointConfiguration = new EndpointConfiguration(endPoint, regions.getName());
			ClientConfiguration clientConfiguration = new ClientConfiguration();
			clientConfiguration.setSignerOverride("S3SignerType");
			s3Client = AmazonS3ClientBuilder.
					standard().
					withCredentials(new AWSStaticCredentialsProvider(creds))
					.withEndpointConfiguration(endpointConfiguration).
					withForceGlobalBucketAccessEnabled(true).
					withPayloadSigningEnabled(false).
					withClientConfiguration(clientConfiguration).
					build();
//			disableCertificates();
		}

		if (!s3Client.doesBucketExistV2(bucketName)) {
			System.out.println("Create Bycket [" + bucketName + "]");
			if (endPoint != null) {
				s3Client.setEndpoint(endPoint);
			}
			s3Client.createBucket(bucketName);
		} else {
			System.out.println("Esiste Bycket [" + bucketName + "]");
		}
		return s3Client;
	}

	@Override
	protected InputStream getFile(AmazonS3 storage, String bucketName, String fileInput, Integer start, Integer end) throws RandalfAmazonS3Exception {
		GetObjectRequest rangeObjectRequest = null;
		S3Object s3Object = null;
		InputStream objectData = null;

		try {
			if (start != null && end != null){
				rangeObjectRequest = new GetObjectRequest(
						bucketName, fileInput);
				rangeObjectRequest.setRange(start, end); // retrieve 1st 11 bytes.
	
				s3Object = storage.getObject(rangeObjectRequest);
			} else {
				s3Object = storage.getObject(new GetObjectRequest(bucketName, fileInput));

			}

			objectData = s3Object.getObjectContent();
		} catch (Exception e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		
		return objectData;
	}

	@Override
	protected S3Object readInfo(AmazonS3 storage, String bucketName, String fileInput) {
		S3Object s3Object = null;
		s3Object = storage.getObject(new GetObjectRequest(bucketName, fileInput));
		return s3Object;
	}
}
