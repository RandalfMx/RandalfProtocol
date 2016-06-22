/**
 * 
 */
package it.mibac.san;

/**
 * @author massi
 *
 */
public class SoggettoProduttoreDati {

	private String id;
	private String urlId;
	private String titolo;
	private String descrizione;
	private String dataIstituzione;
	private String dataSospensione;
	private String sede;
	private String tipoEnte;
	private String complessoArchivisticoId;

	/**
	 * 
	 */
	public SoggettoProduttoreDati() {
	}

	public String getId() {
		return id;
	}

	public String getUrlId() {
		return urlId;
	}

	public String getTitolo() {
		return titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getDataIstituzione() {
		return dataIstituzione;
	}

	public String getDataSoppressione() {
		return dataSospensione;
	}

	public String getSede() {
		return sede;
	}

	public String getTipoEnte() {
		return tipoEnte;
	}

	public String getComplessoArchivisticoId() {
		return complessoArchivisticoId;
	}

	/**
	 * @return the dataSospensione
	 */
	public String getDataSospensione() {
		return dataSospensione;
	}

	/**
	 * @param dataSospensione the dataSospensione to set
	 */
	public void setDataSospensione(String dataSospensione) {
		this.dataSospensione = dataSospensione;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param urlId the urlId to set
	 */
	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	/**
	 * @param titolo the titolo to set
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param dataIstituzione the dataIstituzione to set
	 */
	public void setDataIstituzione(String dataIstituzione) {
		this.dataIstituzione = dataIstituzione;
	}

	/**
	 * @param sede the sede to set
	 */
	public void setSede(String sede) {
		this.sede = sede;
	}

	/**
	 * @param tipoEnte the tipoEnte to set
	 */
	public void setTipoEnte(String tipoEnte) {
		this.tipoEnte = tipoEnte;
	}

	/**
	 * @param complessoArchivisticoId the complessoArchivisticoId to set
	 */
	public void setComplessoArchivisticoId(String complessoArchivisticoId) {
		this.complessoArchivisticoId = complessoArchivisticoId;
	}

}
