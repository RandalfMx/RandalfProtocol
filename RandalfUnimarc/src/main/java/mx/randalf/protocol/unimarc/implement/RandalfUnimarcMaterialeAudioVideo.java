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

import it.sbn.iccu.metaag1.Bib.LocalBib;

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarcMaterialeAudioVideo extends RandalfUnimarcMaterialeCartografico {

	/**
	 * @param segnatura
	 */
	public RandalfUnimarcMaterialeAudioVideo(Vector<String> segnatura, String nomeIstituto, String codiceIsil) {
		super(segnatura, nomeIstituto, codiceIsil);
	}

	@Override
	protected Vector<SimpleLiteral> getRelation517(Record record) {
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

	@Override
	protected Vector<SimpleLiteral> getRelation423(Record record) {
		return null;
	}

	@Override
	protected Vector<SimpleLiteral> getRelation488(Record record) {
		return null;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getDescription(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;
		List<DataField> values = null;
		Subfield sf = null;

		result = getDescription950_316(record);

		values = getDF(record.getDataFields(), "327");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(sf.getData().trim()));
				}
			}
		}

		values = getDF(record.getDataFields(), "323");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(sf.getData().trim()));
				}
			}
		}

		result2 = getDescription927(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getDescription300(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Collection<? extends SimpleLiteral> getIdentifier(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = "";

		result = (Vector<SimpleLiteral>) super.getIdentifier(record);

		values = getDF(record.getDataFields(), "071");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData().trim();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					if (testo.indexOf("EAN/UPC") > -1) {
						testo = testo.replace("EAN/UPC", "EAN");
						testo += " " + sf.getData().trim();
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						result.add(magXsd.genSimpleLiteral(sf.getData().trim()));
					}
				}
			}
		}

		return result;
	}

	@Override
	protected Collection<? extends LocalBib> getLocalBib(Record record) {
		return null;
	}

	@Override
	protected String getDate210(Record record) {
		return null;
	}

	@Override
	protected Vector<SimpleLiteral> getSubject(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getSubject606(record);

		result2 = getSubject676(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getPublisher(Record record) {
		Vector<SimpleLiteral> result = null;

		result = getPublisher210(record);
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getFormat(Record record) {
		Vector<SimpleLiteral> result = null;

		result = getFormat215(record);

		return result;
	}

	@Override
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
				sf = value.getSubfield('c');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + sf.getData();
				}
			}
		}

		values = getDF(record.getDataFields(), "929");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('b');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + sf.getData();
				}
				sf = value.getSubfield('a');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + getUnimarcTable("TONO", sf.getData());
				}
				sf = value.getSubfield('f');
				if (sf != null) {
					result += (result.trim().equals("") ? "" : ". ") + sf.getData();
				}
				sf = value.getSubfield('e');
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
	protected Collection<? extends SimpleLiteral> getRelation(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getRelation410(record);

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

		result2 = getRelation500_928_929(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result2 = getRelation510(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		result2 = getRelation517T(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		return result;
	}

	@Override
	protected Vector<SimpleLiteral> getRelation464(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		boolean elabora = false;

		values = getDF(record.getDataFields(), "464");
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
							testo += (testo.trim().equals("") ? "" : " : ") + sf.getData();
						} else if (sf.getCode() == 'f') {
							testo += (testo.trim().equals("") ? "" : " / ") + sf.getData();
						}
					}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'comprende:'" + testo.trim()));
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation463(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		boolean elabora = false;
		
		values = getDF(record.getDataFields(), "463");
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
							testo += (testo.trim().equals("") ? "" : " : ") + sf.getData();
						} else if (sf.getCode() == 'f') {
							testo += (testo.trim().equals("") ? "" : " / ") + sf.getData();
						}
					}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (record.getLeader().getImplDefined1()[0] == 'a') {
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						result.add(magXsd.genSimpleLiteral("'fa parte di:'" + testo.trim()));
					} else if (record.getLeader().getImplDefined1()[0] == 'm'
							&& (record.getLeader().getImplDefined1()[1] == '1' || record.getLeader().getImplDefined1()[1] == '1')) {
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						result.add(magXsd.genSimpleLiteral("'comprende:'" + testo.trim()));
					}
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation462(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		boolean elabora = false;

		values = getDF(record.getDataFields(), "462");
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
							testo += (testo.trim().equals("") ? "" : " : ") + sf.getData();
						} else if (sf.getCode() == 'f') {
							testo += (testo.trim().equals("") ? "" : " / ") + sf.getData();
						}
					}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'fa parte di:'" + testo.trim()));
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation461(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		boolean elabora = false;

		values = getDF(record.getDataFields(), "461");
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
							testo += (testo.trim().equals("") ? "" : " : ") + sf.getData();
						} else if (sf.getCode() == 'f') {
							testo += (testo.trim().equals("") ? "" : " / ") + sf.getData();
						}
					}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'fa parte di:'" + testo.trim()));
				}
			}
		}
		return result;
	}

}
