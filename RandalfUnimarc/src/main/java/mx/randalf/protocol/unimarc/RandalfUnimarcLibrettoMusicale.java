/**
 * 
 */
package mx.randalf.protocol.unimarc;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.purl.dc.elements._1.SimpleLiteral;

import it.sbn.iccu.metaag1.Bib.LocalBib;
import mx.randalf.mag.MagXsd;

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarcLibrettoMusicale extends RandalfUnimarcMaterialeCartaceo {

	/**
	 * @param prermissiveReader
	 * @param toUtf8
	 */
	public RandalfUnimarcLibrettoMusicale(boolean prermissiveReader, boolean toUtf8) {
		super(prermissiveReader, toUtf8);
	}

	@Override
	protected Collection<? extends SimpleLiteral> genFormat(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		values = getDF(record.getDataFields(), "215");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				if (value.getSubfield('a') != null) {
					sf = value.getSubfield('a');

					testo = sf.getData().trim();
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

	@Override
	protected List<LocalBib> genLocalBibs(Record record, MagXsd magXsd) {
		return null;
	}

	@Override
	protected abstract String getTipoMateriale(char typeOfRecord);

	@Override
	protected abstract String getUnimarcTable(String key, String value);

	@Override
	protected Collection<? extends SimpleLiteral> gen517(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		boolean elabora = false;
		String testo = null;
		String valueE = null;

		values = getDF(record.getDataFields(), "517");
		if (values != null) {
			for (DataField value : values) {
				elabora = false;
				testo = "";
				for (Subfield sf : value.getSubfields()) {
					if (sf.getCode() == '1') {
						elabora = false;
						if (sf.getData().startsWith("200")) {
							elabora = true;
							testo = "";
						}
					} else if (elabora) {
						if (sf.getCode() == 'a') {
							testo = sf.getData();
						} else if (sf.getCode() == 'e') {
							valueE = sf.getData();
						}
					}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(
							(valueE.equals("E") ? "titolo alternativo:" : "'variante del titolo:'") + testo.trim()));
				}
			}
		}
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> gen607(Record record, MagXsd magXsd) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Collection<? extends SimpleLiteral> genSubject(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		result = (Vector<SimpleLiteral>) super.genSubject(record, magXsd);

		values = getDF(record.getDataFields(), "702");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('4');
				if (sf.getData().equals("230")) {
					sf = value.getSubfield('a');
					if (sf != null) {
						testo = sf.getData();
					}
					sf = value.getSubfield('b');
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
		}

		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> genCoverage(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		values = getDF(record.getDataFields(), "922");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
					sf = value.getSubfield('s');
					if (sf != null) {
						testo = sf.getData();
					}

					sf = value.getSubfield('r');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : ", ") + sf.getData();
					}

					sf = value.getSubfield('q');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : ", ") + sf.getData();
					}

					sf = value.getSubfield('p');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : " ") + sf.getData();
					}

					sf = value.getSubfield('u');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : ", ") + sf.getData();
					}

					if (testo != null && !testo.trim().equals("")) {
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						result.add(magXsd.genSimpleLiteral(testo.trim()));
					}
			}
		}

		return result;
	}


	@Override
	protected String gen923_922(Record record, MagXsd magXsd) {
		String result = null;
		List<DataField> values = null;
		Subfield sf = null;

		result = "";

		values = getDF(record.getDataFields(), "923");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('b');
				if (sf != null) {
					switch (sf.getData()) {
					case "A":
						result = "autografo";
						break;
					case "B":
						result = "autografo incerto";
						break;
					case "C":
						result = "autografo in parte";
						break;
					case "D":
						result = "copia";
						break;
					case "E":
						result = "copia di vari copisti";
						break;

					default:
						break;
					}
				}

				sf = value.getSubfield('e');
					if (sf != null) {
						result += (result.trim().equals("")?"":" ; ")+sf.getData();
					}

					sf = value.getSubfield('h');
						if (sf != null) {
							result += (result.trim().equals("")?"":" ; ")+sf.getData();
						}
			}
		}


		values = getDF(record.getDataFields(), "922");
		if (values != null) {
			for (DataField value : values) {
					sf = value.getSubfield('t');
					if (sf != null) {
						result += (result.trim().equals("")?"":" ; ")+sf.getData();
					}
			}
		}
		return result;
	}


	@SuppressWarnings("unchecked")
	@Override
	protected Collection<? extends SimpleLiteral> genDescription(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		result = (Vector<SimpleLiteral>) super.genDescription(record, magXsd);

		values = getDF(record.getDataFields(), "927");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
					sf = value.getSubfield('a');
					if (sf != null) {
						testo = sf.getData();
					}

					sf = value.getSubfield('c');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : ":") + sf.getData();
					}

					sf = value.getSubfield('b');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : ", ") + sf.getData();
					}

					if (testo != null && !testo.trim().equals("")) {
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						result.add(magXsd.genSimpleLiteral(testo.trim()));
					}
			}
		}

		return result;
	}


	@Override
	protected String gen928_929(Record record, MagXsd magXsd) {
		String result = null;
		List<DataField> values = null;
		Subfield sf = null;

		result = "";

		values = getDF(record.getDataFields(), "928");
		if (values != null) {
			for (DataField value : values) {
					sf = value.getSubfield('a');
					if (sf != null) {
						result = sf.getData();
					}
			}
		}

		values = getDF(record.getDataFields(), "929");
		if (values != null) {
			for (DataField value : values) {
					sf = value.getSubfield('f');
					if (sf != null) {
						result += (result.trim().equals("")?"":". ")+sf.getData();
					}

					sf = value.getSubfield('c');
					if (sf != null) {
						result += (result.trim().equals("")?"":". ")+sf.getData();
					}

					sf = value.getSubfield('i');
					if (sf != null) {
						result += (result.trim().equals("")?"":". ")+sf.getData();
					}

					sf = value.getSubfield('d');
					if (sf != null) {
						result += (result.trim().equals("")?"":". ")+sf.getData();
					}
			}
		}

		return result;
	}

}
