/**
 * 
 */
package mx.randalf.protocol.unimarc.test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import mx.randalf.protocol.unimarc.RandalfUnimarcDSpace;
import mx.randalf.standard.dSpace.Dublin_core;

/**
 * @author massi
 *
 */
public class RandalfUnimarcTest {

	/**
	 * 
	 */
	public RandalfUnimarcTest() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RandalfUnimarcDSpace randalfUnimarc = null;
		Hashtable<String, Dublin_core> records = null;
		JAXBContext jaxbContext = null;
		Marshaller marshaller = null;
		
		try {
			if (args.length==1) {
				
				randalfUnimarc = new RandalfUnimarcDSpace();
				records = randalfUnimarc.get(new File(args[0]));

				jaxbContext = JAXBContext.newInstance(Dublin_core.class);
				marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				for (String key: records.keySet()) {
//					System.out.print(key+" => ");
//					marshaller.marshal(records.get(key),System.out);
				}
			} else {
				System.out.println("Indicare il nome del file da analizzare");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
