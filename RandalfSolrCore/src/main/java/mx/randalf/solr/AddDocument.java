/**
 * 
 */
package mx.randalf.solr;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import mx.randalf.solr.exception.SolrException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest.ACTION;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

/**
 * Classe utilizzata per aggiungere/modificare un oggetto al database Solr
 * 
 * @author massi
 *
 */
public abstract class AddDocument extends FindDocument {

	private Logger log = Logger.getLogger(AddDocument.class);

	protected UpdateRequest request;

	/**
	 * Costruttore
	 * 
	 * @param url
	 *            Url per il connessione con il Database Solr o Server ZooKeeper
	 * @param cloud
	 *            Indica se la connessione viene fatta verso un CloudServer o i
	 *            Solr standard
	 * @throws SolrException
	 *             Gestione degli errori con il database Solr
	 */
	public AddDocument(String url, boolean cloud, String collection) throws SolrException {
		super(url, cloud, collection);
	}

	/**
	 * Costruttore
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
	public AddDocument(String url, boolean cloud, String collection, int connectionTimeout,
			int clientTimeout) throws SolrException {
		super(url, cloud, collection, connectionTimeout, clientTimeout);
	}

	/**
	 * Metodo per aggiungere/modificare un oggetto sul database Solr
	 * 
	 * @param params
	 * @throws SolrException 
	 */
	@SuppressWarnings("static-access")
	public Hashtable<String, String> add(Hashtable<String, List<Object>> params, 
			Item items) throws SolrException{
		SolrInputDocument item = null;
		String query = "";
		QueryResponse response = null;
		String id = null;

		try {
			id = (String) params.get(items.ID).get(0);
			query = items.ID+":\"" + id+"\"";
			response = find(query);
			if (response.getResults().getNumFound() == 1) {
				if (request == null){
					request = new UpdateRequest();
					request.setAction(ACTION.OPTIMIZE, false, false);
				}
//				request.deleteById(id, (Long) response.getResults().get(0).getFieldValue("_version_"));
				//Query(query);
			} else if (response.getResults().getNumFound() > 1) {
				throw new SolrException(
						"Riscontrato più di un record Padre per la query ["
								+ query + "] verificare");
			}
			item = initItem(id, params, items);

			if (request == null){
				request = new UpdateRequest();
				request.setAction(ACTION.OPTIMIZE, false, false);
			}
			request.add(item);
		} catch (SolrServerException e) {
			log.error(e.getMessage(), e);
			throw new SolrException("Solr Server: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SolrException("Solr Server: " + e.getMessage(), e);
		}
		return null;
	}

	private SolrInputDocument initItem(String id,
			Hashtable<String, List<Object>> params,
			Item items) {
		SolrInputDocument item = null;
//		Vector<Object> objs = null;
//		Object obj = null;
		Enumeration<String> keys =null;
		String key = null;

		item = new SolrInputDocument();
	
		keys = params.keys();

		while(keys.hasMoreElements()){
			key = keys.nextElement();
			items.add(item, key, (Vector<Object>) params.get(key));
		}
		return item;
	}

	/**
	 * Metodo utilizzato per cancellare uno o pi?? record basandosi su una query di ricerca
	 * 
	 * @param query Query di ricerca
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public boolean delete(String query) throws SolrServerException, IOException{
		UpdateResponse response = null;
		
		response = deleteByQuery(query);
		return (response.getStatus()==200);
	}

	@Override
	public void commit() throws SolrServerException, IOException {
		log.debug("\n"+"Commit");
		if (request!= null){
			log.debug("\n"+"Commit - add");
			add(request);
			log.debug("\n"+"Commit - add - Fine");
		}
		log.debug("\n"+"Commit - Commit");
		super.commit();
		log.debug("\n"+"Commit - Commit - Fine");
	}
}
