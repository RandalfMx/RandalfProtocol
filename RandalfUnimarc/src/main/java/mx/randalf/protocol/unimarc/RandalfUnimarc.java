/**
 * 
 */
package mx.randalf.protocol.unimarc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.marc4j.MarcReader;
import org.marc4j.MarcReaderConfig;
import org.marc4j.MarcReaderFactory;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarc <D> {

	private boolean prermissiveReader = false;

	private boolean toUtf8 = false;

	/**
	 * 
	 */
	public RandalfUnimarc(boolean prermissiveReader, boolean toUtf8) {
		this.prermissiveReader = prermissiveReader;
		this.toUtf8 = toUtf8;
	}

	public Hashtable<String, Record> init(File fMrc) throws IOException {
		FileInputStream fis = null;
		Hashtable<String, Record> result = null;

		try {
			fis = new FileInputStream(fMrc);
			result = init(fis);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		return result;
	}

	public Hashtable<String, Record> init(InputStream fMrc) throws IOException {
		MarcReader marcReader = null;
		MarcReaderConfig marcReaderConfig = null;
		Record record = null;
		List<ControlField> controlFields = null;
		Hashtable<String, Record> result = null;
		String bid = "";

		try {
			marcReaderConfig = new MarcReaderConfig();
			marcReaderConfig.setPermissiveReader(prermissiveReader);
			marcReaderConfig.setToUtf8(toUtf8);
			marcReader = MarcReaderFactory.makeReader(marcReaderConfig, fMrc);
			while (marcReader.hasNext()) {
				try {
					record = marcReader.next();
					controlFields = getCF(record.getControlFields(), "001");
					if (result == null) {
						result = new Hashtable<String, Record>();
					}
					bid = controlFields.get(0).getData();
					if (bid.startsWith("IT\\ICCU\\")) {
						bid = bid.substring(8).replace("\\", "");
					}
					result.put(bid, record);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}

	public void convertXml(File fMrc, File fXml) throws IOException {
		FileInputStream fis = null;
		MarcReaderConfig marcReaderConfig = null;
		MarcReader marcReader = null;
		MarcXmlWriter marcXmlWriter = null;
		Record record = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(fMrc);

			marcReaderConfig = new MarcReaderConfig();
			marcReaderConfig.setPermissiveReader(prermissiveReader);
			marcReaderConfig.setToUtf8(toUtf8);
			marcReader = MarcReaderFactory.makeReader(marcReaderConfig, fis);

			fos = new FileOutputStream(fXml);
			marcXmlWriter = new MarcXmlWriter(fos, "UTF-8");
			while(marcReader.hasNext()){
				record = marcReader.next();
				marcXmlWriter.write(record);
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (marcXmlWriter != null) {
				marcXmlWriter.close();
			}
			if (fos != null) {
				fos.flush();
				fos.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	public Hashtable<String, String> toXml(File fMrc) throws IOException {
		Hashtable<String, String> result = null;
		Hashtable<String, Record> records = null;
		ByteArrayOutputStream baos = null;
		MarcXmlWriter mxw =null;

		try {
			records = init(fMrc);
			for (String key : records.keySet()) {
				baos = new ByteArrayOutputStream();
				mxw = new MarcXmlWriter(baos, "UTF-8");
				mxw.write(records.get(key));
				mxw.close();
				if (result == null) {
					result = new Hashtable<String, String>();
				}
				result.put(key, baos.toString());
			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}

	public static Record getRecord(String xmlMrc) {
		MarcXmlReader mxr = null;
		ByteArrayInputStream bais = null;
		Record record = null;
		
		bais = new ByteArrayInputStream(xmlMrc.getBytes());
		mxr = new MarcXmlReader(bais);
		record = mxr.next();
		
		return record;
	}

	public D get(String xmlMrc) {
		return get(getRecord(xmlMrc));
	}

	public D get(Record record) {
		D bib = null;
		
		bib = init(record);
		return bib;
	}

	public Hashtable<String, D> get(File fMrc) throws IOException {
		Hashtable<String, D> result = null;
		Hashtable<String, Record> records = null;
		D rec = null;
		
		try {
			records = init(fMrc);
			for (String key : records.keySet()) {
				rec = init(records.get(key));
				if (result == null) {
					result = new Hashtable<String, D>();
				}
				result.put(key, rec);
			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}

	protected abstract D init(Record record);

	protected List<DataField> getDF(List<DataField> dataFields, String key) {
		List<DataField> result = null;

		for (DataField dataField : dataFields) {
			if (dataField.getTag().equals(key)) {
				if (result == null) {
					result = new Vector<DataField>();
				}
				result.add(dataField);
			}
		}
		return result;
	}

	protected List<ControlField> getCF(List<ControlField> controlFields, String key) {
		List<ControlField> result = null;
		for (ControlField controlField : controlFields) {
			if (controlField.getTag().equals(key)) {
				if (result == null) {
					result = new Vector<ControlField>();
				}
				result.add(controlField);
			}
		}
		return result;
	}
}
