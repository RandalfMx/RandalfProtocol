/**
 * 
 */
package it.mibac.san;

import java.math.BigInteger;

/**
 * @author massi
 *
 */
public class SoggettoConservatoreDati {

	private String istituto;
	private String urlId;
	private String id;
	private String tipoSoggettoConservatore;
	private String paese;
	private String provincia;
	private String comune;
	private BigInteger cap;
	private String indirizzo;
	private String telefonoFax;
	private String descrizione;
	private String orarioApertura;
	private Boolean servizioConsultazione;

	/**
	 * 
	 */
	public SoggettoConservatoreDati() {
	}

	public String getIstituto() {
		return istituto;
	}

	public String getUrlId() {
		return urlId;
	}

	public String getId() {
		return id;
	}

	public String getTipoSoggettoConservtore() {
		return tipoSoggettoConservatore;
	}

	public String getPaese() {
		return paese;
	}

	public String getProvincia() {
		return provincia;
	}

	public String getComune() {
		return comune;
	}

	public BigInteger getCap() {
		return cap;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getTelefonoFax() {
		return telefonoFax;
	}

	public String getDescrizione() {
		int pos = 0;
		if (descrizione != null && descrizione.length()>900){
			System.out.println("descrizione.length(): "+descrizione.length());
			descrizione = descrizione.substring(0, 900);
			System.out.println("descrizione.length(): "+descrizione.length());
			pos = descrizione.lastIndexOf(".");
			System.out.println("pos: "+pos);
			if (pos >-1){
				descrizione = descrizione.substring(0, pos+1);
			}
			System.out.println("descrizione.length(): "+descrizione.length());
		}
		return descrizione;
	}

	public String getOrariApertura() {
		return orarioApertura;
	}

	public Boolean isServizioConsultazione() {
		return servizioConsultazione;
	}

	/**
	 * @return the tipoSoggettoConservatore
	 */
	public String getTipoSoggettoConservatore() {
		return tipoSoggettoConservatore;
	}

	/**
	 * @param tipoSoggettoConservatore the tipoSoggettoConservatore to set
	 */
	public void setTipoSoggettoConservatore(String tipoSoggettoConservatore) {
		this.tipoSoggettoConservatore = tipoSoggettoConservatore;
	}

	/**
	 * @return the orarioApertura
	 */
	public String getOrarioApertura() {
		return orarioApertura;
	}

	/**
	 * @param orarioApertura the orarioApertura to set
	 */
	public void setOrarioApertura(String orarioApertura) {
		this.orarioApertura = orarioApertura;
	}

	/**
	 * @param istituto the istituto to set
	 */
	public void setIstituto(String istituto) {
		this.istituto = istituto;
	}

	/**
	 * @param urlId the urlId to set
	 */
	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param paese the paese to set
	 */
	public void setPaese(String paese) {
		this.paese = paese;
	}

	/**
	 * @param provincia the provincia to set
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 * @param comune the comune to set
	 */
	public void setComune(String comune) {
		this.comune = comune;
	}

	/**
	 * @param cap the cap to set
	 */
	public void setCap(BigInteger cap) {
		this.cap = cap;
	}

	/**
	 * @param indirizzo the indirizzo to set
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * @param telefonoFax the telefonoFax to set
	 */
	public void setTelefonoFax(String telefonoFax) {
		this.telefonoFax = telefonoFax;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param servizioConsultazione the servizioConsultazione to set
	 */
	public void setServizioConsultazione(Boolean servizioConsultazione) {
		this.servizioConsultazione = servizioConsultazione;
	}

}
