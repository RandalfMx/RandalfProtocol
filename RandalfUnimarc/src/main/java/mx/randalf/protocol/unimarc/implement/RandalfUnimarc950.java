/**
 * 
 */
package mx.randalf.protocol.unimarc.implement;

import java.util.Hashtable;
import java.util.Vector;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Subfield;

/**
 * @author massi
 *
 */
public class RandalfUnimarc950 {

	private String nomeIstituto= null;

	private Hashtable<String, Vector<Subfield>> values = null;
	
	
	/**
	 * 
	 */
	public RandalfUnimarc950(DataField value) {
		init(value);
	}

	private void init(DataField value) {
		String segnatura = null;
		Vector<Subfield> subfields = null;
		String testo = "";

		for (Subfield sf: value.getSubfields()) {
			switch (sf.getCode()) {
			case 'a':
				nomeIstituto = sf.getData();
				break;
			case 'b':
			case 'c':
				if (segnatura != null) {
					if (subfields != null) {
						if (values == null) {
							values = new Hashtable<String, Vector<Subfield>>();
						}
						values.put(segnatura, subfields);
					}
					segnatura = null;
					subfields = null;
				}
				if (subfields == null) {
					subfields = new Vector<Subfield>();
				}
				subfields.add(sf);
				break;
			case 'd':
				if (segnatura != null) {
					if (subfields != null) {
						if (values == null) {
							values = new Hashtable<String, Vector<Subfield>>();
						}
						values.put(segnatura, subfields);
					}
					segnatura = null;
					subfields = null;
				}
				testo = sf.getData();
				segnatura ="";
				if (testo.length() > 3 && testo.length() > 12) {
					segnatura += testo.substring(3, 13).trim();
				} else if (testo.length() > 3) {
					segnatura += testo.substring(3).trim();
				}

				if (testo.length() > 13 && testo.length() > 36) {
					segnatura += (testo.equals("") ? "" : " ") + testo.substring(13, 37).trim();
				} else if (testo.length() > 13) {
					segnatura += (testo.equals("") ? "" : " ") + testo.substring(13).trim();
				}

				if (testo.length() > 37) {
					segnatura += (testo.equals("") ? "" : " ") + testo.substring(37).trim();
				}

				if (subfields == null) {
					subfields = new Vector<Subfield>();
				}
				subfields.add(sf);
				break;

			default:
				if (subfields == null) {
					subfields = new Vector<Subfield>();
				}
				subfields.add(sf);
				break;
			}
		}
		if (segnatura != null) {
			if (subfields != null) {
				if (values == null) {
					values = new Hashtable<String, Vector<Subfield>>();
				}
				values.put(segnatura, subfields);
			}
		}
	}

	public String getNomeIstituto() {
		return nomeIstituto;
	}

	public Vector<Subfield> getValues(String segnatura){
		Vector<Subfield> result = null;
		for (String key: values.keySet()) {
			if (key.trim().equalsIgnoreCase(segnatura.trim())) {
				result = values.get(key);
				break;
			}
		}
		return result;
	}
}
