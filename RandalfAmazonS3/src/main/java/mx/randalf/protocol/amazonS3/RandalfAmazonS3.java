package mx.randalf.protocol.amazonS3;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import mx.randalf.protocol.amazonS3.amazonaws.RandalfAmazonS3Aws;
import mx.randalf.protocol.amazonS3.exception.RandalfAmazonS3Exception;
import mx.randalf.protocol.amazonS3.googleCloud.RandalfAmazonS3GoogleCloud;
import mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3;
import mx.randalf.protocol.amazonS3.minio.RandalfAmazonS3Minio;
import mx.randalf.protocol.amazonS3.protocol.TypeProtocol;

public class RandalfAmazonS3 {

	private TypeProtocol typeProtocol = null;

	private IRandalfAmazonS3<?> storage = null;

	/**
	 * Costruttore utilizzato per la gestione del Protocollo S3 per Minio
	 * 
	 * @param endPoint
	 * @param accessKey
	 * @param secretKey
	 */
	public RandalfAmazonS3(String endPoint, String accessKey, String secretKey) {
		this.typeProtocol = TypeProtocol.MINIO;
		storage = new RandalfAmazonS3Minio(endPoint, accessKey, secretKey);
	}

	/**
	 * Costruttore utilizzato per la gestione del Protocollo S3 per AmazonWs
	 * 
	 * @param profileName
	 * @param endPoint
	 * @param regions
	 */
	public RandalfAmazonS3(String endPoint, Regions regions, String accessKey,
			String secretKey){
		this.typeProtocol = TypeProtocol.AMAZONAWS;
		storage = new RandalfAmazonS3Aws(endPoint, regions, accessKey, secretKey);
	}

	/**
	 * Costruttore utilizzato per la gestione del Protocollo S3 per Google Cloud
	 * @throws RandalfAmazonS3Exception 
	 */
	public RandalfAmazonS3() throws RandalfAmazonS3Exception{
		this.typeProtocol = TypeProtocol.GOOGLECLOUD;
		storage = new RandalfAmazonS3GoogleCloud();
	}

	/**
	 * @return the typeProtocol
	 */
	public TypeProtocol getTypeProtocol() {
		return typeProtocol;
	}
	
	public  boolean sendFile(File fileInput, String contentType, String bucketName, String fileOutput) throws RandalfAmazonS3Exception{
		return storage.sendFile(fileInput, contentType, bucketName, fileOutput);
	}
	
	public InputStream getFile(String bucketName, String fileInput) throws RandalfAmazonS3Exception{
		return storage.getFile(bucketName, fileInput);
	}
	
	public  InputStream getFile(String bucketName, String fileInput, Long start, Long end) throws RandalfAmazonS3Exception{
		return storage.getFile(bucketName, fileInput, start, end);
	}
	
	public  S3Object readInfo(String bucketName, String fileInput) throws RandalfAmazonS3Exception{
		return storage.readInfo(bucketName, fileInput);
	}
	
	public Boolean exists(String bucketName, String fileInput) throws RandalfAmazonS3Exception{
		return storage.exists( bucketName, fileInput);
	}
	
	public  Boolean isValid(String bucketName, String fileInput, String md5Base64, String md5) throws RandalfAmazonS3Exception{
		return storage.isValid(bucketName, fileInput, md5Base64, md5);
	}

	public static void printS3Object(S3Object s3Object){

		System.out.println("getBucketName: "+s3Object.getBucketName());
		System.out.println("getKey: "+s3Object.getKey());

		printS3Object(s3Object.getObjectMetadata());

		System.out.println("getRedirectLocation: "+s3Object.getRedirectLocation());
		System.out.println("getTaggingCount: "+s3Object.getTaggingCount());
		System.out.println("isRequesterCharged: "+s3Object.isRequesterCharged());
	}
	
	@SuppressWarnings("deprecation")
	public static void printPutObjectResult (PutObjectResult putObjectResult){
		System.out.println("getContentMd5: "+putObjectResult.getContentMd5());
		System.out.println("getETag: "+putObjectResult.getETag());
		System.out.println("getExpirationTime: "+putObjectResult.getExpirationTime());
		System.out.println("getExpirationTimeRuleId: "+putObjectResult.getExpirationTimeRuleId());

		printS3Object(putObjectResult.getMetadata());

		System.out.println("getServerSideEncryption: "+putObjectResult.getServerSideEncryption());
		System.out.println("getSSEAlgorithm: "+putObjectResult.getSSEAlgorithm());
		System.out.println("getSSECustomerAlgorithm: "+putObjectResult.getSSECustomerAlgorithm());
		System.out.println("getSSECustomerKeyMd5: "+putObjectResult.getSSECustomerKeyMd5());
		System.out.println("getVersionId: "+putObjectResult.getVersionId());
		
	}
	
	@SuppressWarnings("deprecation")
	public static void printS3Object(ObjectMetadata objectMetadata){
		Map<String, Object> rawMetadata = null;
		Map<String, String> userMetadata = null;

		System.out.println("getObjectMetadata");
		System.out.println("\tgetCacheControl: "+objectMetadata.getCacheControl());
		System.out.println("\tgetContentDisposition: "+objectMetadata.getContentDisposition());
		System.out.println("\tgetContentEncoding: "+objectMetadata.getContentEncoding());
		System.out.println("\tgetContentLanguage: "+objectMetadata.getContentLanguage());
		System.out.println("\tgetContentLength: "+objectMetadata.getContentLength());
		System.out.println("\tgetContentMD5: "+objectMetadata.getContentMD5());
		System.out.println("\tgetContentType: "+objectMetadata.getContentType());
		System.out.println("\tgetETag: "+objectMetadata.getETag());
		System.out.println("\tgetExpirationTime: "+objectMetadata.getExpirationTime());
		System.out.println("\tgetExpirationTimeRuleId: "+objectMetadata.getExpirationTimeRuleId());
		System.out.println("\tgetHttpExpiresDate: "+objectMetadata.getHttpExpiresDate());
		System.out.println("\tgetInstanceLength: "+objectMetadata.getInstanceLength());
		System.out.println("\tgetLastModified: "+objectMetadata.getLastModified());
		System.out.println("\tgetOngoingRestore: "+objectMetadata.getOngoingRestore());
		System.out.println("\tgetPartCount: "+objectMetadata.getPartCount());
		System.out.println("\tgetRawMetadata: ");
		rawMetadata = objectMetadata.getRawMetadata();
		for(String key:rawMetadata.keySet()){
			System.out.println("\t\t"+key+": "+rawMetadata.get(key));
		}
		System.out.println("\tgetReplicationStatus: "+objectMetadata.getReplicationStatus());
		System.out.println("\tgetRestoreExpirationTime: "+objectMetadata.getRestoreExpirationTime());
		System.out.println("\tgetServerSideEncryption: "+objectMetadata.getServerSideEncryption());
		System.out.println("\tgetSSEAlgorithm: "+objectMetadata.getSSEAlgorithm());
		System.out.println("\tgetSSEAwsKmsKeyId: "+objectMetadata.getSSEAwsKmsKeyId());
		System.out.println("\tgetSSECustomerAlgorithm: "+objectMetadata.getSSECustomerAlgorithm());
		System.out.println("\tgetSSECustomerKeyMd5: "+objectMetadata.getSSECustomerKeyMd5());
		System.out.println("\tgetStorageClass: "+objectMetadata.getStorageClass());
		System.out.println("\tgetUserMetadata: ");
		userMetadata =objectMetadata.getUserMetadata();
		for(String key:userMetadata.keySet()){
			System.out.println("\t\t"+key+": "+userMetadata.get(key));
		}
		System.out.println("\tgetVersionId: "+objectMetadata.getVersionId());
	}
}
