/**
 * 
 */
package it.mibac.san;

import javax.xml.datatype.DatatypeConfigurationException;

import it.mibac.san.cat_import.CatRecordBody;
import it.mibac.san.eac_san.BiogHist;
import it.mibac.san.eac_san.Control;
import it.mibac.san.eac_san.CpfDescription;
import it.mibac.san.eac_san.DateSet;
import it.mibac.san.eac_san.DateSet.Date;
import it.mibac.san.eac_san.Description;
import it.mibac.san.eac_san.DescriptiveEntries;
import it.mibac.san.eac_san.DescriptiveEntry;
import it.mibac.san.eac_san.EacCpf;
import it.mibac.san.eac_san.ExistDates;
import it.mibac.san.eac_san.Identity;
import it.mibac.san.eac_san.NameEntry;
import it.mibac.san.eac_san.OtherRecordId;
import it.mibac.san.eac_san.PlaceDate;
import it.mibac.san.eac_san.PlaceDates;
import it.mibac.san.eac_san.Relations;
import it.mibac.san.eac_san.ResourceRelation;
import it.mibac.san.eac_san.Source;
import it.mibac.san.eac_san.Sources;

/**
 * @author massi
 *
 */
public class SoggettoProduttore extends SanMaster<SoggettoProduttoreDati> {

	/**
	 * @throws DatatypeConfigurationException 
	 * 
	 */
	public SoggettoProduttore(String systemId, String name, String mail, String phone, String title, String myAbstract) throws DatatypeConfigurationException {
		super(systemId, name, mail, phone, title, myAbstract, "soggetto produttore");
	}

	@Override
	protected CatRecordBody genCatRecordBody(SoggettoProduttoreDati dati) {
		CatRecordBody catRecordBody = null;
		EacCpf eacCpf = null;
		
		catRecordBody = new CatRecordBody();

		eacCpf = new EacCpf();

		eacCpf.setControl(genControl(dati));

		eacCpf.setCpfDescription(genCpfDescription(dati));

		catRecordBody.getContent().add(eacCpf);
		return catRecordBody;
	}	

	private Control genControl(SoggettoProduttoreDati dati){
		Control control = null;
		OtherRecordId otherRecordId = null;
		Sources sources = null;
		Source source = null;
		
		control = new Control();
		
		otherRecordId = new OtherRecordId();
		otherRecordId.setLocalType(systemId);
		otherRecordId.setValue(dati.getId());
		control.setOtherRecordId(otherRecordId);

		control.setMaintenanceStatus("scheda pubblicata");

		sources = new Sources();
		source = new Source();
		source.setHref(dati.getUrlId());
		sources.setSource(source);
		control.setSources(sources);
		
		return control;
	}

	private CpfDescription genCpfDescription(SoggettoProduttoreDati dati){
		CpfDescription cpfDescription = null;

		cpfDescription = new CpfDescription();

		cpfDescription.setIdentity(genIdentity(dati));

		cpfDescription.setDescription(genDescription(dati));

		cpfDescription.setRelations(gnRelations(dati));
		return cpfDescription;
	}

	private Identity genIdentity(SoggettoProduttoreDati dati){
		Identity identity = null;
		NameEntry nameEntry = null;

		identity = new Identity();
		identity.setEntityType("corporateBody");
		nameEntry = new NameEntry();
		nameEntry.getPart().add(dati.getTitolo());
		identity.getNameEntryOrNameEntryParallel().add(nameEntry);
		return identity;
	}

	private Description genDescription(SoggettoProduttoreDati dati){
		Description description = null;

		description = new Description();

		description.setExistDates(genExistDates(dati));
		description.setPlaceDates(genPlaceDates(dati));
		if (dati.getTipoEnte()!=  null && !dati.getTipoEnte().equals("")){
			description.setDescriptiveEntries(genDescriptiveEntries(dati));
		}

		if (dati.getDescrizione() != null){
			description.setBiogHist(genBiogHist(dati.getDescrizione()));
		}
		return description;
	}

	private ExistDates genExistDates(SoggettoProduttoreDati dati){
		ExistDates existDates = null;

		existDates = new ExistDates();

		existDates.setDateSet(genDateSet(dati));
		return existDates;
	}

	private DateSet genDateSet(SoggettoProduttoreDati dati){
		DateSet dateSet = null;

		dateSet = new DateSet();

		dateSet.getDate().add(genDate("data di istituzione", dati.getDataIstituzione()));
		if (dati.getDataSoppressione()!= null){
			dateSet.getDate().add(genDate("data di soppressione", dati.getDataSoppressione()));
		}
		return dateSet;
	}

	private Date genDate(String localType, String data){
		Date date = null;

		date = new Date();
		date.setLocalType(localType);
		
		date.setStandardDate(genDate(data));
		date.setValue(data);
		return date;
	}

	private PlaceDates genPlaceDates(SoggettoProduttoreDati dati){
		PlaceDates placeDates = null;
		
		placeDates = new PlaceDates();
		placeDates.getPlaceDate().add(genPlaceDate(dati.getSede(), "sede"));
		return placeDates;
	}

	private PlaceDate genPlaceDate(String place, String descriptionNote){
		PlaceDate placeDate = null;
		
		placeDate = new PlaceDate();
		placeDate.setPlace(place);
		placeDate.setDescriptiveNote(descriptionNote);
		return placeDate;
	}

	private DescriptiveEntries genDescriptiveEntries(SoggettoProduttoreDati dati){
		DescriptiveEntries descriptiveEntries = null;
		
		descriptiveEntries = new DescriptiveEntries();
		descriptiveEntries.getDescriptiveEntry().add(genDescriptiveEntry(dati.getTipoEnte(), "tipologia ente"));
		return descriptiveEntries;
	}

	private DescriptiveEntry genDescriptiveEntry(String term, String descriptiveNote){
		DescriptiveEntry descriptiveEntry = null;
		
		descriptiveEntry = new DescriptiveEntry();
		descriptiveEntry.setTerm(term);
		descriptiveEntry.setDescriptiveNote(descriptiveNote);
		return descriptiveEntry;
	}

	private BiogHist genBiogHist(String descrizione){
		BiogHist biogHist = null;

		biogHist = new BiogHist();
		biogHist.setAbstract(descrizione);
		return biogHist;
	}
	
	private Relations gnRelations(SoggettoProduttoreDati dati) {
		Relations relations = null;
		
		relations = new Relations();
		relations.getResourceRelation().add(genResourceRelation("creatorOf", dati.getComplessoArchivisticoId()));
		return relations;
	}

	private ResourceRelation genResourceRelation(String resourceRelationType, String relationEntry){
		ResourceRelation resourceRelation = null;

		resourceRelation = new ResourceRelation();
		resourceRelation.setResourceRelationType(resourceRelationType);
		resourceRelation.setRelationEntry(relationEntry);
		return resourceRelation;
	}
}
