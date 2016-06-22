/**
 * 
 */
package it.mibac.san;

import java.util.Vector;

/**
 * @author massi
 *
 */
public class ComplessiArchivisticiDati {

	private String tipologia;
	private String urlId;
	private String id;
	private String titolo;
	private String descrizione;
	private String estremi;
	private String consistenza;
	private Vector<String> soggettiProduttori;
	private String soggettoConservatoreId;
	private String processinfo;
	private String soggettoConservatore;

	/**
	 * 
	 */
	public ComplessiArchivisticiDati() {
	}

	public String getTipologia() {
		return tipologia;
	}

	public String getUrlId() {
		return urlId;
	}

	public String getId() {
		return id;
	}

	public String getTitolo() {
		int pos = 0;
		if (titolo != null && titolo.length()>900){
			System.out.println("titolo.length(): "+titolo.length());
			titolo = titolo.substring(0, 900);
			System.out.println("titolo.length(): "+titolo.length());
			pos = titolo.lastIndexOf(".");
			System.out.println("pos: "+pos);
			if (pos >-1){
				titolo = titolo.substring(0, pos+1);
			}
			System.out.println("titolo.length(): "+titolo.length());
		}
		return titolo;
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

	public String getEstremi() {
		return estremi;
	}

	public String getConsistenza() {
		return consistenza;
	}

	public Vector<String> getSoggettiProduttori() {
		return soggettiProduttori;
	}

	public String getSoggettoConservatoreId() {
		return soggettoConservatoreId;
	}

	public String getProcessinfo() {
		return processinfo;
	}

	/**
	 * @param tipologia the tipologia to set
	 */
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
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
	 * @param estremi the estremi to set
	 */
	public void setEstremi(String estremi) {
		this.estremi = estremi;
	}

	/**
	 * @param consistenza the consistenza to set
	 */
	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}

	/**
	 * @param soggettiProduttori the soggettiProduttori to set
	 */
	public void addSoggettoProduttore(String soggettoProduttore) {
		if (this.soggettiProduttori == null){
			this.soggettiProduttori = new Vector<String>();
		}
		this.soggettiProduttori.addElement(soggettoProduttore);
	}

	/**
	 * @param soggettoConservatoreId the soggettoConservatoreId to set
	 */
	public void setSoggettoConservatoreId(String soggettoConservatoreId) {
		this.soggettoConservatoreId = soggettoConservatoreId;
	}

	/**
	 * @param processinfo the processinfo to set
	 */
	public void setProcessinfo(String processinfo) {
		this.processinfo = processinfo;
	}

	public String getSoggettoConservatore() {
		return soggettoConservatore;
	}

	/**
	 * @param soggettoConservatore the soggettoConservatore to set
	 */
	public void setSoggettoConservatore(String soggettoConservatore) {
		this.soggettoConservatore = soggettoConservatore;
	}

}
