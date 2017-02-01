/**
 * 
 */
package mx.randalf.protocol.amazonS3;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Upload;

/**
 * @author massi
 *
 */
public class MinioTest {

	/**
	 * 
	 */
	public MinioTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MinioClient minioClient = null;
		List<Bucket> buckets = null;
		boolean isExist = false;
		String nameBucket = "magazzinodigitale";
		Iterable<Result<Item>> myObjects = null;
		Item item = null;
		Iterable<Result<Upload>> uploads = null;
		Upload upload = null;

		try {
			minioClient = new MinioClient("http://192.168.7.153:9100", "ND5X7XQY35U02HF2XLJ8",
					"7tTzMTaP1ZypoCzd9Anvp9q+hzuYSOcweZ9O02kf");

//			minioClient.setTimeout(TimeUnit.SECONDS.toSeconds(100), 
//					TimeUnit.SECONDS.toSeconds(300), 
//					TimeUnit.SECONDS.toSeconds(100));
			buckets = minioClient.listBuckets();
			for (Bucket bucket : buckets) {
				System.out.println(
						"name: " + bucket.name() + "\tsize: " + bucket.size() + "\tcreateDate: " + bucket.creationDate());
			}

			isExist = minioClient.bucketExists(nameBucket);
			if (!isExist) {
				minioClient.makeBucket(nameBucket);
			}

			uploads = minioClient.listIncompleteUploads(nameBucket);
			for (Result<Upload> result : uploads) {
				upload = result.get();
				System.out.println("name: "+upload.name+
						"\r\taggregatedPartSize: "+upload.aggregatedPartSize()+
						"\r\tobjectName: "+upload.objectName()+
						"\r\tsize: "+upload.size()+
						"\r\tstorageClass: "+upload.storageClass()+
						"\r\tuploadId: "+upload.uploadId()+
						"\r\towner.displayName: "+upload.owner().displayName()+
						"\r\towner.id: "+upload.owner().id()
						);
			}
			
			
//			minioClient.putObject(nameBucket,
//					"7a/b5/ff/ed/6b/a6/46/a2/7ab5ffed-6ba6-46a2-a340-d8121e396983C20161115.tar", 
//					"/mnt/volume1/Storage2/7a/b5/ff/ed/6b/a6/46/a2/7ab5ffed-6ba6-46a2-a340-d8121e396983C20161115.tar");
			myObjects = minioClient.listObjects(nameBucket);
			for (Result<Item> result : myObjects) {
				item = result.get();
				System.out.println("name: "+item.name+
						"\r\tetag: "+item.etag()+
						"\r\tobjectName: "+item.objectName()+
						"\r\tobjectSize: "+item.objectSize()+
						"\r\tsize: "+item.size()+
						"\r\tstorageClass: "+item.storageClass()+
						"\r\tisDir: "+item.isDir()+
						"\r\tlastModified: "+item.lastModified()+
						"\r\towner.displayName: "+item.owner().displayName()+
						"\r\towner.id: "+item.owner().id());
				
				Set<String> keySets = item.keySet();
				Iterator<String> iterable = keySets.iterator();
				
				while(iterable.hasNext()){
					String key = iterable.next();
					System.out.println("\t"+key+" - "+item.get(key));
				}
				System.out.println("URL:"+minioClient.presignedGetObject(nameBucket, item.objectName(), 60));
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidEndpointException e) {
			e.printStackTrace();
		} catch (InvalidPortException e) {
			e.printStackTrace();
		} catch (InvalidBucketNameException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InsufficientDataException e) {
			e.printStackTrace();
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (ErrorResponseException e) {
			e.printStackTrace();
		} catch (InternalException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
//		} catch (InvalidArgumentException e) {
//			e.printStackTrace();
		} catch (InvalidExpiresRangeException e) {
			e.printStackTrace();
		}
	}

}
