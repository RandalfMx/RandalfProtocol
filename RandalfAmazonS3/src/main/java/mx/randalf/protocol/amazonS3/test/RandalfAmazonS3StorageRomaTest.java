/**
 * 
 */
package mx.randalf.protocol.amazonS3.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.S3Object;

import mx.randalf.protocol.amazonS3.RandalfAmazonS3;
import mx.randalf.protocol.amazonS3.exception.RandalfAmazonS3Exception;

/**
 * @author massi
 *
 */
public class RandalfAmazonS3StorageRomaTest {

	private RandalfAmazonS3 randalfAmazonS3 = null;

	private String bucketName = "Storage";

	/**
	 * 
	 */
	public RandalfAmazonS3StorageRomaTest() {
	
		randalfAmazonS3 = new RandalfAmazonS3("https://cs2.cloudspc.it:8079", Regions.US_EAST_1, "465ffb75913244be96a292672ba7850c",
				"5662f5554926477584a0c0b6998fdec2");
				
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		System.out.println(fmt.format(new GregorianCalendar().getTime()));
		RandalfAmazonS3StorageRomaTest randalfAmazonS3StorageRomaTest = null;
		
		randalfAmazonS3StorageRomaTest = new RandalfAmazonS3StorageRomaTest();
		if (args[0].equals("PUT")){
			randalfAmazonS3StorageRomaTest.sendFile(args[1], args[2], args[3]);
		} else if (args[0].equals("GET")){
			randalfAmazonS3StorageRomaTest.getFile(args[1], args[2]);
		} else if (args[0].equals("READINFO")){
			randalfAmazonS3StorageRomaTest.readInfo(args[1]);
		}
		System.out.println(fmt.format(new GregorianCalendar().getTime()));
	}

	public void readInfo(String fileInput) {
		S3Object s3Object = null;

		try {
			s3Object = randalfAmazonS3.readInfo(bucketName, fileInput);
			
			RandalfAmazonS3.printS3Object(s3Object);
		} catch (RandalfAmazonS3Exception e) {
			e.printStackTrace();
		}
	}

	public void sendFile(String fileInput, String contentType, String fileOutput) {
		try {
			if (randalfAmazonS3.sendFile(new File(fileInput), contentType, bucketName, fileOutput)){
				System.out.println("Il file ["+fileInput+"] è stato inviato");
			} else {
				System.out.println("Il file ["+fileInput+"] FALLITO");
			}
		} catch (RandalfAmazonS3Exception e) {
			e.printStackTrace();
		}
	}

	public void getFile(String fileInput, String fileOutput){
		InputStream inputStream = null;
		File f = null;
		FileOutputStream fos = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		
		try {
			inputStream = randalfAmazonS3.getFile(bucketName, fileInput);
			
			f = new File(fileOutput);
			fos = new FileOutputStream(f);
			

			while ((read = inputStream.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (RandalfAmazonS3Exception e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null){
					fos.flush();
					fos.close();
				}
				if (inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
	}
}
