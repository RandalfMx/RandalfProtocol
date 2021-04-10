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
public abstract class RandalfUnimarcLibroAntico extends RandalfUnimarcPeriodico {

	/**
	 * 
	 */
	public RandalfUnimarcLibroAntico(Vector<String> segnature, String nomeIstituto, String codiceIsil) {
		super(segnature, nomeIstituto, codiceIsil);
		
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

	protected Vector<SimpleLiteral> getDescription300_NoSegna(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

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

			if (testo != null && !testo.trim().equals("")) {
				if (result == null) {
					result = new Vector<SimpleLiteral>();
				}
				result.add(magXsd.genSimpleLiteral(testo.trim()));
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getDescription921(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;

		values = getDF(record.getDataFields(), "921");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('b');
				if (sf != null) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'marca:' " + sf.getData().trim()));
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getDescription303(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;

		values = getDF(record.getDataFields(), "303");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'dedica:' " + sf.getData().trim()));
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getDescription950_316(Record record) {
		RandalfUnimarc950 randalfUnimarc950 = null;
		Vector<Subfield> subfields = null;
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		boolean trovato = false;
		Subfield sf = null;
		
		values = getDF(record.getDataFields(), "950");
		if (values != null) {
			for (DataField value : values) {
				randalfUnimarc950 = new RandalfUnimarc950(value);
				if (randalfUnimarc950.getNomeIstituto().trim().equalsIgnoreCase(nomeIstituto.trim())) {
					for (String segnatura : segnature) {
						subfields = randalfUnimarc950.getValues(segnatura);
						if (subfields != null) {
							for (Subfield subfield : subfields) {
								switch (subfield.getCode()) {
								case 'e':
									if (subfield.getData().trim().length() > 44) {

										if (result == null) {
											result = new Vector<SimpleLiteral>();
										}
										trovato = true;
										result.add(magXsd.genSimpleLiteral(subfield.getData().trim().substring(43)));
									}
									break;

								default:
									break;
								}
							}
						}
					}
				}
			}
		}

		if (!trovato) {
			values = getDF(record.getDataFields(), "316");
			if (values != null) {
				for (DataField value : values) {
					sf = value.getSubfield('5');
					if (sf != null) {
						for (String segnatura: segnature) {
							if (sf.getData().trim().equalsIgnoreCase(segnatura.trim())) {
								sf = value.getSubfield('a');
								if (sf != null) {
									if (result == null) {
										result = new Vector<SimpleLiteral>();
									}
									result.add(magXsd.genSimpleLiteral(sf.getData().trim()));
								}
							}
						}
					}
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

	protected Vector<SimpleLiteral> getPublisher210_620_712(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = "";
		String testo2 = null;
		String subfieldA = "";
		String subfieldC = "";

		values = getDF(record.getDataFields(), "210");
		if (values != null) {
			for (DataField value : values) {
				testo = "";

				subfieldA = "";
				subfieldC = "";

				sf = value.getSubfield('a');
				if (sf != null) {
					subfieldA = sf.getData().trim();
					testo = sf.getData().trim();
				}

				sf = value.getSubfield('c');
				if (sf != null) {
					subfieldC = sf.getData().trim();
					testo += (testo.trim().equals("") ? "" : " : ") + sf.getData().trim();
				}

				sf = value.getSubfield('d');
				if (sf != null) {
					testo2 = checkPublisherD(sf.getData().trim());
					if (testo2 != null) {
						testo += (testo.trim().equals("") ? "" : ", ") + testo2.trim();
					}
				}

				sf = value.getSubfield('e');
				if (sf != null && !sf.getData().trim().equals(subfieldA)) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
				}

				sf = value.getSubfield('g');
				if (sf != null && !sf.getData().trim().equals(subfieldC)) {
					testo += (testo.trim().equals("") ? "" : " : ") + sf.getData().trim();
				}

				values = getDF(record.getDataFields(), "620");
				if (values != null) {
					for (DataField value2 : values) {
						sf = value2.getSubfield('d');
						if (sf != null) {
							testo += (testo.trim().equals("")?"":" ")+ sf.getData();
						}
					}
				}

				values = getDF(record.getDataFields(), "712");
				if (values != null) {
					for (DataField value2 : values) {
						sf = value2.getSubfield('4');
						if (sf != null && (sf.getData().equals("610") || sf.getData().equals("650"))) {
							sf = value2.getSubfield('a');
							if (sf != null) {
								testo += (testo.trim().equals("")?"":" ; ")+ sf.getData();
							}
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

		result2 = getRelation500(record);
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

		result2 = getRelation517(record);
		if (result2!= null) {
			if (result ==null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation488(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		boolean elabora = false;

		values = getDF(record.getDataFields(), "488");
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
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation517(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;

		values = getDF(record.getDataFields(), "517");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				for (Subfield sf : value.getSubfields()) {
						if (sf.getCode() == 'a') {
							testo = sf.getData();
						}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'variante del titolo:'" + testo.trim()));
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation500(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;

		values = getDF(record.getDataFields(), "500");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				for (Subfield sf : value.getSubfields()) {
						if (sf.getCode() == 'a') {
							testo = sf.getData();
						} else if (sf.getCode() == 'e') {
							testo += (testo.trim().equals("") ? "" : " : ") + sf.getData();
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
