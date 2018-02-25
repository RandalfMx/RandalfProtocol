/**
 * 
 */
package mx.randalf.solr;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest.ACTION;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;

import mx.randalf.solr.exception.SolrException;

/**
 * Classe utilizzata per la gestione del materiale in comune per l'accesso a
 * solr
 * 
 * @author massi
 * 
 */
class SolrCore {

	/**
	 * Variabile utilizzata per indicare l'URL del server solr
	 */
	private SolrCoreServer<?> server = null;

//	private boolean cloud= false;

	/**
	 * Variabile utilizzata per loggare l'applicazione
	 */
	private Logger log = Logger.getLogger(SolrCore.class);

	private String id= null;

	public static List<String> lAdd = new Vector<String>();

	public static List<String> lCommit = new Vector<String>();

	public static List<String> lOptmize = new Vector<String>();
	
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
	public SolrCore(String url, boolean cloud, String collection, int connectionTimeout,
			int clientTimeout) throws SolrException {
		
		
		if (cloud){
			server = new SolrCoreCloudServer(connectionTimeout, clientTimeout, collection, url);
		} else {
			server = new SolrCoreStandardServer(connectionTimeout, clientTimeout, url);
		}
		id = UUID.randomUUID().toString();
	}

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
	public SolrCore(String url, boolean cloud, String collection) throws SolrException {
		this(url, cloud, collection, 60000, 100000);
	}

	/**
	 * Metodo utilizzato per eseguire l'ottimizzazione del database
	 * 
	 * @throws SolrServerException
	 *             Errore relativo alla connessione con il Database Solr
	 * @throws IOException
	 *             Errore IO
	 */
	public synchronized void optimize() throws SolrServerException, IOException 
	{
		log.info("\n"+"Optmize ID: "+id);
		if (!lOptmize.contains(id)){
			log.debug("\n"+"addOptmize ID: "+id);
			lOptmize.add(id);
		}
		try {
			while (true){
				log.debug("\n"+"Optmize test posizione 0 : "+lOptmize.get(0));
				if (((String)lOptmize.get(0)).equals(id)){
					log.info("\n"+"Optmize test ID: "+id+" TROVATO nella posizione 0");
					break;
				}
				log.debug("\n"+"Optmize test ID: "+id+" non trovato");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
			}
			log.debug("\n"+"Optmize ID: "+id+" in esecuzione");
			server.optimize();
			log.info("\n"+"Optmize ID: "+id+" terminato");
		} catch (SolrServerException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			lOptmize.remove(id);
		}
	}

	/**
	 * Metodo utilizzato per eseguire l'ottimizzazione del database
	 * 
	 * @throws SolrServerException
	 *             Errore relativo alla connessione con il Database Solr
	 * @throws IOException
	 *             Errore IO
	 */
	public synchronized void commit() throws SolrServerException, IOException {
		log.info("\n"+"Commit ID: "+id);
		if (!lCommit.contains(id)){
			log.debug("\n"+"addCommit ID: "+id);
			lCommit.add(id);
		}
		try {
			while (true){
				log.debug("\n"+"Commit test posizione 0 : "+lCommit.get(0));
				if (((String)lCommit.get(0)).equals(id)){
					log.info("\n"+"Commit test ID: "+id+" TROVATO nella posizione 0");
					break;
				}
				log.debug("\n"+"Commit test ID: "+id+" non trovato");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
			}
			log.debug("\n"+"Commit ID: "+id+" in esecuzione");
			server.commit();
			log.info("\n"+"Commit ID: "+id+" terminato");
		} catch (SolrServerException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			lCommit.remove(id);
		}
	}

	/**
	 * Metodo utilizzato per eseguire l'ottimizzazione del database
	 * 
	 * @throws SolrServerException
	 *             Errore relativo alla connessione con il Database Solr
	 * @throws IOException
	 *             Errore IO
	 */
	public void rollback() throws SolrServerException, IOException {
		server.rollback();
	}

	public void add(UpdateRequest request) throws SolrServerException, IOException{
		NamedList<?> response = null;

		log.info("\n"+"Add ID: "+id);
		if (!lAdd.contains(id)){
			log.debug("\n"+"addAdd ID: "+id);
			lAdd.add(id);
		}
		try {
			while (true){
				log.debug("\n"+"Add test posizione 0 : "+lAdd.get(0));
				if (((String)lAdd.get(0)).equals(id)){
					log.info("\n"+"Add test ID: "+id+" TROVATO nella posizione 0");
					break;
				}
				log.debug("\n"+"Add test ID: "+id+" non trovato");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
			}
			log.debug("\n"+"info ID: "+id+" in esecuzione");
			log.debug("\n"+"add(UpdateRequest request) - "+request.getDocuments().size());
			request.setAction(ACTION.OPTIMIZE, false, false);
			response = server.request(request); // Returns a backwards compatible condensed response.
			log.debug("\n"+"add(UpdateRequest request) FINE ");
			for (int x=0; x<response.size(); x++){
				if (((Integer)((NamedList<?>)response.get(response.getName(x))).get("status"))!=0){
					throw new SolrServerException("Riscontrato un problema durante l'aggiornamento");
				}
			}
			log.info("\n"+"Add ID: "+id+" terminato");
		} catch (SolrServerException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			lAdd.remove(id);
		}
	}

	public UpdateResponse addBean(Object obj) throws IOException, SolrServerException{
		return server.addBean(obj);
	}

	public UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException{
		return server.deleteByQuery(query);
	}

	public QueryResponse query(SolrParams query) throws SolrServerException, IOException{
		return server.query(query);
	}

	public void close() throws IOException {
		server.close();
	}
}
