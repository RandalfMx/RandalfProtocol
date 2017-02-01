package mx.randalf.protocol.amazonS3;

import java.io.File;

import com.amazonaws.regions.Regions;

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
	public RandalfAmazonS3(String profileName, String endPoint, Regions regions){
		this.typeProtocol = TypeProtocol.AMAZONAWS;
		storage = new RandalfAmazonS3Aws(profileName, endPoint, regions);
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
}
