package mx.randalf.solr;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
//import org.apache.solr.client.solrj.impl.CloudSolrClient.Builder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;

import mx.randalf.solr.exception.SolrException;

public class SolrCoreCloudServer extends SolrCoreServer<CloudSolrClient>{

	/**
	 * Variabile utilizzata per loggare l'applicazione
	 */
	private Logger log = Logger.getLogger(SolrCoreCloudServer.class);

	/**
	 * Timeout di connessione in Millisecondi
	 */
	private int connectionTimeout = 60000;

	/**
	 * Timeout di connessione con il server zookeeper
	 */
	private int zkClientTimeout = 100000;

	private String collection = null;
	
	public SolrCoreCloudServer(int connectionTimeout, int zkClientTimeout, String collection, String url) throws SolrException{
		this.connectionTimeout = connectionTimeout;
		this.zkClientTimeout = zkClientTimeout;
		this.collection = collection;
		server = open(url);
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
	protected CloudSolrClient open(String url) throws SolrException {
		CloudSolrClient server = null;
//		Builder build  = null;

		try {
//			build = new Builder();
//			server = build.withZkChroot(url).build();
//			build.
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

	@Override
	public UpdateResponse addBean(Object obj) throws IOException, SolrServerException {
//		throw
//		SolrInputDocument doc = new SolrInputDocument();
//		obj.getClass().getEnumConstants()
//		doc.a
////		doc.
//		UpdateRequest request = new UpdateRequest();
//		request.add(doc);
//		request.setAction(ACTION.OPTIMIZE, false, false);
//
//		NamedList response = ((CloudSolrServer)server).request(request); // Returns a backwards compatible condensed response.
//		//To get more detailed response down cast to RouteResponse:
//		CloudSolrServer.RouteResponse rr = (CloudSolrServer.RouteResponse)response;
		return null;
	}

	@Override
	public void queryToXML(SolrParams query, Writer writer) throws SolrServerException, IOException {
		ResponseParser parserOri = null;
		QueryResponse qr = null; 
		SolrDocumentList sdl = null;
		Object[] o = null;

		try {
			parserOri = server.getParser();
			server.setParser(new XMLResponseParser());
			((SolrQuery)query).set("wt", "xml");

			qr = server.query(query);
			sdl = qr.getResults();

			o = new Object[sdl.size()];
			o = sdl.toArray();
			for (int i = 0; i < o.length; i++){
			   log.debug("\n"+o[i].toString());
			   writer.write(o[i].toString() + "\n");
			}
		} catch (SolrServerException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			if (writer != null){
		        writer.flush();
			}
			if (parserOri != null){
				server.setParser(parserOri);
			}
		}
    }

	@Override
	public void close() throws IOException {
	}

}
