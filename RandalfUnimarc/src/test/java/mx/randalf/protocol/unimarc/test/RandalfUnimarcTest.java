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

		try {
			if (args.length==2) {
				randalfUnimarc = new RandalfUnimarcDSpace();
				randalfUnimarc.convertXml(new File(args[0]), new File(args[1]));
			} else {
				System.out.println("Indicare il nome del file mrc e il file xml");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Hashtable<String, Dublin_core> records = null;
//		Hashtable<String, String> xmls = null;
//		JAXBContext jaxbContext = null;
//		Marshaller marshaller = null;
//		
//		try {
//			if (args.length==1) {
//				
//				randalfUnimarc = new RandalfUnimarcDSpace();
//				xmls = randalfUnimarc.toXml(new File(args[0]));
//				for (String key: xmls.keySet()) {
//					if (key.equals("VEAE006569")) {
//					System.out.print(key+" => "+xmls.get(key));
//					}
//				}
//				records = randalfUnimarc.get(new File(args[0]));
//
//				jaxbContext = JAXBContext.newInstance(Dublin_core.class);
//				marshaller = jaxbContext.createMarshaller();
//				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
//				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true); // Elimina la dichiarazione <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
//				
//				for (String key: records.keySet()) {
//					if (key.equals("VEAE006569")) {
//						System.out.print(key+" => ");
//						marshaller.marshal(records.get(key),System.out);
//					}
//				}
//			} else {
//				System.out.println("Indicare il nome del file da analizzare");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
	}

}
