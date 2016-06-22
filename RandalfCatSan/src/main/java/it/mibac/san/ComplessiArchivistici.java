/**
 * 
 */
package it.mibac.san;

import javax.xml.datatype.DatatypeConfigurationException;

import it.mibac.san.cat_import.CatRecordBody;
import it.mibac.san.ead_san.Archdesc;
import it.mibac.san.ead_san.Datetype;
import it.mibac.san.ead_san.Did;
import it.mibac.san.ead_san.Ead;
import it.mibac.san.ead_san.Physdesc;
import it.mibac.san.ead_san.Relatedmaterial;
import it.mibac.san.ead_san.Repository;
import it.mibac.san.ead_san.Unitid;
import it.mibac.san.ead_san.Unittitle;

/**
 * @author massi
 *
 */
public class ComplessiArchivistici extends SanMaster<ComplessiArchivisticiDati> {

	/**
	 * @throws DatatypeConfigurationException 
	 * 
	 */
	public ComplessiArchivistici(String systemId, String name, String mail, String phone, String title, String myAbstract) throws DatatypeConfigurationException {
		super(systemId, name, mail, phone, title, myAbstract, "complesso archivistico");
	}

	@Override
	protected CatRecordBody genCatRecordBody(ComplessiArchivisticiDati dati) {
		CatRecordBody catRecordBody = null;
		Ead ead = null;
		Archdesc archdesc = null;
		Did did = null;
		Unitid unitid = null;
		Unittitle unittitle = null;
		Datetype unitdate = null;
		Physdesc physdesc = null;
		Repository repository = null;
		Relatedmaterial relatedmaterial = null;
		
		catRecordBody = new CatRecordBody();

		ead = new Ead();

		archdesc = new Archdesc();
		archdesc.setLevel("otherlevel");
		archdesc.setOtherlevel(dati.getTipologia());

		did = new Did();

		unitid = new Unitid();
		unitid.setType(systemId);
		unitid.setIdentifier(dati.getUrlId());
		unitid.setValue(dati.getId());
		did.setUnitid(unitid);

		unittitle = new Unittitle();
		unittitle.setType("principale");
		unittitle.setValue(dati.getTitolo());
		did.getUnittitle().add(unittitle);

		if (dati.getDescrizione() != null){
			unittitle = new Unittitle();
			unittitle.setType("descrizione");
			unittitle.setValue(dati.getDescrizione());
			did.getUnittitle().add(unittitle);
		}


		if (dati.getEstremi() == null ||
				dati.getEstremi().startsWith("manoscritti")){
			unitdate = new Datetype();
			unitdate.setDatechar("non indicata");
			unitdate.setNormal("00000000/00000000");
			unitdate.setValue("non indicata");
		} else {
			unitdate = new Datetype();
			unitdate.setDatechar("principale");
			unitdate.setNormal(genDate(dati.getEstremi()));
			unitdate.setValue(dati.getEstremi());
		}
		did.getUnitdate().add(unitdate);

		physdesc = new Physdesc();
		if (dati.getConsistenza()!=null && !dati.getConsistenza().trim().equals("")){
			physdesc.setExtent(dati.getConsistenza());
		} else {
			physdesc.setExtent("n. d.");
		}
		did.setPhysdesc(physdesc);
		
		if (dati.getSoggettiProduttori() != null){
			for(int x=0; x<dati.getSoggettiProduttori().size(); x++){
				did.getOrigination().add(dati.getSoggettiProduttori().get(x));
			}
		}
		repository = new Repository();
		repository.setId(dati.getSoggettoConservatoreId());
		repository.setLabel(systemId);
		repository.setValue(dati.getSoggettoConservatore());
		did.getRepository().add(repository);
		
		archdesc.setDid(did);

		archdesc.setProcessinfo(dati.getProcessinfo());
		
		relatedmaterial = new Relatedmaterial();
		relatedmaterial.setArchref(dati.getId());
		archdesc.setRelatedmaterial(relatedmaterial);
		ead.setArchdesc(archdesc);

		catRecordBody.getContent().add(ead);
		return catRecordBody;
	}	
	
}
