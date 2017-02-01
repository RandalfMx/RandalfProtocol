/**
 * 
 */
package mx.randalf.protocol.amazonS3.amazonaws;

import java.io.File;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import mx.randalf.protocol.amazonS3.exception.RandalfAmazonS3Exception;
import mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3;

/**
 * @author massi
 *
 */
public class RandalfAmazonS3Aws extends IRandalfAmazonS3<AmazonS3> {
	
	private String profileName = null;
	private String endPoint = null;
	private Regions regions = null;

	/**
	 * 
	 */
	public RandalfAmazonS3Aws(String profileName, String endPoint, Regions regions) {
		this.profileName = profileName;
		this.endPoint = endPoint;
		this.regions = regions;
	}

	/**
	 * @see mx.randalf.protocol.amazonS3.interfaces.IRandalfAmazonS3#sendFile(java.io.File, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendFile(AmazonS3 storage, File fileInput, String contentType, String md5Base64, 
			String bucketName, String fileOutput) throws RandalfAmazonS3Exception {
		boolean result = false;
		PutObjectRequest putObjectRequest = null;
		PutObjectResult putObjectResult = null;

		try {
			putObjectRequest = new PutObjectRequest(bucketName, fileOutput, fileInput);
			putObjectResult = storage.putObject(putObjectRequest);
			if (putObjectResult.getContentMd5().equals(md5Base64)){
				result=true;
			}
		} catch (Exception e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
		return result;
	}

	@Override
	protected boolean exists(AmazonS3 storage, String bucketName, String fileOutput, String md5) throws RandalfAmazonS3Exception{
		boolean exists=false;
		S3Object s3Object = null;
		
		try {
			if (storage.doesObjectExist(bucketName, fileOutput)){
				s3Object = storage.getObject(new GetObjectRequest(bucketName, fileOutput));
				if (s3Object.getObjectMetadata().getContentMD5().equals(md5)){
					exists = true;
				} else {
					throw new RandalfAmazonS3Exception("Il file risulta presente nello Storage ma l chiave MD5 non corrisponde");
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
	protected AmazonS3 openConn(String bucketName) throws RandalfAmazonS3Exception{
		AmazonS3 storage = null;
        AWSCredentials credentials = null;
        Region region = null;

        try {
			try {
				if (profileName == null){
					credentials = new ProfileCredentialsProvider().getCredentials();
				} else {
					credentials = new ProfileCredentialsProvider(profileName).getCredentials();
				}
			} catch (Exception e) {
			    throw new RandalfAmazonS3Exception(
			            "Impossibile caricare le credenziali dal file delle credenziale. " +
			            "Si prega di assicurarsi che il file di credenziali è nella posizione "+
			            "corretta (~/.aws/credentials), ed è in un formato valido.",
			            e);
			}

			storage = new AmazonS3Client(credentials);
			if (endPoint != null){
				storage.setEndpoint(endPoint);
			}
			
			region = Region.getRegion(regions);
			storage.setRegion(region);

			if (!storage.doesBucketExist(bucketName)){
			    storage.createBucket(bucketName);
			}
		} catch (RandalfAmazonS3Exception e) {
			throw e;
		} catch (AmazonServiceException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (SdkClientException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new RandalfAmazonS3Exception(e.getMessage(), e);
		}
        return storage;
	}
}
