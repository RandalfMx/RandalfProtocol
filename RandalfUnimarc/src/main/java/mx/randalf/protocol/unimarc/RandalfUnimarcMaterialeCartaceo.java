/**
 * 
 */
package mx.randalf.protocol.unimarc;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.purl.dc.elements._1.SimpleLiteral;

import it.sbn.iccu.metaag1.Bib;
import it.sbn.iccu.metaag1.Bib.Holdings;
import it.sbn.iccu.metaag1.Bib.LocalBib;
import it.sbn.iccu.metaag1.BibliographicLevel;
import mx.randalf.mag.MagXsd;

/**
 * @author massi
 *
 */
public abstract class RandalfUnimarcMaterialeCartaceo extends RandalfUnimarc<Bib> {

	/**
	 * @param prermissiveReader
	 * @param toUtf8
	 */
	public RandalfUnimarcMaterialeCartaceo(boolean prermissiveReader, boolean toUtf8) {
		super(prermissiveReader, toUtf8);
	}

	@Override
	protected Bib init(Record record, String key) {
		Bib bib = null;
		MagXsd magXsd = null;
		Collection<? extends SimpleLiteral> values = null;
		List<LocalBib> localBibs = null;
		List<Holdings> holdings = null;

		bib = new Bib();
		magXsd = new MagXsd(null);

		bib.setLevel(BibliographicLevel.fromValue(record.getLeader().getImplDefined1()[0] + ""));

		bib.getType().add(magXsd.genSimpleLiteral(getTipoMateriale(record.getLeader().getTypeOfRecord())));

		values = genIdentifier(record, magXsd);
		if (values != null)
			bib.getIdentifier().addAll(values);

		values = genDate(record, magXsd);
		if (values != null)
			bib.getDate().addAll(values);

		values = genLanguage(record, magXsd);
		if (values != null)
			bib.getLanguage().addAll(values);

		values = genFormat(record, magXsd);
		if (values != null)
			bib.getFormat().addAll(values);

		values = genTitle(record, magXsd);
		if (values != null)
			bib.getTitle().addAll(values);

		localBibs = genLocalBibs(record, magXsd);
		if (localBibs != null)
			bib.getLocalBib().addAll(localBibs);

		values = genPublisher(record, magXsd);
		if (values != null)
			bib.getPublisher().addAll(values);

		values = genDescription(record, magXsd);
		if (values != null)
			bib.getDescription().addAll(values);

		values = genRelation(record, magXsd);
		if (values != null)
			bib.getRelation().addAll(values);

		values = genSubject(record, magXsd);
		if (values != null)
			bib.getSubject().addAll(values);

		values = genCreator(record, magXsd);
		if (values != null)
			bib.getCreator().addAll(values);

		values = genContributor(record, magXsd);
		if (values != null)
			bib.getContributor().addAll(values);

		holdings = genHoldings(record, magXsd);
		if (holdings != null)
			bib.getHoldings().addAll(holdings);

		values = genCoverage(record, magXsd);
		if (values != null)
			bib.getCoverage().addAll(values);

		return bib;
	}

	protected Collection<? extends SimpleLiteral> genCoverage(Record record, MagXsd magXsd) {
		return null;
	}

	protected List<Holdings> genHoldings(Record record, MagXsd magXsd){
		List<Holdings> result = null;
		
		
		return result;
	}

	private Collection<? extends SimpleLiteral> genContributor(Record record, MagXsd magXsd) {
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
				sf = value.getSubfield('f');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData() + ">";
				}
				sf = value.getSubfield('4');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ") +"["+ getUnimarcTable("LETA",sf.getData()) + "]";
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
					testo += (testo.trim().equals("") ? "" : " : ") + sf.getData();
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
				sf = value.getSubfield('f');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData() + ">";
				}
				sf = value.getSubfield('4');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ") +"["+ getUnimarcTable("LETA",sf.getData()) + "]";
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

	private Collection<? extends SimpleLiteral> genCreator(Record record, MagXsd magXsd) {
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

	protected Collection<? extends SimpleLiteral> genSubject(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;
		Collection<? extends SimpleLiteral> sls = null;

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

		sls = gen607(record, magXsd);
		if (sls != null)
			result.addAll(sls);

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

	protected Collection<? extends SimpleLiteral> gen607(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;

		values = getDF(record.getDataFields(), "607");
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

	private Collection<? extends SimpleLiteral> genRelation(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Collection<? extends SimpleLiteral> sls = null;
		String testo = null;
		String testoCont = null;
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

		if (record.getLeader().getImplDefined1()[0] == 'a') {
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
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						result.add(magXsd.genSimpleLiteral("'fa parte di:'" + testo.trim()));
					}
				}
			}
		}

		if (record.getLeader().getImplDefined1()[0] == 'm'
				&& (record.getLeader().getImplDefined1()[1] == '1' || record.getLeader().getImplDefined1()[1] == '2')) {
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
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						result.add(magXsd.genSimpleLiteral("'comprende:'" + testo.trim()));
					}
				}
			}
		}

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
					result.add(magXsd.genSimpleLiteral("'comprende:'" + testo.trim()));
				}
			}
		}

		values = getDF(record.getDataFields(), "500");
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

				testoCont = gen928_929(record, magXsd);
				if (testoCont != null)
					testo += (testo.trim().equals("")?"":". ")+testoCont;
				if (testo != null && !testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral("'titolo uniforme:'" + testo.trim()));
				}
			}
		}

		values = getDF(record.getDataFields(), "510");
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
					result.add(magXsd.genSimpleLiteral("'titolo paralleto:'" + testo.trim()));
				}
			}
		}

		sls = gen517(record, magXsd);
		if (sls != null)
			result.addAll(sls);

		return result;
	}

	protected String gen928_929(Record record, MagXsd magXsd) {
		return null;
	}

	protected Collection<? extends SimpleLiteral> gen517(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		boolean elabora = false;
		String testo = null;

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
							testo += (testo.trim().equals("") ? "" : " : ") + sf.getData();
						}
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

	protected Collection<? extends SimpleLiteral> genDescription(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = "";
		String testo2 = "";

		values = getDF(record.getDataFields(), "303");
		if (values != null) {
			testo = "";
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

		values = getDF(record.getDataFields(), "300");
		if (values != null) {
			testo = "";
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					if (!(sf.getData().trim().startsWith("Segn.:") || sf.getData().trim().startsWith("Marca")
							|| sf.getData().trim().startsWith("Colophon"))) {
						testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
						
						testo2 = gen923_922(record,magXsd);
						if (testo2 != null)
							testo += (testo.trim().equals("") ? "" : " ; ") + testo2;
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

	protected String gen923_922(Record record, MagXsd magXsd) {
		return null;
	}

	private Collection<? extends SimpleLiteral> genPublisher(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = "";
		String testo2 = null;

		values = getDF(record.getDataFields(), "210");
		if (values != null) {
			for (DataField value : values) {
				testo = "";

				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData().trim();
				}

				sf = value.getSubfield('c');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " : ") + sf.getData().trim();
				}

				sf = value.getSubfield('d');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : ", ") + sf.getData().trim();
				}

				sf = value.getSubfield('e');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " ; ") + sf.getData().trim();
				}

				sf = value.getSubfield('g');
				if (sf != null) {
					testo += (testo.trim().equals("") ? "" : " : ") + sf.getData().trim();
				}

				testo2 = genPubblicazione(record);
				if (testo2 != null) {
					testo += (testo.trim().equals("") ? "" : " ") + "[" + testo2 + "]";
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

	private String genPubblicazione(Record record) {
		String result = null;
		List<DataField> values = null;
		Subfield sf = null;

		values = getDF(record.getDataFields(), "620");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('d');
				if (sf != null) {
					if (result == null) {
						result = sf.getData();
					} else {
						result += " " + sf.getData();
					}
				}
			}
		}

		values = getDF(record.getDataFields(), "712");
		if (values != null) {
			for (DataField value : values) {
				sf = value.getSubfield('4');
				if (sf != null && (sf.getData().equals("610") || sf.getData().equals("650"))) {
					sf = value.getSubfield('a');
					if (sf != null) {
						if (result == null) {
							result = sf.getData();
						} else {
							result += " ; " + sf.getData();
						}
					}
				}
			}
		}
		return result;
	}

	protected List<LocalBib> genLocalBibs(Record record, MagXsd magXsd) {
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

	private Collection<? extends SimpleLiteral> genTitle(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;
		String testo2 = null;

		values = getDF(record.getDataFields(), "200");
		if (values != null) {
			for (DataField value : values) {
				if (value.getIndicator1() == '0') {
					testo = "";
					sf = value.getSubfield('a');
					testo2 = sf.getData().trim().replace("<<", "").replace(">>", "");
					values = getDF(record.getDataFields(), "461");
					if (values != null) {
						for (DataField value2 : values) {
							sf = value2.getSubfield('a');
							testo += "[" + sf.getData();
							sf = value2.getSubfield('e');
							testo += " : " + sf.getData() + "] " + testo2;
						}
					} else {
						values = getDF(record.getDataFields(), "462");
						if (values != null) {
							for (DataField value2 : values) {
								sf = value2.getSubfield('a');
								testo += "[" + sf.getData();
								sf = value2.getSubfield('e');
								testo += " : " + sf.getData() + "] " + testo2;
							}
						} else {
							values = getDF(record.getDataFields(), "463");
							if (values != null) {
								for (DataField value2 : values) {
									sf = value2.getSubfield('a');
									testo += "[" + sf.getData();
									sf = value2.getSubfield('e');
									testo += " : " + sf.getData() + "] " + testo2;
								}
							}
						}
					}
				} else {
					testo = "";
					for (Subfield sValue : value.getSubfields()) {
						switch (sValue.getCode()) {
						case 'a':
							testo += (testo.trim().equals("") ? "" : " ; ")
									+ sValue.getData().trim().replace("<<", "").replace(">>", "");
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

	protected Collection<? extends SimpleLiteral> genFormat(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;
		String formato = null;
		String testo = null;
		String[] st = null;

		values = getDF(record.getDataFields(), "124");
		if (values != null) {
			for (DataField value : values) {
				if (value.getSubfield('b') != null) {
					sf = value.getSubfield('b');
					formato = getFormato(sf.getData());
				}
			}
		}

		values = getDF(record.getDataFields(), "215");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				if (value.getSubfield('a') != null) {
					sf = value.getSubfield('a');
					st = sf.getData().split(" ");

					if (formato.indexOf(st[st.length - 1]) == -1) {
						testo = sf.getData().trim() + " [" + formato + "]";
					} else {
						testo = sf.getData().trim();
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

	private String getFormato(String data) {
		String result = null;

		switch (data) {
		case "a":
			result = "atlante";
			break;
		case "b":
			result = "diagramma";
			break;
		case "c":
			result = "globo";
			break;
		case "d":
			result = "carta geografica";
			break;
		case "e":
			result = "modello";
			break;
		case "f":
			result = "profilo";
			break;
		case "g":
			result = "immagine remota";
			break;
		case "h":
			result = "sezione";
			break;
		case "i":
			result = "veduta";
			break;
		case "j":
			result = "pianta";
			break;
		case "z":
			result = "altro";
			break;

		default:
			break;
		}

		return result;
	}

	private Collection<? extends SimpleLiteral> genLanguage(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<DataField> values = null;
		Subfield sf = null;

		values = getDF(record.getDataFields(), "101");
		if (values != null) {
			for (DataField value : values) {
				if (value.getSubfield('a') != null) {
					sf = value.getSubfield('a');
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					result.add(magXsd.genSimpleLiteral(sf.getData().trim()));
				}
			}
		}

		return result;
	}

	private Collection<? extends SimpleLiteral> genDate(Record record, MagXsd magXsd) {
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

						values = getDF(record.getDataFields(), "210");
						if (values != null) {
							for (DataField value2 : values) {
								if (value2.getSubfield('d') != null) {
									sf = value2.getSubfield('d');
									if (result == null) {
										result = new Vector<SimpleLiteral>();
									}
									result.add(magXsd.genSimpleLiteral(sf.getData()));
								}
							}
						}

					} else {
						if (key.equals("e")) {
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
									if (!date1.trim().equals("")) {
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
		}
		return result;
	}

	private Collection<? extends SimpleLiteral> genIdentifier(Record record, MagXsd magXsd) {
		Vector<SimpleLiteral> result = null;
		List<ControlField> values = null;

		values = getCF(record.getControlFields(), "001");
		if (values != null) {
			for (ControlField value : values) {
				if (result == null) {
					result = new Vector<SimpleLiteral>();
				}
				result.add(magXsd.genSimpleLiteral(value.getData()));
			}
		}
		return result;
	}

	protected abstract String getTipoMateriale(char typeOfRecord);

	protected abstract String getUnimarcTable(String key, String value);

}
