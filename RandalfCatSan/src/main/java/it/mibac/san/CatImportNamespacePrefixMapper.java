/**
 * 
 */
package it.mibac.san;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * @author massi
 *
 */
public class CatImportNamespacePrefixMapper extends NamespacePrefixMapper {

	/**
	 * 
	 */
	public CatImportNamespacePrefixMapper() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreferredPrefix(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		String prefix = null;

		if (namespaceUri.equals("http://san.mibac.it/cat-import")){
			prefix = "";
		} else if (namespaceUri.equals("http://san.mibac.it/scons-san/")){
			prefix = "scons";
			requirePrefix= true;
		} else if (namespaceUri.equals("http://san.mibac.it/ead-san/")){
			prefix = "ead-complarc";
		} else if (namespaceUri.equals("http://san.mibac.it/eac-san/")){
			prefix = "eac-cpf";
		} else if (namespaceUri.equals("http://www.w3.org/1999/xlink")){
			prefix = "xlink";
		} else if (namespaceUri.equals("http://san.mibac.it/ricerca-san/")){
			prefix = "ead-str";
		} else if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema-instance")){
			prefix = "xsi";
		} else {
			System.out.println("namespaceUri: "+namespaceUri+"\tsuggestion: "+suggestion+"\trequirePrefix: "+requirePrefix);
		}
//				xmlns="http://san.mibac.it/cat-import" 
//				xsi:schemaLocation="http://san.mibac.it/cat-import http://www.san.beniculturali.it/tracciato/cat-import.xsd">				
		
		return prefix;
	}

}
