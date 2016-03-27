/**
 * 
 */
package mx.randalf.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import mx.randalf.solr.exception.SolrException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest.ACTION;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;

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
	private SolrClient server = null;

	private boolean cloud= false;

	/**
	 * Timeout di connessione in Millisecondi
	 */
	private int connectionTimeout = 60000;

	/**
	 * Socket Timeout
	 */
	private int soTimeout = 100000;

	/**
	 * Timeout di connessione con il server zookeeper
	 */
	private int zkClientTimeout = 100000;

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
		this.connectionTimeout = connectionTimeout;
		this.cloud = cloud;
		if (cloud) {
			this.zkClientTimeout = clientTimeout;
		} else {
			this.soTimeout = clientTimeout;
		}
		server = openConn(url, cloud, collection);
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
		server = openConn(url, cloud, collection);
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
	private SolrClient openConn(String url, boolean cloud, String collection) throws SolrException {
		SolrClient server;
		if (cloud) {
			server = getSolrCloudServer(url, collection);
		} else {
			server = getSolrServer(url);
		}
		id = UUID.randomUUID().toString();
//		System.out.println("Solr ID: "+id);
		return server;
	}

	/**
	 * 
	 * @param url
	 *            Url per la connessione con il Server ZooKeeper necessario per
	 *            ricavare la configurazione dei server cloud
	 * @return Connessione con il Server
	 * @throws SolrException
	 *             Gestione degli errori con il database Solr
	 */
	private SolrClient getSolrCloudServer(String url, String collection) throws SolrException {
		CloudSolrClient server = null;

		try {
			server = new CloudSolrClient(url);
			server.setDefaultCollection(collection);
			server.setZkClientTimeout(zkClientTimeout);
			server.setZkConnectTimeout(connectionTimeout);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SolrException(e.getMessage(), e);
		}
		return server;
	}

	/**
	 * Metodo utilizzato per aprire la connessione con il database
	 * 
	 * @param url
	 *            Url per il connessione con il Database Solr
	 * @return Connessione con il database
	 * @throws MalformedURLException
	 *             Errore relativo alla formattazione del URL
	 */
	private SolrClient getSolrServer(String url) {
		HttpSolrClient server = null;

		server = new HttpSolrClient(url);
		server.setSoTimeout(soTimeout); // socket read timeout
		server.setConnectionTimeout(connectionTimeout);
		server.setDefaultMaxConnectionsPerHost(100);
		server.setMaxTotalConnections(100);
		server.setFollowRedirects(false); // defaults to false
											// allowCompression defaults to
											// false.
											// Server side must support gzip or
											// deflate for this to have any
											// effect.
		server.setAllowCompression(true);
//MX		server.setMaxRetries(1); // defaults to 0. > 1 not recommended.
		// server.setParser(new XMLResponseParser()); // binary parser is used
		// by default return server;
		return server;
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
		log.info("Optmize ID: "+id);
		if (!lOptmize.contains(id)){
			log.debug("addOptmize ID: "+id);
			lOptmize.add(id);
		}
		try {
			while (true){
				log.debug("Optmize test posizione 0 : "+lOptmize.get(0));
				if (((String)lOptmize.get(0)).equals(id)){
					log.info("Optmize test ID: "+id+" TROVATO nella posizione 0");
					break;
				}
				log.debug("Optmize test ID: "+id+" non trovato");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
			}
			log.debug("Optmize ID: "+id+" in esecuzione");
			server.optimize();
			log.info("Optmize ID: "+id+" terminato");
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
		log.info("Commit ID: "+id);
		if (!lCommit.contains(id)){
			log.debug("addCommit ID: "+id);
			lCommit.add(id);
		}
		try {
			while (true){
				log.debug("Commit test posizione 0 : "+lCommit.get(0));
				if (((String)lCommit.get(0)).equals(id)){
					log.info("Commit test ID: "+id+" TROVATO nella posizione 0");
					break;
				}
				log.debug("Commit test ID: "+id+" non trovato");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
			}
			log.debug("Commit ID: "+id+" in esecuzione");
			server.commit();
			log.info("Commit ID: "+id+" terminato");
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

		log.info("Add ID: "+id);
		if (!lAdd.contains(id)){
			log.debug("addAdd ID: "+id);
			lAdd.add(id);
		}
		try {
			while (true){
				log.debug("Add test posizione 0 : "+lAdd.get(0));
				if (((String)lAdd.get(0)).equals(id)){
					log.info("Add test ID: "+id+" TROVATO nella posizione 0");
					break;
				}
				log.debug("Add test ID: "+id+" non trovato");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
			}
			log.debug("info ID: "+id+" in esecuzione");
			log.debug("add(UpdateRequest request) - "+request.getDocuments().size());
			request.setAction(ACTION.OPTIMIZE, false, false);
			if (cloud){
				response = ((CloudSolrClient)server).request(request); // Returns a backwards compatible condensed response.
			} else {
				response = ((SolrClient)server).request(request); // Returns a backwards compatible condensed response.
			}
			log.debug("add(UpdateRequest request) FINE ");
			for (int x=0; x<response.size(); x++){
				if (((Integer)((NamedList<?>)response.get(response.getName(x))).get("status"))!=0){
					throw new SolrServerException("Riscontrato un problema durante l'aggiornamento");
				}
			}
			log.info("Add ID: "+id+" terminato");
		} catch (SolrServerException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			lAdd.remove(id);
		}
	}

	public UpdateResponse addBean(Object obj) throws IOException, SolrServerException{
		if (cloud){
//			throw
//			SolrInputDocument doc = new SolrInputDocument();
//			obj.getClass().getEnumConstants()
//			doc.a
////			doc.
//			UpdateRequest request = new UpdateRequest();
//			request.add(doc);
//			request.setAction(ACTION.OPTIMIZE, false, false);
//
//			NamedList response = ((CloudSolrServer)server).request(request); // Returns a backwards compatible condensed response.
//			//To get more detailed response down cast to RouteResponse:
//			CloudSolrServer.RouteResponse rr = (CloudSolrServer.RouteResponse)response;
			return null;
		} else {
			return ((SolrClient)server).addBean(obj);
		}
	}

	public UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException{
		if (cloud){
			return ((CloudSolrClient)server).deleteByQuery(query);
		} else {
			return ((SolrClient)server).deleteByQuery(query);
		}
	}

	public QueryResponse query(SolrParams query) throws SolrServerException, IOException{
		if (cloud){
			return ((CloudSolrClient)server).query(query);
		} else {
			return ((SolrClient)server).query(query);
		}
	}

	public void close() throws IOException {
		try {
			if (cloud){
				((CloudSolrClient)server).close();
//			((CloudSolrServer)server).getZkStateReader().getZkClient().close();
//			((CloudSolrServer)server).getZkStateReader().close();
			} else {
				((SolrClient)server).close();
			}
		} catch (IOException e) {
			throw e;
		}
	}
}
