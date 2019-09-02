/**
 * 
 */
package mx.randalf.protocol.sbn.client.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Vector;

import mx.randalf.protocol.sbn.client.RandalfSbnClient;
import mx.randalf.protocol.sbn.metadata.RandalfMetadata;

/**
 * @author massi
 *
 */
public class RandalfSbnClientTest {

	/**
	 * 
	 */
	public RandalfSbnClientTest() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RandalfSbnClient randalfSbnClient = null;
		Vector<RandalfMetadata> randalfMetadatas = null;
		
		try {
			randalfSbnClient = new RandalfSbnClient();
//			randalfSbnClient.bidMarc("IEI0111767", "IEI0111767.mrc");
			randalfMetadatas = randalfSbnClient.bidToMetaData("IEI0111767");
			for (RandalfMetadata randalfMetadata: randalfMetadatas) {
				System.out.println("-------------------");
				print(randalfMetadata.getContributors(),"Contributors");
				print(randalfMetadata.getCoverages(),"Coverages");
				print(randalfMetadata.getCreators(),"Creators");
				print(randalfMetadata.getDates(),"Dates");
				print(randalfMetadata.getDescriptions(),"Descriptions");
				print(randalfMetadata.getFormats(),"Formats");
				print(randalfMetadata.getIdentifiers(),"Identifiers");
				print(randalfMetadata.getLanguages(),"Languages");
				print(randalfMetadata.getPublishers(),"Publishers");
				print(randalfMetadata.getRelations(),"Relations");
				print(randalfMetadata.getRights(),"Rights");
				print(randalfMetadata.getSources(),"Sources");
				print(randalfMetadata.getSubjects(),"Subjects");
				print(randalfMetadata.getTitles(),"Titles");
				print(randalfMetadata.getTypes(),"Types");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void print(List<String> values, String title) {
		
		if (values!= null) {
			System.out.println(title);
			for (String value: values) {
				System.out.println("\t"+value);
			}
		}
	}

}
