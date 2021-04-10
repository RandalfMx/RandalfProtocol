/**
 * 
 */
package mx.randalf.protocol.unimarc.implement;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.purl.dc.elements._1.SimpleLiteral;

import it.sbn.iccu.metaag1.Bib;
import it.sbn.iccu.metaag1.Bib.LocalBib;

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarcMaterialeCartografico extends RandalfUnimarcMaterialeGrafico {

	/**
	 * @param prermissiveReader
	 * @param toUtf8
	 */
	public RandalfUnimarcMaterialeCartografico(Vector<String> segnatura, String nomeIstituto, String codiceIsil) {
		super(segnatura, nomeIstituto, codiceIsil);
	}

	@Override
	protected Collection<? extends SimpleLiteral> getSubject(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		result = getSubject606(record);

		values = getDF(record.getDataFields(), "606");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData();
				}
				sf = value.getSubfield('x');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " - ") + sf.getData();
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(testo.trim()));
				}
			}
		}

		result2 = getSubject676(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		return result;
	}

	@Override
	protected Collection<? extends LocalBib> getLocalBib(Record record) {
		Vector<LocalBib> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		LocalBib localBib = null;

		values = getDF(record.getDataFields(), "206");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf.getData() != null && !sf.getData().trim().equals("")) {
					if (result == null) {
						result = new Vector<Bib.LocalBib>();
					}
					localBib = new LocalBib();
					localBib.getGeoCoord().add(sf.getData().trim());
					result.add(localBib);
				}
			}
		}
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getDescription(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getDescription950_316(record);

		result2 = getDescription303(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getDescription921(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getDescription300_NoSegna(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getRelation(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getRelation410(record);

		result2 = getRelation423(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation461(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation462(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation463(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation464(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation488(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation500(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation510(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation517(record);
		if (result2 != null) {
			result.addAll(result2);
		}
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getFormat(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;
		String testo2 = null;

		values = getDF(record.getDataFields(), "215");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				if (value.getSubfield('a') != null) {
					sf = value.getSubfield('a');
					testo = sf.getData().trim();
					testo2 = getFormat124(record, testo);

					if (testo2 != null) {
						testo += " " + testo2;
					}
				}

				if (value.getSubfield('c') != null) {
					sf = value.getSubfield('c');

					testo += (testo.trim().equals("") ? "" : " : ") + sf.getData().trim();
				}

				if (value.getSubfield('d') != null) {
					sf = value.getSubfield('d');

					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
				}

				if (value.getSubfield('e') != null) {
					sf = value.getSubfield('e');

					testo += (testo.trim().equals("") ? "" : " + ") + sf.getData().trim();
				}

				if (testo != null && !testo.trim().equals("")) {
					testo = testo.replace("\\", "[");
					testo = testo.replace("!", "]");
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(testo.trim()));
				}
			}
		}

		return result;
	}

	private String getFormat124(Record record, String testoA) {
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;
		String[] st = null;

		values = getDF(record.getDataFields(), "124");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('b');
				if (sf != null) {
					switch (sf.getData()) {
					case "a":
						testo = "atlante";
						break;
					case "b":
						testo = "diagramma";
						break;
					case "c":
						testo = "globo";
						break;
					case "d":
						testo = "carta geografica";
						break;
					case "e":
						testo = "modello";
						break;
					case "f":
						testo = "profilo";
						break;
					case "g":
						testo = "immagine remota";
						break;
					case "h":
						testo = "sezione";
						break;
					case "i":
						testo = "veduta";
						break;
					case "j":
						testo = "pianta";
						break;
					case "z":
						testo = "altro";
						break;

					default:
						break;
					}

					st = testoA.split(" ");

					if (testo != null && testo.indexOf(st[st.length - 1]) == -1) {
						testo = "[" + testo + "]";
					}
				}
			}
		}

		return testo;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getPublisher(Record record) {
		Vector<SimpleLiteral> result = null;

		result = getPublisher210_620_712(record);
		return result;
	}

}
