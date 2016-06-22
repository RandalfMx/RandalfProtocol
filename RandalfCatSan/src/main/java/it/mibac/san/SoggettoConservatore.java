/**
 * 
 */
package it.mibac.san;

import javax.xml.datatype.DatatypeConfigurationException;

import it.mibac.san.cat_import.CatRecordBody;
import it.mibac.san.scons_san.Identifier;
import it.mibac.san.scons_san.Localizzazione;
import it.mibac.san.scons_san.ObjectFactory;
import it.mibac.san.scons_san.Scons;

/**
 * @author massi
 *
 */
public class SoggettoConservatore extends SanMaster<SoggettoConservatoreDati> {

	/**
	 * @throws DatatypeConfigurationException 
	 * 
	 */
	public SoggettoConservatore(String systemId, String name, String mail, String phone, String title, String myAbstract) throws DatatypeConfigurationException {
		super(systemId, name, mail, phone, title, myAbstract, "soggetto conservatore");
	}

	@Override
	protected CatRecordBody genCatRecordBody(SoggettoConservatoreDati dati) {
		CatRecordBody catRecordBody = null;
		Scons scons = null;
		ObjectFactory of = null;
		Identifier identifier = null;
		Localizzazione localizzazione = null;
		
		catRecordBody = new CatRecordBody();

		scons = new Scons();
		
		of = new ObjectFactory();
		
		scons.getContent().add(of.createFormaautorizzata(dati.getIstituto()));

		identifier = of.createIdentifier();
		identifier.setSistemaId(systemId);
		identifier.setHref(dati.getUrlId());
		identifier.setRecordId(dati.getId());
		scons.getContent().add(identifier);

		scons.getContent().add(of.createSconsTipologia(dati.getTipoSoggettoConservtore()));

		localizzazione =of.createLocalizzazione();
		localizzazione.setPaese(dati.getPaese());
		localizzazione.setProvincia(dati.getProvincia());
		localizzazione.setComune(dati.getComune());
		localizzazione.setCap(dati.getCap());
		localizzazione.setValue(dati.getIndirizzo());
		scons.getContent().add(localizzazione);

		scons.getContent().add(of.createSconsServizi(dati.getTelefonoFax()));

		scons.getContent().add(of.createSconsDescrizione(dati.getDescrizione()));

		if (dati.isServizioConsultazione()){
			scons.getContent().add(of.createSconsOrario(dati.getOrariApertura()));
	
			scons.getContent().add(of.createSconsAltroaccesso(""));
	
			scons.getContent().add(of.createSconsConsultazione(dati.isServizioConsultazione()));
		} else {
			scons.getContent().add(of.createSconsAltroaccesso(dati.getOrariApertura()));
	
			scons.getContent().add(of.createSconsConsultazione(dati.isServizioConsultazione()));
		}

		catRecordBody.getContent().add(scons);

		return catRecordBody;
	}
}
