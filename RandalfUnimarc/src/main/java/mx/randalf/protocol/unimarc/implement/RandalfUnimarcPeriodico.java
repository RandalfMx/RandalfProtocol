/**
 * 
 */
package mx.randalf.protocol.unimarc.implement;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.purl.dc.elements._1.SimpleLiteral;

import it.sbn.iccu.metaag1.Bib.Holdings;
import it.sbn.iccu.metaag1.Bib.Holdings.Shelfmark;
import it.sbn.iccu.metaag1.Bib.LocalBib;
import it.sbn.iccu.metaag1.Bib.Piece;
import mx.randalf.protocol.unimarc.RandalfUnimarcDublinCore;
import it.sbn.iccu.metaag1.BibliographicLevel;

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarcPeriodico extends RandalfUnimarcDublinCore {

	protected Vector<String> segnature = null;

	protected String nomeIstituto = null;

	protected String codiceIsil = null;

	/**
	 * 
	 */
	public RandalfUnimarcPeriodico(Vector<String> segnature, String nomeIstituto, String codiceIsil) {
		this.segnature = segnature;
		this.nomeIstituto = nomeIstituto;
		this.codiceIsil = codiceIsil;
	}

	@Override
	protected BibliographicLevel getLevel(Record record) {
		return BibliographicLevel.fromValue(String.valueOf(record.getLeader().getImplDefined1()[0]));
	}

	@Override
	protected Collection<? extends SimpleLiteral> getIdentifier(Record record) {
		Vector<SimpleLiteral> result = null;
		List<ControlField> controlFields = null;
		String bid = "";

		controlFields = getCF(record.getControlFields(), "001");
		for (ControlField controlField : controlFields) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			bid = controlField.getData();
			if (bid.startsWith("IT\\ICCU\\")) {
				bid = bid.substring(8).replace("\\", "");
			}
			result.add(magXsd.genSimpleLiteral(bid));
		}
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getTitle(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;

		values = getDF(record.getDataFields(), "200");
		if (values != null) {
			for (DataField value : values) {

				testo = "";
				for (Subfield sValue : value.getSubfields()) {
					switch (sValue.getCode()) {
					case 'a':
						testo += (testo.trim().equals("") ? "" : " ; ") + sValue.getData().trim().replaceAll("<<", "")
								.replaceAll(">>", "").replaceAll("#", "").replaceAll("\\*", "").replaceAll("\u0098", "")
								.replaceAll("\u009C", "").replaceAll("\u0088", "").replaceAll("\u0089", "");
						break;

					case 'c':
						testo += (testo.trim().equals("") ? "" : ". ") + sValue.getData().trim();
						break;

					case 'd':
						testo += (testo.trim().equals("") ? "" : " = ") + sValue.getData().trim();
						break;

					case 'e':
						testo += (testo.trim().equals("") ? "" : " : ") + sValue.getData().trim();
						break;

					case 'f':
						testo += (testo.trim().equals("") ? "" : " / ") + sValue.getData().trim();
						break;

					case 'g':
						testo += (testo.trim().equals("") ? "" : " ; ") + sValue.getData().trim();
						break;

					default:
						break;
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
	protected Collection<? extends SimpleLiteral> getCreator(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		values = getDF(record.getDataFields(), "700");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData().trim();
				}
				sf = value.getSubfield('b');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : (sf.getData().trim().startsWith(",") ? "" : ", ")) + sf.getData();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					if (sf.getData().indexOf("omonimi non identificati") == -1
							&& sf.getData().indexOf("autore indifferenziato") == -1) {
						testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(" <") ? "" : " <")) + sf.getData();
					}
				}
				sf = value.getSubfield('d');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData();
				}
				sf = value.getSubfield('f');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(" ; ") ? " " : " ; ")) + sf.getData().trim()
							+ (sf.getData().trim().endsWith(">") ? "" : ">");
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(testo.trim()));
				}
			}
		}

		values = getDF(record.getDataFields(), "701");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData();
				}
				sf = value.getSubfield('b');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(", ") ? "" : ", ")) + sf.getData();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					if (sf.getData().indexOf("omonimi non identificati") == -1
							&& sf.getData().indexOf("autore indifferenziato") == -1) {
						testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(" <") ? "" : " <")) + sf.getData();
					}
				}
				sf = value.getSubfield('d');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData();
				}
				sf = value.getSubfield('f');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(" ; ") ? "" : " ; ")) + sf.getData()
							+ (sf.getData().endsWith(">") ? "" : ">");
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(testo.trim()));
				}
			}
		}

		values = getDF(record.getDataFields(), "710");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData();
				}
				sf = value.getSubfield('b');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : ", ") + sf.getData();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					if (sf.getData().indexOf("omonimi non identificati") == -1
							&& sf.getData().indexOf("autore indifferenziato") == -1) {
						testo += (testo.trim().equals("") ? "" : " <") + sf.getData();
					}
				}
				sf = value.getSubfield('d');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData();
				}
				sf = value.getSubfield('e');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData();
				}
				sf = value.getSubfield('f');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData() + ">";
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(testo.trim()));
				}
			}
		}

		values = getDF(record.getDataFields(), "711");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData();
				}
				sf = value.getSubfield('b');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : ", ") + sf.getData();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					if (sf.getData().indexOf("omonimi non identificati") == -1
							&& sf.getData().indexOf("autore indifferenziato") == -1) {
						testo += (testo.trim().equals("") ? "" : " <") + sf.getData();
					}
				}
				sf = value.getSubfield('d');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData();
				}
				sf = value.getSubfield('e');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData();
				}
				sf = value.getSubfield('f');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData() + ">";
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

		result = getPublisher210(record);
		return result;
	}

	protected Vector<SimpleLiteral> getPublisher210(Record record) {
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

	protected Collection<? extends SimpleLiteral> getSubject(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getSubject606(record);

		result2 = getSubject676(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		return result;
	}

	protected Vector<SimpleLiteral> getSubject676(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		values = getDF(record.getDataFields(), "676");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ") + sf.getData();
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

	protected Vector<SimpleLiteral> getSubject606(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

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
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getDescription(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		values = getDF(record.getDataFields(), "326");
		if (values != null) {
			testo = "";
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
				}
			}

			if (testo != null && !testo.trim().equals("")) {
				if (result == null) {
					result = new Vector<SimpleLiteral>();
				}
				result.add(magXsd.genSimpleLiteral(testo.trim()));
			}
		}

		result2 = getDescription300(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		values = getDF(record.getDataFields(), "207");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("[numerazione] " + sf.getData().trim()));
				}
			}
		}

		result2 = getDescription950(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		return result;
	}

	protected Vector<SimpleLiteral> getDescription300(Record record) {
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
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
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

	private Vector<SimpleLiteral> getDescription950(Record record) {
		RandalfUnimarc950 randalfUnimarc950 = null;
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo1 = null;
		String testo2 = null;
		Vector<Subfield> subfields = null;
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
								case 'b':
									testo1 = subfield.getData().trim();
									break;
								case 'c':
									testo1 = subfield.getData().trim();
									break;

								default:
									break;
								}
							}
							if (testo1 != null) {
								if (result == null) {
									result = new Vector<SimpleLiteral>();
								}
								result.add(magXsd.genSimpleLiteral("[consistenza] " + testo1.trim()));
							} else if (testo2 != null) {
								if (result == null) {
									result = new Vector<SimpleLiteral>();
								}
								result.add(magXsd.genSimpleLiteral("[consistenza] " + testo2.trim()));
							}
						}
					}
				}
			}
		}

		if (result == null) {
			values = getDF(record.getDataFields(), "899");
			if (values != null) {
				for (DataField value : values) {
					sf = value.getSubfield('1');
					if (sf != null && sf.getData().equals(codiceIsil)) {
						sf = value.getSubfield('4');
						if (sf != null && !sf.getData().trim().equals("") && !sf.getData().trim().equals(".")) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral("[consistenza] " + sf.getData().trim()));
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getContributor(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		values = getDF(record.getDataFields(), "702");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData();
				}
				sf = value.getSubfield('b');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : (sf.getData().trim().startsWith(", ") ? "" : ", "))
							+ sf.getData().trim();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					if (sf.getData().indexOf("omonimi non identificati") == -1
							&& sf.getData().indexOf("autore indifferenziato") == -1) {
						testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(" <") ? "" : " <")) + sf.getData();
					}
				}
				sf = value.getSubfield('d');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData();
				}
				sf = value.getSubfield('f');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim()
							+ (sf.getData().trim().endsWith(">") ? "" : ">");
				}
				sf = value.getSubfield('4');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ") + "[" + getUnimarcTable("LETA", sf.getData()) + "]";
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(testo.trim()));
				}
			}
		}

		values = getDF(record.getDataFields(), "712");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData();
				}
				sf = value.getSubfield('b');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(" : ") ? "" : " : ")) + sf.getData();
				}
				sf = value.getSubfield('c');
				if (sf != null) {
					if (sf.getData().indexOf("omonimi non identificati") == -1
							&& sf.getData().indexOf("autore indifferenziato") == -1) {
						testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(" <") ? "" : " <")) + sf.getData();
					}
				}
				sf = value.getSubfield('d');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
				}
				sf = value.getSubfield('f');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : (sf.getData().startsWith(" ; ") ? " " : " ; "))
							+ sf.getData().trim();
				}
				sf = value.getSubfield('e');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim() + ">";
				}
				sf = value.getSubfield('4');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ") + "[" + getUnimarcTable("LETA", sf.getData()) + "]";
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
	protected Collection<? extends SimpleLiteral> getDate(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String key = "";
		String date1 = "";
		String date2 = "";

		values = getDF(record.getDataFields(), "100");
		if (values != null) {
			for (DataField value : values) {
				if (value.getSubfield('a') != null) {
					sf = value.getSubfield('a');
					key = sf.getData().substring(8, 9);
					date1 = sf.getData().substring(9, 13);
					date2 = sf.getData().substring(13, 17);

					if (date1.trim().equals("") && date2.trim().equals("")) {

						date1 = getDate210(record);
						if (date1 != null) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral(date1));
						}
					} else {
						if (key.equals("a")) {
							if (!date1.trim().equals("")) {
								if (result == null) {
									result = new Vector<SimpleLiteral>();
								}
								result.add(magXsd.genSimpleLiteral(date1 + "-"));
							}
						} else if (key.equals("e")) {
							if (!date1.trim().equals("")) {
								if (result == null) {
									result = new Vector<SimpleLiteral>();
								}
								result.add(magXsd.genSimpleLiteral(date1));
							}
						} else {
							if (date2.trim().equals("")) {
								if (!date1.trim().equals("")) {
									if (result == null) {
										result = new Vector<SimpleLiteral>();
									}
									result.add(magXsd.genSimpleLiteral(date1));
								}
							} else if (!date1.trim().equals("")) {
								if (date1.trim().equals(date2.trim())) {
									if (result == null) {
										result = new Vector<SimpleLiteral>();
									}
									result.add(magXsd.genSimpleLiteral(date1));
								} else {
									if (result == null) {
										result = new Vector<SimpleLiteral>();
									}
									result.add(magXsd.genSimpleLiteral(date1));
									result.add(magXsd.genSimpleLiteral(date2));
								}
							}
						}
					}
				}
			}
		}

		return result;
	}

	protected String getDate210(Record record) {
		List<DataField> values = null;
		Subfield sf = null;
		String result = null;

		values = getDF(record.getDataFields(), "210");
		if (values != null) {
			for (DataField value2 : values) {
				if (value2.getSubfield('d') != null) {
					sf = value2.getSubfield('d');
					result = sf.getData();
				}
			}
		}
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getType(Record record) {
		Vector<SimpleLiteral> result = null;

		result = new Vector<SimpleLiteral>();
		result.add(magXsd.genSimpleLiteral(getTipoMateriale(record.getLeader().getTypeOfRecord())));
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getFormat(Record record) {
		Vector<SimpleLiteral> result = null;

		result = getFormat215(record);

		return result;
	}

	protected Vector<SimpleLiteral> getFormat215(Record record) {
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
	protected Collection<? extends SimpleLiteral> getSource(Record record) {
		return null;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getLanguage(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;

		values = getDF(record.getDataFields(), "101");
		if (values != null) {
			for (DataField value : values) {
				if (value.getSubfield('a') != null) {
					sf = value.getSubfield('a');
					if (!sf.getData().trim().equalsIgnoreCase("abs")) {
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
	protected Collection<? extends SimpleLiteral> getRelation(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;

		result = getRelation410(record);

		result2 = getRelation423(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		result = getRelation(result, getDF(record.getDataFields(), "430"));
		result = getRelation(result, getDF(record.getDataFields(), "431"));
		result = getRelation(result, getDF(record.getDataFields(), "432"));
		result = getRelation(result, getDF(record.getDataFields(), "433"));
		result = getRelation(result, getDF(record.getDataFields(), "434"));
		result = getRelation(result, getDF(record.getDataFields(), "435"));
		result = getRelation(result, getDF(record.getDataFields(), "436"));
		result = getRelation(result, getDF(record.getDataFields(), "437"));
		result = getRelation(result, getDF(record.getDataFields(), "440"));
		result = getRelation(result, getDF(record.getDataFields(), "441"));
		result = getRelation(result, getDF(record.getDataFields(), "442"));
		result = getRelation(result, getDF(record.getDataFields(), "443"));
		result = getRelation(result, getDF(record.getDataFields(), "444"));
		result = getRelation(result, getDF(record.getDataFields(), "445"));
		result = getRelation(result, getDF(record.getDataFields(), "446"));
		result = getRelation(result, getDF(record.getDataFields(), "447"));
		result = getRelation(result, getDF(record.getDataFields(), "448"));

		result2 = getRelation510(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation510(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;

		values = getDF(record.getDataFields(), "510");
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
					result.add(magXsd.genSimpleLiteral("'titolo paralleto:'" + testo.trim()));
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation423(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		boolean elabora = false;

		values = getDF(record.getDataFields(), "423");
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
					result.add(magXsd.genSimpleLiteral("'pubblicato con:'" + testo.trim()));
				}
			}
		}
		return result;
	}

	protected Vector<SimpleLiteral> getRelation410(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		String testo = null;
		boolean elabora = false;

		values = getDF(record.getDataFields(), "410");
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
						} else if (sf.getCode() == 'v') {
							testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData();
						}
					}
				}

				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'collana:'" + testo.trim()));
				}
			}
		}
		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getCoverage(Record record) {
		return null;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getRights(Record record) {
		return null;
	}

	@Override
	protected Collection<? extends Holdings> getHoldings(Record record) {
		RandalfUnimarc950 randalfUnimarc950 = null;
		Vector<Subfield> subfields = null;
		String segnatura = null;
		Holdings holdings = null;
		List<Holdings> result = null;
		Shelfmark shelfmark = null;
		List<DataField> values = null;
		String testo = null;
		String t1 = null;
		String t2 = null;

		values = getDF(record.getDataFields(), "950");

		if (values != null) {
			for (DataField value : values) {
				randalfUnimarc950 = new RandalfUnimarc950(value);
				if (randalfUnimarc950.getNomeIstituto().trim().equalsIgnoreCase(nomeIstituto.trim())) {

					for (String segna : segnature) {
						subfields = randalfUnimarc950.getValues(segna);
						if (subfields != null) {
							for (Subfield subfield : subfields) {
								switch (subfield.getCode()) {
								case 'd':
									if (segnatura != null) {
										holdings = new Holdings();
										holdings.setLibrary(randalfUnimarc950.getNomeIstituto());
										shelfmark = new Shelfmark();
										shelfmark.setContent(segnatura);
										holdings.getShelfmark().add(shelfmark);
										if (result == null) {
											result = new Vector<Holdings>();
										}
										result.add(holdings);
									}
									testo = subfield.getData();
									segnatura = "";
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

									break;
								case 'e':
									holdings = new Holdings();
									holdings.setLibrary(randalfUnimarc950.getNomeIstituto());
									testo = subfield.getData();
									if (testo.length() > 24 && testo.length() > 43) {
										segnatura += " " + testo.substring(24, 44);
									} else if (testo.length() > 24) {
										segnatura += " " + testo.substring(24);
									}

									t1 = testo.substring(3, 6);
									t2 = testo.substring(6, 15);
									if (t1.trim().equals("")) {
										testo = testo.substring(1, 3) + t2;
									} else {
										testo = t1 + "_" + t2;
									}
									holdings = new Holdings();
									holdings.setLibrary(randalfUnimarc950.getNomeIstituto());
									holdings.setInventoryNumber(testo);
									shelfmark = new Shelfmark();
									shelfmark.setContent(segnatura.trim());
									holdings.getShelfmark().add(shelfmark);
									if (result == null) {
										result = new Vector<Holdings>();
									}
									result.add(holdings);
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
		return result;
	}

	@Override
	protected Collection<? extends LocalBib> getLocalBib(Record record) {
		return null;
	}

	@Override
	protected Piece getPiece(Record record) {
		return null;
	}

	private Vector<SimpleLiteral> getRelation(Vector<SimpleLiteral> result, List<DataField> values) {
		String testo = null;
		boolean elabora = false;

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

	protected String checkPublisherD(String testo) {
		String result = null;

		if (testo.length() == 4) {
			try {
				Integer.parseInt(testo);
			} catch (NumberFormatException e) {
				result = testo;
			}
		} else if (testo.length() == 5 && testo.endsWith("-")) {
			try {
				Integer.parseInt(testo.replace("-", ""));
			} catch (NumberFormatException e) {
				result = testo;
			}
		} else if (testo.length() == 9 && testo.substring(5, 6).equals("-")) {
			try {
				Integer.parseInt(testo.replace("-", ""));
			} catch (NumberFormatException e) {
				result = testo;
			}
		} else {
			result = testo;
		}
		return result;
	}

	protected abstract String getTipoMateriale(char chiave);

	protected abstract String getUnimarcTable(String id, String codice);

}
