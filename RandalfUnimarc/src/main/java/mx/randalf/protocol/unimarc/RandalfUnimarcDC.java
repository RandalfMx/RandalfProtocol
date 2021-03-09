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

import it.sbn.iccu.metaag1.Bib;
import mx.randalf.mag.MagXsd;

/**
 * @author massi
 *
 */
public class RandalfUnimarcDC extends RandalfUnimarc<Bib> {

	private boolean qualificato = false;

	private MagXsd magXsd = null;

	/**
	 * 
	 */
	public RandalfUnimarcDC() {
		super(true, true);
		magXsd = new MagXsd(null);
	}

	protected Bib init(Record record, String key) {
		Bib bib = null;
		Collection<? extends SimpleLiteral> value = null;

		qualificato = false;

		bib = new Bib();
		System.out.println("------------------");
		System.out.println("Bid: " + key);
		bib.getIdentifier().add(magXsd.genSimpleLiteral(key));

		value = getTitle(record);
		if (value != null) {
			bib.getTitle().addAll(value);
		}

		value = getCreator(record);
		if (value != null) {
			bib.getCreator().addAll(value);
		}

		value = getSubject(record);
		if (value != null) {
			bib.getSubject().addAll(value);
		}

		value = getDescription(record);
		if (value != null) {
			bib.getDescription().addAll(value);
		}

		value = getPublisher(record);
		if (value != null) {
			bib.getPublisher().addAll(value);
		}
		return bib;
	}

	private Collection<? extends SimpleLiteral> getPublisher(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> extra = null;

		result = getPublisherNonQualiticato(record);

		if (result == null) {
			result = getPublisherQualiticato(record);
		} else {
			extra = getPublisherQualiticato(record);
			if (extra == null) {
				result.addAll(extra);
			}
		}
		return result;
	}

	private Vector<SimpleLiteral> getPublisherNonQualiticato(Record record) {
		// TODO Auto-generated method stub
		return null;
	}

	private Vector<SimpleLiteral> getPublisherQualiticato(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "712");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("712 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'b':
						testo += (testo.equals("") ? "" : " ") + value.trim();

						break;
					case 'c':
						testo += (testo.equals("") ? "" : " ") + value.trim();

						break;
					case 'd':
						testo += (testo.equals("") ? "" : " ") + value.trim();

						break;
					case 'f':
						testo += (testo.equals("") ? "" : " ") + value.trim();

						break;
					case '3':
					case '4':
					case '5':
						break;
					default:
  						System.out.println("712 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
//					System.out.println("Creator 712: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends SimpleLiteral> getDescription(Record record) {
		Vector<SimpleLiteral> result = null;

		if (qualificato) {
			result = getDescriptionQualiticato(record);
		} else {
			result = getDescriptionNonQualiticato(record);
		}
		return result;
	}

	private Vector<SimpleLiteral> getDescriptionNonQualiticato(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "300");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("300 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'x':
						testo += (testo.equals("") ? "" : " - ") + value.trim();

						break;
					case '2':
					case '3':
					case '9':
						break;
					default:
						System.out.println("300 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
//					System.out.println("Creator 300: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}
		return result;
	}

	private Vector<SimpleLiteral> getDescriptionQualiticato(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "330");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("330 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'x':
						testo += (testo.equals("") ? "" : " - ") + value.trim();

						break;
					case '2':
					case '3':
					case '9':
						break;
					default:
						System.out.println("330 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 330: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends SimpleLiteral> getSubject(Record record) {
		Vector<SimpleLiteral> result = null;

		if (qualificato) {
			result = getSubjectQualiticato(record);
		} else {
			result = getSubjectNonQualiticato(record);
		}
		return result;
	}

	private Vector<SimpleLiteral> getSubjectNonQualiticato(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "610");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("610 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'x':
						testo += (testo.equals("") ? "" : " - ") + value.trim();

						break;
					case '2':
					case '3':
					case '9':
						break;
					default:
						System.out.println("610 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 610: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}
		return result;
	}

	private Vector<SimpleLiteral> getSubjectQualiticato(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "606");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("606 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'x':
						testo += (testo.equals("") ? "" : " - ") + value.trim();

						break;
					case '2':
					case '3':
					case '9':
						break;
					default:
						System.out.println("606 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
//					System.out.println("Creator 606: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}

		dataFields = getDF(record.getDataFields(), "676");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("676 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'x':
						testo += (testo.equals("") ? "" : " - ") + value.trim();

						break;
					case '2':
					case '3':
					case '9':
						break;
					default:
						System.out.println("676 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 676: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}

		dataFields = getDF(record.getDataFields(), "675");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("675 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'x':
						testo += (testo.equals("") ? "" : " - ") + value.trim();

						break;
					case '2':
					case '3':
					case '9':
						break;
					default:
						System.out.println("675 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 675: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}

		dataFields = getDF(record.getDataFields(), "680");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("680 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'x':
						testo += (testo.equals("") ? "" : " - ") + value.trim();

						break;
					case '2':
					case '3':
					case '9':
						break;
					default:
						System.out.println("680 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 680: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}

		dataFields = getDF(record.getDataFields(), "686");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("686 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'x':
						testo += (testo.equals("") ? "" : " - ") + value.trim();

						break;
					case '2':
					case '3':
					case '9':
						break;
					default:
						System.out.println("686 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 686: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends SimpleLiteral> getCreator(Record record) {
		Vector<SimpleLiteral> result = null;
		Vector<SimpleLiteral> extra = null;

		if (qualificato) {
			result = getCreatorQualiticato(record);
		} else {
			result = getCreatorNonQualiticato(record);
		}
		extra = gertCreatorExtra(record);
		if (extra != null) {
			result.addAll(extra);
		}
		return result;
	}

	private Vector<SimpleLiteral> getCreatorNonQualiticato(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "730");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("730 NON QUAL  Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value;

						break;
					case 'b':
						testo += value;

						break;
					case '3':
						break;
					case '4':
						break;
					default:
						System.out.println("730 NON QUAL Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 730: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}
		return result;
	}

	private Vector<SimpleLiteral> gertCreatorExtra(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "701");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("Creator NON QUAL  Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value;

						break;
					case 'b':
						testo += value;

						break;
					case '3':
						break;
					case '4':
						break;
					default:
						System.out.println("gertCreatorInit NON QUAL Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
//					System.out.println("Creator 701: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}

		dataFields = getDF(record.getDataFields(), "711");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("Creator NON QUAL  Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value;

						break;
					case 'b':
						testo += value;

						break;
					case '3':
						break;
					case '4':
						break;
					default:
						System.out.println("gertCreatorInit NON QUAL Cod: " + subfield.getCode() + " => " + value);
//						testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 711: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}

		return result;
	}

	private Vector<SimpleLiteral> getCreatorQualiticato(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "700");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("Creator700 NON QUAL  Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value;

						break;
					case 'b':
						testo += value;

						break;
					case 'c':
						testo += value;

						break;
					case 'f':
						testo += value;

						break;

					case '3':
					case '4':
						break;
					default:
						System.out.println(" 700 NON QUAL Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
//					System.out.println("Creator 700: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}

		dataFields = getDF(record.getDataFields(), "710");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("Creator710 NON QUAL  Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value;

						break;
					case 'b':
						testo += value;

						break;
					case 'c':
						testo += value;

						break;
					case 'f':
						testo += value;

						break;

					case '3':
					case '4':
						break;
					default:
						System.out.println(" 710 NON QUAL Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
					System.out.println("Creator 710: " + testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends SimpleLiteral> getTitle(Record record) {
		Vector<SimpleLiteral> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "200");

		for (DataField dataField : dataFields) {

			if (dataField.getIndicator1() == '0') {
				// TODO: Titolo non Qualificato / Significativo
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("NON QUAL  Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value;

						break;
					default:
						System.out.println("NON QUAL Cod: " + subfield.getCode() + " => " + value);
//						testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
//					System.out.println(testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			} else {
				qualificato = true;
				// TODO: Titolo Qualificato / Significativo
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (Character.valueOf(value.charAt(0)).hashCode() == 152) {
							value = value.substring(1);
						}
						value = value.replace((char) 156, '*');
						if (!testo.trim().equals("")) {
							System.out.println("200 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value;

						break;

					case 'c':
						System.out.println("Cod: " + subfield.getCode() + " => " + value);
//						testo += (testo.equals("") ? "" : " : ") + value;

						break;

					case 'e':
						testo += (testo.equals("") ? "" : " : ") + value;

						break;

					default:
//						System.out.println("Cod: " + subfield.getCode() + " => " + value);
//						testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<SimpleLiteral>();
					}
//					System.out.println(testo);
					result.add(magXsd.genSimpleLiteral(testo));
				}
			}
		}

		if (qualificato) {

			dataFields = getDF(record.getDataFields(), "517");

			if (dataFields != null) {
				for (DataField dataField : dataFields) {
//					System.out.println("517");
					testo = "";
					for (Subfield subfield : dataField.getSubfields()) {
						value = subfield.getData();
						switch (subfield.getCode()) {
						case 'a':
							if (!testo.trim().equals("")) {
								System.out.println("517 Cod: " + subfield.getCode() + " => " + value);
							}
							testo += (testo.equals("") ? "" : " ; ") + value;
							break;
						default:
//							System.out.println("Cod: " + subfield.getCode() + " => " + value);
//							testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

							break;
						}
					}
					if (!testo.trim().equals("")) {
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
//						System.out.println(testo);
						result.add(magXsd.genSimpleLiteral(testo));
					}
				}
			}

			dataFields = getDF(record.getDataFields(), "500");

			if (dataFields != null) {
				for (DataField dataField : dataFields) {
//					System.out.println("500");
					testo = "";
					for (Subfield subfield : dataField.getSubfields()) {
						value = subfield.getData();
						switch (subfield.getCode()) {
						case 'a':
							if (!testo.trim().equals("")) {
								System.out.println("500 Cod: " + subfield.getCode() + " => " + value);
							}
							testo += (testo.equals("") ? "" : " ; ") + value;
							break;
						default:
//							System.out.println("500 Cod: " + subfield.getCode() + " => " + value);
//							testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

							break;
						}
					}
					if (!testo.trim().equals("")) {
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
//						System.out.println(testo);
						result.add(magXsd.genSimpleLiteral(testo));
					}
				}
			}

			dataFields = getDF(record.getDataFields(), "530");

			if (dataFields != null) {
				for (DataField dataField : dataFields) {
					System.out.println("530");
					testo = "";
					for (Subfield subfield : dataField.getSubfields()) {
						value = subfield.getData();
						switch (subfield.getCode()) {
						case 'a':
							if (!testo.trim().equals("")) {
								System.out.println("530 Cod: " + subfield.getCode() + " => " + value);
							}
							testo += (testo.equals("") ? "" : " ; ") + value;
							break;
						default:
							System.out.println("530 Cod: " + subfield.getCode() + " => " + value);
//							testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

							break;
						}
					}
					if (!testo.trim().equals("")) {
						if (result == null) {
							result = new Vector<SimpleLiteral>();
						}
						System.out.println(testo);
						result.add(magXsd.genSimpleLiteral(testo));
					}
				}
			}

		}
		return result;
	}
}
