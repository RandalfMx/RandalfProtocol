/**
 * 
 */
package mx.randalf.solr;


import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import mx.randalf.solr.exception.SolrException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;

/**
 * Classe utilizzata per gestire la ricerca dei documenti nell'archivio Solr
 * 
 * @author massi
 *
 */
public class FindDocument extends SolrCore {

	/**
	 * Questa variabile viene utilizzata per loggare l'applicazione
	 */
	private Logger log = Logger.getLogger(FindDocument.class);

	/**
	 * Variabile utilizzata per indicare se utilizzare la gestione delle faccette
	 */
	private boolean facet=false;

	/**
	 * Variabile utilizzata per indicare il numero minimo di facette
	 */
	private int facetMinCount=0;

	/**
	 * Variabile utilizzata per indicare il numero massimo di faccette
	 */
	private int facetLimit=0;

	/**
	 * Variabile utilizzata per indicare i campi da utilizzare per le facette
	 */
	private String[] facetField=null;

	private String[] facetPivot=null;
	
	private String facetSort= FacetParams.FACET_SORT_COUNT;
	
	private TreeMap<String, String[]> queryFacet = null;

	private SolrQuery solrQuery = null;

	/**
	 * Costrutore
	 * 
	 * @param url
	 *            Url per il connessione con il Database Solr o Server ZooKeeper
	 * @param cloud
	 *            Indica se la connessione viene fatta verso un CloudServer o i
	 *            Solr standard
	 * @throws SolrException
	 *             Gestione degli errori con il database Solr
	 */
	public FindDocument(String url, boolean cloud, String collection, String optional) throws SolrException {
		super(url, cloud, collection, optional);
	}

	/**
	 * Costrutore
	 * 
	 * @param url
	 *            Url per il connessione con il Database Solr o Server ZooKeeper
	 * @param cloud
	 *            Indica se la connessione viene fatta verso un CloudServer o i
	 *            Solr standard
	 * @param connectionTimeout
	 *            Indica il tempo in Milliseconti di timeout di connessione
	 * @param clientTimeout
	 *            Indica il timeout del socket nel caso di una connessione
	 *            SolrStandard altrimenti il timeout verso il ZooKeeper Server
	 * @throws SolrException
	 *             Gestione degli errori con il database Solr
	 */
	public FindDocument(String url, boolean cloud, String collection, int connectionTimeout,
			int clientTimeout, String optional) throws SolrException {
		super(url, cloud, collection, connectionTimeout, clientTimeout, optional);
	}

	/**
	 * Metodo utilizzato per eseguire la ricerca di tutti i Risultati
	 * 
	 * @return Risultato della ricerca
	 * @throws SolrServerException Errore riscontrato durante la ricerca sul Database
	 */
	public QueryResponse findAll() throws SolrServerException{
		return find("*:*");
	}

	/**
	 * Metodo utilizzato per eseguire la ricerca di tutti i Risultati
	 * 
	 * @return Risultato della ricerca
	 * @throws SolrServerException Errore riscontrato durante la ricerca sul Database
	 */
	public QueryResponse findAll(Vector<String> cols) throws SolrServerException{
		return find("*:*", cols);
	}

	/**
	 * Metodo utilizzato per eseguire la ricerca sul database solr
	 * 
	 * @param query Questi per la ricerca
	 * @return Risultato della ricerca
	 * @throws SolrServerException Errore riscontrato durante la ricerca sul Database
	 */
	public QueryResponse find(String query) throws SolrServerException{
		return find(query, 0, 0);
	}

	/**
	 * Metodo utilizzato per eseguire la ricerca sul database solr
	 * 
	 * @param query Questi per la ricerca
	 * @return Risultato della ricerca
	 * @throws SolrServerException Errore riscontrato durante la ricerca sul Database
	 */
	public QueryResponse find(String query, Vector<String> cols) throws SolrServerException{
		return find(query, 0, 0, cols, null);
	}

	/**
	 * Metodo utilizzato per eseguire la ricerca sul database solr
	 * 
	 * @param query Questi per la ricerca
	 * @return Risultato della ricerca
	 * @throws SolrServerException Errore riscontrato durante la ricerca sul Database
	 */
	public QueryResponse find(String query, Vector<String> cols, Vector<SortClause> sort) throws SolrServerException{
		return find(query, 0, 0, cols, sort);
	}
	
	/**
	 * Metodo utilizzato per eseguire la ricerca sul database solr
	 * 
	 * @param query Questi per la ricerca
	 * @param start Indica il numero della record di partenza
	 * @param rows Indica il numero di record massimo da estrarre
	 * @return Risultato della ricerca
	 * @throws SolrServerException Errore riscontrato durante la ricerca sul Database
	 */
	public QueryResponse find(String query, int start, int rows) throws SolrServerException{
		return find(query, start, rows, null, null);
	}
	
	/**
	 * Metodo utilizzato per eseguire la ricerca sul database solr
	 * 
	 * @param query Questi per la ricerca
	 * @param start Indica il numero della record di partenza
	 * @param rows Indica il numero di record massimo da estrarre
	 * @param cols Lista delle colonne da visualizzare
	 * @return Risultato della ricerca
	 * @throws SolrServerException Errore riscontrato durante la ricerca sul Database
	 */
	public QueryResponse find(String query, int start, int rows, Vector<String> cols, Vector<SortClause> sort) throws SolrServerException{
		QueryResponse response=null;
		String[] values = null;
		
		try {
			solrQuery = new SolrQuery();
			if (start>0){
				solrQuery.setStart(start);
			}
			if (rows>0 || rows==-1){
				solrQuery.setRows(rows);
			}
			if (cols != null){
				for ( int x=0; x<cols.size(); x++){
					solrQuery.addField(cols.get(x));
				}
			}
			if (facet){
				solrQuery.setFacet(facet);
				solrQuery.setFacetSort(facetSort);
				solrQuery.setFacetMinCount(facetMinCount);
				solrQuery.setFacetLimit(facetLimit);
				if (facetField != null){
					for (int x=0; x<facetField.length; x++){
						solrQuery.addFacetField(facetField[x]);
					}
				}
				if (facetPivot != null){
					for (int x=0; x<facetPivot.length; x++){
						solrQuery.addFacetPivotField(facetPivot[x]);
					}
				}
				if (queryFacet!= null){
					Iterator<String> keys = queryFacet.keySet().iterator();
				    while (keys.hasNext()){
				    	String key = (String) keys.next();
				    	values =queryFacet.get(key);
				    	for (int x=0; x<values.length;x++){
				    		query += " +"+key+":\""+values[x].replace("\"", "\\\"")+"\"";
				    	}
				    }
				}
			}
			
			if (sort != null){
				for (int x=0; x<sort.size(); x++){
					solrQuery.addSort(sort.get(x));
				}
			}
			solrQuery.setQuery(query);
			
			response = query(solrQuery);
		} catch (SolrServerException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SolrServerException(e.getMessage(), e);
		}
		return response;
	}

	public void enableFacet(int facetMinCount, int facetLimit, String[] facetField, TreeMap<String, String[]> queryFacet){
		enableFacet(facetMinCount, facetLimit, facetField);
		this.queryFacet = queryFacet;
	}

	/**
	 * Abilita la gestione delle faccette
	 * 
	 * @param facetMinCount Variabile utilizzata per indicare il numero minimo di facette
	 * @param facetLimit Variabile utilizzata per indicare il numero massimo di faccette
	 * @param facetField Variabile utilizzata per indicare i campi da utilizzare per le facette
	 */
	public void enableFacet(int facetMinCount, int facetLimit, String[] facetField){
		this.facet = true;
		this.facetMinCount=facetMinCount;
		this.facetLimit=facetLimit;
		this.facetField=facetField;
		this.queryFacet = null;
	}

	public void enableFacetPivot(int facetMinCount, int facetLimit, String facetSort, String[] facetPivot){
		enableFacet(facetMinCount, facetLimit, null);
		this.facetPivot = facetPivot;
		this.facetSort = facetSort;
	}

	/**
	 * Disabilita la gestione delle Facette
	 */
	public void disableFacet(){
		facet = false;
		facetMinCount=0;
		facetLimit=0;
		facetField=null;
		queryFacet=null;
	}

	/**
	 * @return the solrQuery
	 */
	public SolrQuery getSolrQuery() {
		return solrQuery;
	}
}
