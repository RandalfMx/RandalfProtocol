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
public abstract class RandalfUnimarcMaterialeGrafico extends RandalfUnimarcLibrettoMusicale {

	/**
	 * @param segnatura
	 */
	public RandalfUnimarcMaterialeGrafico(Vector<String> segnatura, String nomeIstituto, String codiceIsil) {
		super(segnatura, nomeIstituto, codiceIsil);
	}

	@Override
	protected Collection<? extends SimpleLiteral> getSubject(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = null;
		String testo2 = "";

		values = getDF(record.getDataFields(), "116");
		if (values != null) {
			for (DataField value : values) {
				testo = "";
				sf = value.getSubfield('a');
				if (sf != null) {
					testo = sf.getData();

					testo2 = getSubjectCod1(testo.substring(0, 1));
					if (testo2 != null && !testo2.trim().equals("")) {
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						result.add(magXsd.genSimpleLiteral(testo2.trim()));
					}

					if (testo.length() > 4) {
						testo2 = getSubjectCod2(testo.substring(4, 6));
						if (testo2 != null && !testo2.trim().equals("")) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral(testo2.trim()));
						}
					}

					if (testo.length() > 6) {
						testo2 = getSubjectCod2(testo.substring(6, 8));
						if (testo2 != null && !testo2.trim().equals("")) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral(testo2.trim()));
						}
					}

					if (testo.length() > 8) {
						testo2 = getSubjectCod2(testo.substring(8, 10));
						if (testo2 != null && !testo2.trim().equals("")) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral(testo2.trim()));
						}
					}

					if (testo.length() > 10) {
						testo2 = getSubjectCod3(testo.substring(10, 12));
						if (testo2 != null && !testo2.trim().equals("")) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral(testo2.trim()));
						}
					}

					if (testo.length() > 12) {
						testo2 = getSubjectCod3(testo.substring(12, 14));
						if (testo2 != null && !testo2.trim().equals("")) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral(testo2.trim()));
						}
					}

					if (testo.length() > 14) {
						testo2 = getSubjectCod3(testo.substring(14, 16));
						if (testo2 != null && !testo2.trim().equals("")) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral(testo2.trim()));
						}
					}

					if (testo.length() > 16) {
						testo2 = getSubjectCod4(testo.substring(16, 18));
						if (testo2 != null && !testo2.trim().equals("")) {
							if (result == null) {
								result = new Vector<SimpleLiteral>();
							}
							result.add(magXsd.genSimpleLiteral(testo2.trim()));
						}
					}
				}
			}
		}

		result2 = getSubject606(record);
		if (result2 != null) {
			result.addAll(result2);
		}


		result2 = getSubject676(record);
		if (result2 != null) {
			result.addAll(result2);
		}

		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getDescription(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;
		List<DataField> values = null;
		Subfield sf = null;
		String testo = "";

		values = getDF(record.getDataFields(), "330");
		if (values != null) {
			testo = "";
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					testo += (testo.equals("") ? "" : " ; ");
				}
			}
		}

		values = getDF(record.getDataFields(), "327");
		if (values != null) {
			testo = "";
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					testo += (testo.equals("") ? "" : " ; ");
				}
			}
		}

		values = getDF(record.getDataFields(), "300");
		if (values != null) {
			testo = "";
			for (DataField value : values) {
				sf = value.getSubfield('a');
				if (sf != null) {
					testo += (testo.equals("") ? "" : " ; ");
				}
			}
		}

		if (testo != null && !testo.trim().equals("")) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.add(magXsd.genSimpleLiteral(testo));
		}

		result2 = getDescription950_316(record);
		if (result2 != null) {
			if (result == null) {
				result = new Vector<SimpleLiteral>();
			}
			result.addAll(result2);
		}

		return result;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getCoverage(Record record) {
		return null;
	}

	@Override
	protected Collection<? extends SimpleLiteral> getRelation(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> result2 = null;
		List<DataField> values = null;
		String testo = null;

		result = getRelation410(record);

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

		values = getDF(record.getDataFields(), "560");
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
					result.add(magXsd.genSimpleLiteral("'fa parte di:'" + testo.trim()));
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

	private String getSubjectCod1(String key) {
		switch (key) {
		case "a":
			return "collage";
		case "b":
			return "disegno";
		case "c":
			return "pittura";
		case "d":
			return "riproduzione fotomeccanica";
		case "e":
			return "foto negativo";
		case "f":
			return "riproduzione fotografica";
		case "h":
			return "immagine";
		case "i":
			return "stampa";
		case "k":
			return "disegno tecnico";
		case "m":
			return "master";

		default:
			return null;
		}
	}

	private String getSubjectCod2(String key) {
		switch (key) {
		case "aa":
			return "matita";
		case "ab":
			return "grafite";
		case "ac":
			return "matita colorata";
		case "ad":
			return "inchiostro di china";
		case "ae":
			return "acquerellato";
		case "af":
			return "carbone";
		case "ag":
			return "gesso";
		case "ah":
			return "gesso nero";
		case "ai":
			return "sanguigna";
		case "aj":
			return "acquarello";
		case "ak":
			return "tempera";
		case "al":
			return "pittura a guazzo";
		case "am":
			return "pastello";
		case "an":
			return "olio";
		case "ba":
			return "pennarello";
		case "bb":
			return "macchia";
		case "bc":
			return "carboncino";
		case "bd":
			return "seppia";
		case "be":
			return "inchiostro da scrittura";
		case "bf":
			return "caseina";
		case "bg":
			return "doratura";
		case "bh":
			return "decorato a fuoco";
		case "bi":
			return "acrilico";
		case "bj":
			return "collage";
		case "bk":
			return "punta d'argento";
		case "bl":
			return "aerografo";
		case "vv":
			return "compisito";

		default:
			return null;
		}
	}

	private String getSubjectCod3(String key) {
		switch (key) {
		case "ba":
			return "incisione su legno";
		case "bb":
			return "incisione su legno a chiaroscuro";
		case "bc":
			return "xilografia a linea bianca";
		case "bd":
			return "camaieu";
		case "be":
			return "elioincisione";
		case "bf":
			return "cromolitografia";
		case "bg":
			return "linoleum";
		case "bh":
			return "acquaforte";
		case "bi":
			return "litografia";
		case "bj":
			return "fotolitografia";
		case "bk":
			return "zincografia";
		case "bl":
			return "algrafia";
		case "bm":
			return "acquatinta";
		case "bn":
			return "reservage";
		case "ca":
			return "vernis-mou";
		case "cb":
			return "incisione";
		case "cc":
			return "maniera a matita";
		case "cd":
			return "incisione a bulino";
		case "ce":
			return "Puntasecca";
		case "cf":
			return "maniera nera";
		case "cg":
			return "monotipo";
		case "ch":
			return "serigrafia";
		case "ci":
			return "incisione su metallo";
		case "cj":
			return "computer grafica";
		case "ck":
			return "fotocopia";
		case "vv":
			return "composito";

		default:
			return null;
		}
	}

	private String getSubjectCod4(String key) {
		switch (key) {
		case "aa":
			return "disegno architettonico";
		case "ab":
			return "copertina del documento";
		case "ac":
			return "sticker";
		case "ad":
			return "poster";
		case "ae":
			return "cartolina";
		case "af":
			return "biglietto di auguri";
		case "ag":
			return "diagramma";
		case "ah":
			return "carte da gioco";
		case "ai":
			return "flash card";
		case "aj":
			return "ephemera";
		case "an":
			return "calendario";
		case "as":
			return "biglietto segnaposto";
		case "au":
			return "santino";
		case "vv":
			return "composito";

		default:
			return null;
		}
	}

}
