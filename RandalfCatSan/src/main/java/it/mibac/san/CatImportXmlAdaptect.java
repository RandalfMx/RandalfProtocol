/**
 * 
 */
package it.mibac.san;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author massi
 *
 */
public class CatImportXmlAdaptect extends XmlAdapter<Object, Object> {

	/**
	 * 
	 */
	public CatImportXmlAdaptect() {
	}

	/**
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	public String unmarshal(Object v) throws Exception {
		System.out.println("unMarshal: "+v);
		return null;
	}

	/**
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	public String marshal(Object v) throws Exception {
		System.out.println("Marshal: "+v);
		return null;
	}

}
