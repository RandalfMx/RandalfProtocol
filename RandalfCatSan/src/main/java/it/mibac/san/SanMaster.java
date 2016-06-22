/**
 * 
 */
package it.mibac.san;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.xml.sax.SAXParseException;

import it.mibac.san.cat_import.CatImport;
import it.mibac.san.cat_import.CatListRecords;
import it.mibac.san.cat_import.CatRecord;
import it.mibac.san.cat_import.CatRecordBody;
import it.mibac.san.cat_import.CatRecordHeader;
import it.mibac.san.cat_import.Catheader;
import it.mibac.san.cat_import.Contact;
import it.mibac.san.cat_import.Filedesc;
import mx.randalf.parser.Parser;
import mx.randalf.tools.MD5Tools;
import mx.randalf.xsd.exception.XsdException;

/**
 * @author massi
 *
 */
abstract class SanMaster<C> {

	protected CatImport catImport = null;

	private String type = null;

	protected String systemId = null;

	/**
	 * @throws DatatypeConfigurationException 
	 * 
	 */
	public SanMaster(String systemId, String name, String mail, String phone, String title, String myAbstract, String type) throws DatatypeConfigurationException {
		Catheader catheader = null;
		Contact contact = null;
		Filedesc filedesc = null;
				
		try {
			this.type = type;
			this.systemId = systemId;

			catImport =new CatImport();
			
			catheader = new Catheader();

			catheader.setSystemId(systemId);

			contact = new Contact();
			contact.getName().add(name);
			contact.getMail().add(mail);
			contact.getPhone().add(phone);
			catheader.setContact(contact);

			filedesc = new Filedesc();
			filedesc.setTitle(title);
			filedesc.setAbstract(myAbstract);
			filedesc.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
			catheader.setFiledesc(filedesc);
			catImport.setCatheader(catheader);
			catImport.setCatListRecords(new CatListRecords());
		} catch (DatatypeConfigurationException e) {
			throw e;
		}
	}

	protected CatRecordHeader genCatRecordHeader(String id, XMLGregorianCalendar lastUpdate){
		CatRecordHeader catRecordHeader = null;
		
		catRecordHeader = new CatRecordHeader();
		catRecordHeader.setType(type);
		catRecordHeader.setId(id);
		catRecordHeader.setLastUpdate(lastUpdate);
		return catRecordHeader;
	}


	public void add(String id, XMLGregorianCalendar lastUpdate, C dati){
		CatRecord catRecord = null;
		
		catRecord = new CatRecord();
		
		catRecord.setCatRecordHeader(genCatRecordHeader(id, lastUpdate));

		catRecord.setCatRecordBody(genCatRecordBody(dati));
		
		catImport.getCatListRecords().getCatRecord().add(catRecord);
	}

	protected abstract CatRecordBody genCatRecordBody(C dati);
	
	public void write(File fXml) throws SAXParseException, NoSuchAlgorithmException, XsdException, IOException{
		CatImportXsd catImportXsd = null;
		File fCert = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			catImportXsd = new CatImportXsd();
//			new CatImportListener(), 
			catImportXsd.write(catImport, fXml, 
					new CatImportNamespacePrefixMapper(), 
					null, 
					new CatImportXmlAdaptect(), 
					"http://san.mibac.it/cat-import http://www.san.beniculturali.it/tracciato/cat-import.xsd");
			
			if (Parser.checkFile(fXml)){
				try {
					fCert = new File(fXml.getAbsolutePath()+".cert");
					fw = new FileWriter(fCert);
					bw = new BufferedWriter(fw);
					bw.write(MD5Tools.readMD5File(fXml.getAbsolutePath()));
				} catch (FileNotFoundException e) {
					throw e;
				} catch (NoSuchAlgorithmException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} finally {
					if (bw != null){
						bw.flush();
						bw.close();
					}
					if (fw != null){
						fw.close();
					}
				}
			}
		} catch (SAXParseException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	protected String genDate(String date){
		String data = "";
		String[] st = null;
		String result = "00000000/00000000";

		if (date != null) {
			data = date.replace(" - ", "-");
			data = data.replace("REGNO D'ITALIA (", "");
			data = data.replace("REGNO DI NAPOLI (", "");
			data = data.replace(")", "");
			data = data.replace(",", "");
			
			st = data.split(";");
			if (st.length>1){
				data = st[0];
			}
	
			st = data.split("-");
			if (st.length==2){
				if (st[1].indexOf(" ")>-1){
					result = genDate(st[0],"0101")+
							"/"+
							genDate(st[1].substring(0, st[1].indexOf(" ")),"1231");
				} else {
					result = genDate(st[0],"0101")+
							"/"+
							genDate(st[1],"1231");
				}
			} else {
				result = genDate(st[0],"0101")+
						"/"+
						genDate(st[0],"1231");
			}
		}

		return result;
	}

	private String genDate(String date, String vDefault){
		String[] st = null;
		String result = "";
		DecimalFormat df2 = new DecimalFormat("00");
		Hashtable<String, String> mesi = new Hashtable<String, String>();
		
		mesi.put("gen.", "01");
		mesi.put("feb.", "02");
		mesi.put("mar.", "03");
		mesi.put("apr.", "04");
		mesi.put("mag.", "05");
		mesi.put("giu.", "06");
		mesi.put("lug.", "07");
		mesi.put("ago.", "08");
		mesi.put("set.", "09");
		mesi.put("ott.", "10");
		mesi.put("nov.", "11");
		mesi.put("dic.", "12");

		date = date.replace("[", "").replace("]", "");
		date = date.trim();
		st = date.split(" ");
		if (st.length==3){
			result = st[2];
			result += mesi.get(st[1]);
			result += df2.format(new Integer(st[0]));
		} else {
			result = date+vDefault;
		}
		return result;
	}
}
