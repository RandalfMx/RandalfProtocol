/**
 * 
 */
package mx.randalf.protocol.sbn.metadata;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;

/**
 * @author massi
 *
 */
public class RandalfMetadata {

	private boolean significativo = false;

	private String level = null;

	private String levelDesc = null;

	private List<String> titles = null;

	private List<String> creators = null;

	private List<String> subjects = null;

	private List<String> descriptions = null;

	private List<String> publishers = null;

	private List<String> contributors = null;

	private List<String> dates = null;

	private String typeId = null;
	private List<String> types = null;

	private List<String> formats = null;

	private List<String> identifiers = null;

	private List<String> sources = null;

	private List<String> languages = null;

	private List<String> relations = null;

	private List<String> coverages = null;

	private List<String> rights = null;

	private List<String> urls = null;
	
	/**
	 * 
	 */
	public RandalfMetadata(Record record, String idIstituto) {
		analizza(record,  idIstituto);
	}

	private void analizza(Record record, String idIstituto) {

		checkLevel(record);
		checkTitle(record);
		checkCreator(record);
		checkSubject(record);
		checkDescription(record, idIstituto);
		checkPublisher(record);
		checkContributor(record);
		checkDate(record);
		checkType(record);
		checkFormat(record);
		checkIdentifier(record);
		checkSource(record);
		checkLanguage(record);
		checkRelation(record);
		checkCoverage(record);
		checkRights(record);
		checkUrls(record);
	}

	private void checkUrls(Record record) {
		List<VariableField> dataFields = null;
		Iterator<VariableField> i = null;
		DataField dataField = null;
		List<Subfield> subfields = null;

		dataFields = record.getVariableFields("856");
		i = dataFields.iterator();
		while (i.hasNext()) {
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("u");
			if (subfields != null) {
				for (Subfield subfield : subfields) {
					if (subfield.getData() != null) {
						if (urls == null) {
							urls = new Vector<String>();
						}
						urls.add(subfield.getData());
					}
				}
			}
			subfields = dataField.getSubfields("2");
			if (subfields != null) {
				for (Subfield subfield : subfields) {
					if (subfield.getData() != null) {
						if (urls == null) {
							urls = new Vector<String>();
						}
						urls.add(subfield.getData());
					}
				}
			}
		}
	}

	private void checkLevel(Record record) {
		switch (record.getLeader().getImplDefined1()[0]) {
		case 'a':
			level="a";
			levelDesc="analitico (parte componente)";
			break;
		case 'i':
			level="i";
			levelDesc="risorsa integrativa";
			break;
		case 'm':
			level="m";
			levelDesc="monografia";
			break;
		case 's':
			level="s";
			levelDesc="risorsa in continuazione";
			break;
		case 'c':
			level="c";
			levelDesc="collezione";
			break;
		}
	}

	private void checkRights(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		if (!significativo) {
			dataFields = record.getVariableFields("300");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (rights == null) {
						rights = new Vector<String>();
					}
					rights.add(testo);
				}
			}
		} else {
			dataFields = record.getVariableFields("856");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("u");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (rights == null) {
						rights = new Vector<String>();
					}
					rights.add(testo);
				}
			}
		}
	}

	private void checkCoverage(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		if (!significativo) {
			dataFields = record.getVariableFields("300");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (coverages == null) {
						coverages = new Vector<String>();
					}
					coverages.add(testo);
				}
			}
		} else {
//			dataFields = record.getVariableFields("102");
//			i = dataFields.iterator();
//			while (i.hasNext()) {
//				testo = "";
//				dataField = (DataField) i.next();
//				subfields = dataField.getSubfields("a");
//				for (Subfield subfield : subfields) {
//					if (!testo.trim().equals("")) {
//						testo += " ";
//					}
//					testo += subfield.getData();
//				}
//
//				if (!testo.trim().equals("")) {
//					if (coverages == null) {
//						coverages = new Vector<String>();
//					}
//					coverages.add(testo);
//				}
//			}

			dataFields = record.getVariableFields("102");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("b");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (coverages == null) {
						coverages = new Vector<String>();
					}
					coverages.add(testo);
				}
			}

			dataFields = record.getVariableFields("606");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (coverages == null) {
						coverages = new Vector<String>();
					}
					coverages.add(testo);
				}
			}

			dataFields = record.getVariableFields("620");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (coverages == null) {
						coverages = new Vector<String>();
					}
					coverages.add(testo);
				}
			}
		}

	}

	private void checkRelation(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		if (!significativo) {
			dataFields = record.getVariableFields("300");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (relations == null) {
						relations = new Vector<String>();
					}
					relations.add(testo);
				}
			}
		} else {
			
			dataFields = record.getVariableFields("517");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (relations == null) {
						relations = new Vector<String>();
					}
					relations.add("'variante del titolo:' "+testo.trim());
				}
			}
			
			
			
			
			
			String[] tags = { "410", "411", "412", "413", "421", "422", "423", "424", "425", "430", "431", "432", "433",
					"434", "435", "436", "437", "440", "441", "442", "443", "444", "445", "446", "447", "448", "451",
					"452", "453", "454", "455", "456", "461", "462", "463", "464", "470", "481", "482", "488", "500",
					"501", "503", "510", "511", "512", "513", "514", "515", "516", "518", "520", "530", "531",
					"532", "540", "541", "545", "560" };
			dataFields = record.getVariableFields(tags);
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (relations == null) {
						relations = new Vector<String>();
					}
					relations.add(testo);
				}
			}
		}

	}

	private void checkLanguage(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		dataFields = record.getVariableFields("101");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (languages == null) {
					languages = new Vector<String>();
				}
				languages.add(testo);
			}
		}
	}

	private void checkSource(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		if (!significativo) {
			dataFields = record.getVariableFields("300");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (sources == null) {
						sources = new Vector<String>();
					}
					sources.add(testo);
				}
			}
		} else {
			dataFields = record.getVariableFields("324");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (sources == null) {
						sources = new Vector<String>();
					}
					sources.add(testo);
				}
			}
		}
	}

	private void checkIdentifier(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;
		String[] st = null;

		for (ControlField controlField : record.getControlFields()) {
			if (controlField.getTag().equals("001")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				testo = controlField.getData();
				st = testo.split("\\\\");
				if (st.length==4) {
					identifiers.add(st[2]+st[3]);
				} else {
					identifiers.add(controlField.getData());
				}
			}
		}

		if (identifiers == null) {
		dataFields = record.getVariableFields("010");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				identifiers.add(testo);
			}
		}

		dataFields = record.getVariableFields("011");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				identifiers.add(testo);
			}
		}

		dataFields = record.getVariableFields("012");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				identifiers.add(testo);
			}
		}

		dataFields = record.getVariableFields("013");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				identifiers.add(testo);
			}
		}

		dataFields = record.getVariableFields("020");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("b");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				identifiers.add(testo);
			}
		}

		dataFields = record.getVariableFields("071");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				identifiers.add(testo);
			}
		}

		dataFields = record.getVariableFields("856");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("g");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				identifiers.add(testo);
			}
		}

		dataFields = record.getVariableFields("856");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("u");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (identifiers == null) {
					identifiers = new Vector<String>();
				}
				identifiers.add(testo);
			}
		}
		}
	}

	private void checkFormat(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		dataFields = record.getVariableFields("336");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (formats == null) {
					formats = new Vector<String>();
				}
				formats.add(testo);
			}
		}

		dataFields = record.getVariableFields("856");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("q");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (formats == null) {
					formats = new Vector<String>();
				}
				formats.add(testo);
			}
		}
	}

	private void checkType(Record record) {
		String testo = null;

		typeId= Character.valueOf(record.getLeader().getTypeOfRecord()).toString();
		switch (record.getLeader().getTypeOfRecord()) {
		case 'a':
			testo = "testo a stampa";
			break;
		case 'b':
			testo = "materiale manoscritto";
			break;
		case 'c':
			testo = "partiture musicali a stampa";
			break;
		case 'd':
			testo = "partiture musicali manoscritte";
			break;
		case 'e':
			testo = "materiale cartografico a stampa";
			break;
		case 'f':
			testo = "materiale cartografico manoscritto";
			break;
		case 'g':
			testo = "materiali video e proiettato (film, filmine, diapositive, trasparenti, videoregistrazioni)";
			break;
		case 'i':
			testo = "registrazioni sonore non musicali";
			break;
		case 'j':
			testo = "registrazioni sonore musicali";
			break;
		case 'k':
			testo = "grafica bidimensionale (dipinti, disegni etc.)";
			break;
		case 'l':
			testo = "risorsa elettronica";
			break;
		case 'm':
			testo = "materiale misto";
			break;
		case 'r':
			testo = "manufatti tridimensionali o oggetti presenti in natura";
			break;
		}
		if (testo != null) {
			if (types == null) {
				types = new Vector<String>();
			}
			types.add(testo);
		}
	}

	private void checkDate(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;
		boolean bData = false;

		dataFields = record.getVariableFields("100");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();

				if (!testo.substring(9, 13).trim().equals("")) {
					bData = true;
					if (dates == null) {
						dates = new Vector<String>();
					}
					dates.add(testo.substring(9, 13).trim());
				}

				if (!testo.substring(13, 17).trim().equals("")) {
					bData = true;
					if (dates == null) {
						dates = new Vector<String>();
					}
					dates.add(testo.substring(13, 17).trim());
				}
			}
		}

		if (!bData) {

			dataFields = record.getVariableFields("210");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("d");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();

					if (dates == null) {
						dates = new Vector<String>();
					}
					dates.add(testo);
				}
				subfields = dataField.getSubfields("h");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();

					if (dates == null) {
						dates = new Vector<String>();
					}
					dates.add(testo);
				}
			}
		}
	}

	private void checkContributor(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		dataFields = record.getVariableFields("200");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("g");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (contributors == null) {
					contributors = new Vector<String>();
				}
				contributors.add(testo);
			}
		}

		dataFields = record.getVariableFields("702");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}
			subfields = dataField.getSubfields("4");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (contributors == null) {
					contributors = new Vector<String>();
				}
				contributors.add(testo);
			}
		}

		dataFields = record.getVariableFields("712");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}
			subfields = dataField.getSubfields("4");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (contributors == null) {
					contributors = new Vector<String>();
				}
				contributors.add(testo);
			}
		}
	}

	private void checkPublisher(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		// Verifica per "Non qualificato"
		dataFields = record.getVariableFields("210");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}
			subfields = dataField.getSubfields("c");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " : ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (publishers == null) {
					publishers = new Vector<String>();
				}
				publishers.add(testo);
			}
		}
		dataFields = record.getVariableFields("712");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}
			subfields = dataField.getSubfields("b");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " : ";
				}
				testo += subfield.getData();
			}
			subfields = dataField.getSubfields("c");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " : ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (publishers == null) {
					publishers = new Vector<String>();
				}
				publishers.add(testo);
			}
		}

	}

	private void checkDescription(Record record, String idIstituto) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;
		String testo2 = null;
		String testo300 = "";

		// Verifica per "Non qualificato"
		dataFields = record.getVariableFields("300");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (!testo300.equals("")) {
					testo300 += " ; ";
				}
				testo300 += testo;
			}
		}

		if (!testo300.trim().equals("")) {
			if (descriptions == null) {
				descriptions = new Vector<String>();
			}
			descriptions.add(testo300);
		}

		dataFields = record.getVariableFields("330");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}

			if (!testo.trim().equals("")) {
				if (descriptions == null) {
					descriptions = new Vector<String>();
				}
				descriptions.add(testo);
			}
		}

		dataFields = record.getVariableFields("899");
		i = dataFields.iterator();
		while (i.hasNext()) {
			testo = "";
			testo2 = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("1");
			for (Subfield subfield : subfields) {
				if (!testo.trim().equals("")) {
					testo += " ";
				}
				testo += subfield.getData();
			}
			subfields = dataField.getSubfields("4");
			for (Subfield subfield : subfields) {
				if (!testo2.trim().equals("")) {
					testo2 += " ";
				}
				testo2 += subfield.getData();
			}

			if (!testo2.trim().equals("") &&
					!testo.trim().equals("") &&
					testo.trim().equalsIgnoreCase(idIstituto)) {
				if (descriptions == null) {
					descriptions = new Vector<String>();
				}
				descriptions.add("[consistenza] "+testo2);
			}
		}
	}

	private void checkSubject(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		if (!significativo) {
			// Verifica per "Non qualificato"
			dataFields = record.getVariableFields("610");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (subjects == null) {
						subjects = new Vector<String>();
					}
					subjects.add(testo);
				}
			}
		} else {
			// Verifica per "Qualificato"

			dataFields = record.getVariableFields("606");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (subjects == null) {
						subjects = new Vector<String>();
					}
					subjects.add(testo);
				}
			}

			dataFields = record.getVariableFields("676");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (subjects == null) {
						subjects = new Vector<String>();
					}
					subjects.add(testo);
				}
			}

			dataFields = record.getVariableFields("675");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (subjects == null) {
						subjects = new Vector<String>();
					}
					subjects.add(testo);
				}
			}

			dataFields = record.getVariableFields("680");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (subjects == null) {
						subjects = new Vector<String>();
					}
					subjects.add(testo);
				}
			}

			dataFields = record.getVariableFields("686");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (subjects == null) {
						subjects = new Vector<String>();
					}
					subjects.add(testo);
				}
			}
		}

	}

	private void checkCreator(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String testo = null;

		if (!significativo) {
			// Verifica per "Non qualificato"
			dataFields = record.getVariableFields("730");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}
				subfields = dataField.getSubfields("4");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (creators == null) {
						creators = new Vector<String>();
					}
					creators.add(testo);
				}
			}
		} else {
			// Verifica per "Qualificato"

			dataFields = record.getVariableFields("200");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("f");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (creators == null) {
						creators = new Vector<String>();
					}
					creators.add(testo);
				}
			}

			dataFields = record.getVariableFields("700");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}
				subfields = dataField.getSubfields("4");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (creators == null) {
						creators = new Vector<String>();
					}
					creators.add(testo);
				}
			}

			dataFields = record.getVariableFields("710");
			i = dataFields.iterator();
			while (i.hasNext()) {
				testo = "";
				dataField = (DataField) i.next();
				subfields = dataField.getSubfields("a");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}
				subfields = dataField.getSubfields("4");
				for (Subfield subfield : subfields) {
					if (!testo.trim().equals("")) {
						testo += " ";
					}
					testo += subfield.getData();
				}

				if (!testo.trim().equals("")) {
					if (creators == null) {
						creators = new Vector<String>();
					}
					creators.add(testo);
				}
			}

			if (testo == null) {

				dataFields = record.getVariableFields("701");
				i = dataFields.iterator();
				while (i.hasNext()) {
					testo = "";
					dataField = (DataField) i.next();
					subfields = dataField.getSubfields("a");
					for (Subfield subfield : subfields) {
						if (!testo.trim().equals("")) {
							testo += " ";
						}
						testo += subfield.getData();
					}
					subfields = dataField.getSubfields("4");
					for (Subfield subfield : subfields) {
						if (!testo.trim().equals("")) {
							testo += " ";
						}
						testo += subfield.getData();
					}

					if (!testo.trim().equals("")) {
						if (creators == null) {
							creators = new Vector<String>();
						}
						creators.add(testo);
					}
				}

				dataFields = record.getVariableFields("711");
				i = dataFields.iterator();
				while (i.hasNext()) {
					testo = "";
					dataField = (DataField) i.next();
					subfields = dataField.getSubfields("a");
					for (Subfield subfield : subfields) {
						if (!testo.trim().equals("")) {
							testo += " ";
						}
						testo += subfield.getData();
					}
					subfields = dataField.getSubfields("4");
					for (Subfield subfield : subfields) {
						if (!testo.trim().equals("")) {
							testo += " ";
						}
						testo += subfield.getData();
					}

					if (!testo.trim().equals("")) {
						if (creators == null) {
							creators = new Vector<String>();
						}
						creators.add(testo);
					}
				}
			}
		}
	}

	private void checkTitle(Record record) {
		List<VariableField> dataFields = null;
		DataField dataField = null;
		Iterator<VariableField> i = null;
		List<Subfield> subfields = null;
		String title = "";

		dataFields = record.getVariableFields("200");
		i = dataFields.iterator();
		while (i.hasNext()) {
			title = "";
			dataField = (DataField) i.next();
			if (dataField.getIndicator1() == '1') {
				significativo = true;
			}
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!title.trim().equals("")) {
					title += " ";
				}
				title += subfield.getData();
			}
			subfields = dataField.getSubfields("c");
			for (Subfield subfield : subfields) {
				if (!title.trim().equals("")) {
					title += " ";
				}
				title += subfield.getData();
			}
			subfields = dataField.getSubfields("e");
			for (Subfield subfield : subfields) {
				if (!title.trim().equals("")) {
					title += " : ";
				}
				title += subfield.getData();
			}

			if (titles == null) {
				titles = new Vector<String>();
			}
			titles.add(title);
		}

//		dataFields = record.getVariableFields("517");
//		i = dataFields.iterator();
//		while (i.hasNext()) {
//			title = "";
//			dataField = (DataField) i.next();
//			subfields = dataField.getSubfields("a");
//			for (Subfield subfield : subfields) {
//				if (!title.trim().equals("")) {
//					title += " ";
//				}
//				title += subfield.getData();
//			}
//			if (titles == null) {
//				titles = new Vector<String>();
//			}
//			titles.add(title);
//		}

		dataFields = record.getVariableFields("500");
		i = dataFields.iterator();
		while (i.hasNext()) {
			title = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!title.trim().equals("")) {
					title += " ";
				}
				title += subfield.getData();
			}
			if (titles == null) {
				titles = new Vector<String>();
			}
			titles.add(title);
		}

		dataFields = record.getVariableFields("530");
		i = dataFields.iterator();
		while (i.hasNext()) {
			title = "";
			dataField = (DataField) i.next();
			subfields = dataField.getSubfields("a");
			for (Subfield subfield : subfields) {
				if (!title.trim().equals("")) {
					title += " ";
				}
				title += subfield.getData();
			}
			if (titles == null) {
				titles = new Vector<String>();
			}
			titles.add(title);
		}
	}

	public List<String> getTitles() {
		return titles;
	}

	public List<String> getCreators() {
		return creators;
	}

	public List<String> getSubjects() {
		return subjects;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public List<String> getPublishers() {
		return publishers;
	}

	public List<String> getContributors() {
		return contributors;
	}

	public List<String> getDates() {
		return dates;
	}

	public boolean isSignificativo() {
		return significativo;
	}

	public List<String> getTypes() {
		return types;
	}

	public String getTypesId() {
		return typeId;
	}
	public List<String> getFormats() {
		return formats;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public List<String> getSources() {
		return sources;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public List<String> getRelations() {
		return relations;
	}

	public List<String> getCoverages() {
		return coverages;
	}

	public List<String> getRights() {
		return rights;
	}

	public String getLevel() {
		return level;
	}

	public String getLevelDesc() {
		return levelDesc;
	}

	public List<String> getUrls() {
		return urls;
	}
}
