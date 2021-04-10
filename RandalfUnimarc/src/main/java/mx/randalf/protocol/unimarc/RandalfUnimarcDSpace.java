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

import mx.randalf.standard.dSpace.Dcvalue;
import mx.randalf.standard.dSpace.Dublin_core;
import mx.randalf.standard.dSpace.Element;
import mx.randalf.standard.dSpace.Qualifier;

/**
 * @author massi
 *
 */
public class RandalfUnimarcDSpace extends RandalfUnimarc<Dublin_core> {

	private boolean qualificato = false;

	public RandalfUnimarcDSpace() {
		super(true, true);
	}

	@Override
	protected Dublin_core init(Record record) {
		Dublin_core dublin_core = null;
		Collection<? extends Dcvalue> value = null;
		qualificato = false;

//		System.out.println(key);
		dublin_core = new Dublin_core();

		value = getTitle(record, Element.TITLE, Qualifier.NONE);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getCreator(record, Element.CONTRIBUTOR, Qualifier.AUTHOR);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getContributor(record, Element.CONTRIBUTOR, Qualifier.AUTHOR);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getSubject(record, Element.SUBJECT, Qualifier.NONE);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getPublisher(record, Element.PUBLISHER, Qualifier.NONE);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getDate(record, Element.DATE, Qualifier.ISSUED);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getType(record, Element.TYPE, Qualifier.NONE);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getFormat(record, Element.FORMAT, Qualifier.NONE);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getIdentifier(record, Element.IDENTIFIER, Qualifier.OTHER);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getSource(record, Element.SOURCE, Qualifier.CONTENT);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		value = getLanguage(record, Element.LANGUAGE, Qualifier.ISO);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}
//
//		value = getRelation(record, Element.RELATION, Qualifier.OTHERLINKS);
//		if (value != null) {
//			dublin_core.getDcvalue().addAll(value);
//		}
		
		value = getRights(record, Element.RIGHTS, Qualifier.LICENSE);
		if (value != null) {
			dublin_core.getDcvalue().addAll(value);
		}

		return dublin_core;
	}

	private Collection<? extends Dcvalue> getRights(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "856");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						testo += (testo.equals("") ? "" : ", ") + value;

						break;
					default:
						System.out.println("856 NON QUAL Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 856: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

//
//	private Collection<? extends Dcvalue> getRelation(Record record, Element relation, Qualifier otherlinks) {
//		List<DataField> dataFields = null;
//		String key = null;
//		
//		for (int x=400; x<600; x++) {
//			key = String.valueOf(x);
//			if (key.indexOf("9")==-1) {
//				dataFields = getDF(record.getDataFields(), key);
//				if (dataFields != null) {
//					System.out.println(key);
//				}
//			}
//		}
//		// TODO Auto-generated method stub
//		return result;
//	}

	private Collection<? extends Dcvalue> getLanguage(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "101");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						testo += (testo.equals("") ? "" : ", ") + value;

						break;
					default:
						System.out.println("101 NON QUAL Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 101: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getSource(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;

		if (qualificato) {
			result = getSourceQualiticato(record, element, qualifier);
		} else {
			result = getSourceNonQualiticato(record, element, qualifier);
		}
		return result;
	}

	private Vector<Dcvalue> getSourceQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
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
							System.out.println("300 NON QUAL  Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value;

						break;
					default:
						System.out.println("300 NON QUAL Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 300: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Vector<Dcvalue> getSourceNonQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "324");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("324 NON QUAL  Cod: " + subfield.getCode() + " => " + value);
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
						System.out.println("324 NON QUAL Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 324: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getIdentifier(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<ControlField> controlFields = null;

		controlFields = getCF(record.getControlFields(), "001");
		if (controlFields != null) {
			for (ControlField controlField : controlFields) {

				if (result == null) {
					result = new Vector<Dcvalue>();
				}
//				System.out.println("001: " + controlField.getData());
				result.add(genDcvalue(element, qualifier, controlField.getData()));

			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getFormat(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "336");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'd':
						if (!testo.trim().equals("")) {
							System.out.println("336 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'a':
					case 'c':
					case 'e':
					case 'g':
						break;
					default:
						System.out.println("336 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
					System.out.println("336: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}

		dataFields = getDF(record.getDataFields(), "856");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'd':
						if (!testo.trim().equals("")) {
							System.out.println("856 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'a':
					case 'c':
					case 'e':
					case 'g':
						break;
					default:
						System.out.println("856 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
					System.out.println("856: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}

		return result;
	}

	private Collection<? extends Dcvalue> getType(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;
		String typeRec = null;

		typeRec = Character.valueOf(record.getLeader().getTypeOfRecord()).toString();

//		System.out.println("Format: " + typeRec + " -> " + TipoRecord.valueOf(typeRec).value());
		if (result == null) {
			result = new Vector<Dcvalue>();
		}
		result.add(genDcvalue(element, qualifier, TipoRecord.valueOf(typeRec).value()));

		dataFields = getDF(record.getDataFields(), "135");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'd':
						if (!testo.trim().equals("")) {
							System.out.println("135 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'a':
					case 'c':
					case 'e':
					case 'g':
						break;
					default:
						System.out.println("135 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
					System.out.println("135: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}

		dataFields = getDF(record.getDataFields(), "230");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'd':
						if (!testo.trim().equals("")) {
							System.out.println("230 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'a':
					case 'c':
					case 'e':
					case 'g':
						break;
					default:
						System.out.println("230 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
					System.out.println("230: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getDate(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;

		if (qualificato) {
			result = getDateQualiticato(record, element, qualifier);
		} else {
			result = getDateNonQualiticato(record, element, qualifier);
		}
		return result;
	}

	private Vector<Dcvalue> getDateQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "210");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'd':
						if (!testo.trim().equals("")) {
							System.out.println("210 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'a':
					case 'c':
					case 'e':
					case 'g':
						break;
					default:
						System.out.println("210 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 210: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Vector<Dcvalue> getDateNonQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "100");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!value.substring(9, 13).trim().equals("")) {
							testo = value.substring(9, 13).trim();
						}
						if (!value.substring(13, 17).trim().equals("")) {
							testo += (testo.equals("") ? "" : "-") + value.substring(13, 17).trim();
						}
						if (!testo.trim().equals("")) {
							if (result == null) {
								result = new Vector<Dcvalue>();
							}
//							System.out.println("Creator 100: " + testo);
							result.add(genDcvalue(Element.PUBLISHER, Qualifier.NONE, testo));
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					default:
						System.out.println("100 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getContributor(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		Vector<Dcvalue> extra = null;

		result = getContributorNonQualiticato(record, element, qualifier);

		if (result == null) {
			result = getContributorQualiticato(record, element, qualifier);
		} else {
			extra = getContributorQualiticato(record, element, qualifier);
			if (extra != null) {
				result.addAll(extra);
			}
		}
		return result;
	}

	private Vector<Dcvalue> getContributorNonQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "200");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'g':
						if (!testo.trim().equals("")) {
							System.out.println("200 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 200$g: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Vector<Dcvalue> getContributorQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "702");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						if (!testo.trim().equals("")) {
							System.out.println("702 Cod: " + subfield.getCode() + " => " + value);
						}
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'b':
						testo += value;

						break;
					case 'c':
						testo += (testo.equals("") ? "" : " ") + value.trim();

						break;
					case 'd':
						testo += value;

						break;
					case 'f':
						testo += (testo.equals("") ? "" : " ") + value.trim();

						break;
					case '4':

						if (CodiceRelazione.valueOf("_" + value) != null) {
							testo = "[" + CodiceRelazione.valueOf("_" + value).value() + "] " + testo;
						} else {
							System.out.println("702 Cod: 4 Val: " + value);
							testo = "[" + value + "] " + testo;
						}

						break;

					case '3':
					case '5':
						break;
					default:
						System.out.println("702 Cod: " + subfield.getCode() + " => " + value);

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 702: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}

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
						testo += value;

						break;
					case 'c':
						testo += (testo.equals("") ? "" : " ") + value.trim();

						break;
					case 'd':
						testo += value;

						break;
					case 'f':
						testo += (testo.equals("") ? "" : " ") + value.trim();

						break;
					case '4':

						if (CodiceRelazione.valueOf("_" + value) != null) {
							testo = "[" + CodiceRelazione.valueOf("_" + value).value() + "] " + testo;
						} else {
							System.out.println("712 Cod: 4 Val: " + value);
							testo = "[" + value + "] " + testo;
						}

						break;

					case '3':
					case '5':
						break;
					default:
						System.out.println("712 Cod: " + subfield.getCode() + " => " + value);

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 712: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getPublisher(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		Vector<Dcvalue> extra = null;

		result = getPublisherNonQualiticato(record, element, qualifier);

		if (result == null) {
			result = getPublisherQualiticato(record, element, qualifier);
		} else {
			extra = getPublisherQualiticato(record, element, qualifier);
			if (extra != null) {
				result.addAll(extra);
			}
		}
		return result;
	}

	private Vector<Dcvalue> getPublisherNonQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		List<DataField> dataFields = null;
		String testo = "";
		String value = null;

		dataFields = getDF(record.getDataFields(), "210");
		if (dataFields != null) {
			for (DataField dataField : dataFields) {
				testo = "";
				for (Subfield subfield : dataField.getSubfields()) {
					value = subfield.getData();
					switch (subfield.getCode()) {
					case 'a':
						testo += (testo.equals("") ? "" : " ; ") + value.trim();

						break;
					case 'c':
						testo += (testo.equals("") ? "" : ": ") + value.trim();

						break;
					case 'd':
						testo += (testo.equals("") ? "" : ", ") + value.trim();

						break;
					case 'e':
						testo += (testo.equals("") ? "" : " (") + value.trim();

						break;
					case 'g':
						testo += (testo.equals("") ? "" : " ") + value.trim() + ")";

						break;
					default:
						System.out.println("210 Cod: " + subfield.getCode() + " => " + value);
//					testo += (testo.equals("") ? "" : " ; ") + subfield.getData();

						break;
					}
				}
				if (!testo.trim().equals("")) {
					if (result == null) {
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 210: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Vector<Dcvalue> getPublisherQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
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
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 712: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getSubject(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;

		if (qualificato) {
			result = getSubjectQualiticato(record, element, qualifier);
		} else {
			result = getSubjectNonQualiticato(record, element, qualifier);
		}
		return result;
	}

	private Vector<Dcvalue> getSubjectNonQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
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
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 610: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Vector<Dcvalue> getSubjectQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
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
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 606: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
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
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 676: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
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
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 675: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
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
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 680: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
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
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 686: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getCreator(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
		Vector<Dcvalue> extra = null;

		if (qualificato) {
			result = getCreatorQualiticato(record, element, qualifier);
		} else {
			result = getCreatorNonQualiticato(record, element, qualifier);
		}
		extra = gertCreatorExtra(record, element, qualifier);
		if (extra != null) {
			result.addAll(extra);
		}
		return result;
	}

	private Vector<Dcvalue> getCreatorNonQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
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
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 730: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Vector<Dcvalue> gertCreatorExtra(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
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
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 701: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
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
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 711: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}

		return result;
	}

	private Vector<Dcvalue> getCreatorQualiticato(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
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
						result = new Vector<Dcvalue>();
					}
//					System.out.println("Creator 700: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
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
						result = new Vector<Dcvalue>();
					}
					System.out.println("Creator 710: " + testo);
					result.add(genDcvalue(element, qualifier, testo));
				}
			}
		}
		return result;
	}

	private Collection<? extends Dcvalue> getTitle(Record record, Element element, Qualifier qualifier) {
		Vector<Dcvalue> result = null;
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
						result = new Vector<Dcvalue>();
					}
//					System.out.println(testo);
					result.add(genDcvalue(element, qualifier, testo));
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
						result = new Vector<Dcvalue>();
					}
//					System.out.println(testo);
					result.add(genDcvalue(element, qualifier, testo));
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
							result = new Vector<Dcvalue>();
						}
//						System.out.println(testo);
						result.add(genDcvalue(element, qualifier, testo));
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
							result = new Vector<Dcvalue>();
						}
//						System.out.println(testo);
						result.add(genDcvalue(element, qualifier, testo));
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
							result = new Vector<Dcvalue>();
						}
						System.out.println(testo);
						result.add(genDcvalue(element, qualifier, testo));
					}
				}
			}

		}
		return result;
	}

	private Dcvalue genDcvalue(Element element, Qualifier qualifier, String testo) {
		Dcvalue result = null;

		result = new Dcvalue();
		result.setElement(element);
		result.setQualifier(qualifier);
		result.getContent().add(testo);
		return result;
	}
}
