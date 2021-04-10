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

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarcLibrettoMusicale extends RandalfUnimarcLibroModerno {

	/**
	 * @param prermissiveReader
	 * @param toUtf8
	 */
	public RandalfUnimarcLibrettoMusicale(Vector<String> segnatura, String nomeIstituto, String codiceIsil) {
		super(segnatura, nomeIstituto, codiceIsil);
	}

	@Override
	protected Collection<? extends SimpleLiteral> getDescription(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = "";

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

		values = getDF(record.getDataFields(), "300");
		if (values != null) {
			testo = "";
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					if (!(sf.getData().trim().startsWith("Segn.:") || sf.getData().trim().startsWith("Marca")
							|| sf.getData().trim().startsWith("Colophon"))) {
						testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
					}
				}
			}

			values = getDF(record.getDataFields(), "923");
			if (values != null) {
				for (DataField value : values) {
					sf = value.getSubfield('b');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : " ; ");
						switch (sf.getData()) {
						case "A":
							testo += "autografo";
							break;
						case "B":
							testo += "autografo incerto";
							break;
						case "C":
							testo += "autografo in parte";
							break;
						case "D":
							testo += "copia";
							break;
						case "E":
							testo += "copia di vari copisti";
							break;

						default:
							break;
						}
					}
					sf = value.getSubfield('e');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
					}
					sf = value.getSubfield('h');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
					}
				}
			}

			values = getDF(record.getDataFields(), "922");
			if (values != null) {
				for (DataField value : values) {
					sf = value.getSubfield('t');
					if (sf != null) {
						testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
					}
				}
			}

			if (testo != null && !testo.trim().equals("")) {
				if (result == null) {
					result = new Vector<SimpleLiteral>();
				}
				result.add(magXsd.genSimpleLiteral(testo.trim()));
			}
		}

		result2 = getDescription927(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		return result;
	}

	protected Vector<SimpleLiteral> getDescription927(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		values = getDF(record.getDataFields(), "927");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo += sf.getData().trim();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : ":") + sf.getData().trim();
				}
				sf = value.getSubfield('b');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : ", ") + getUnimarcTable("ORGA", sf.getData().trim());
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
	protected Collection<? extends SimpleLiteral> getRelation(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getRelation410(record);

		result2 = getRelation423(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation461(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation462(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation463(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation464(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation488(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation500_928_929(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation510(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation517T(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation517T(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		String testo2 = null;

		values = getDF(record.getDataFields(), "517");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				testo2 = "";
				for (Subfield sf : value.getSubfields()) {
					if (sf.getCode() == 'a') {
						testo = sf.getData();
					} else if (sf.getCode() == 'e') {
						testo2 = sf.getData();
					}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					if (testo2.equals("T")) {
						result.add(magXsd.genSimpleLiteral("'titolo alternativo:'" + testo.trim()));
					} else {
						result.add(magXsd.genSimpleLiteral("'variante del titolo:'" + testo.trim()));
					}
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation500_928_929(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		String testo2 = null;

		values = getDF(record.getDataFields(), "500");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				for (Subfield sf : value.getSubfields()) {
					if (sf.getCode() == 'a') {
						testo = sf.getData();
						testo2 = getRelation928_929(record);
						if (testo2 != null) {
							testo += (testo.equals("") ? "" : ". ") + testo2;
						}
					}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'titolo uniforme:'" + testo.trim()));
				}
			}
		}
		return result;
	}

	protected String getRelation928_929(Record record) {
		String result = "";
		List<DataField> values = null;
		Subfield sf = null;

		values = getDF(record.getDataFields(), "928");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + getUnimarcTable("FOMU", sf.getData());
				}
			}
		}

		values = getDF(record.getDataFields(), "929");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('f');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + sf.getData();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + sf.getData();
				}
				sf = value.getSubfield('i');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + sf.getData();
				}
				sf = value.getSubfield('d');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + sf.getData();
				}
			}
		}

		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getCoverage(Record record) {
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
	protected Collection<? extends SimpleLiteral> getPublisher(Record record) {
		Vector<SimpleLiteral> result = null;

		result = getPublisher210_620_712(record);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Collection<? extends SimpleLiteral> getSubject(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		result = (Vector<SimpleLiteral>) super.getSubject(record);

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

}
