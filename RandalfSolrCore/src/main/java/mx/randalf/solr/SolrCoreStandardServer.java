package mx.randalf.solr;

import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
//import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;

import mx.randalf.solr.exception.SolrException;

public class SolrCoreStandardServer extends SolrCoreServer<HttpSolrClient>{

	private Logger log = Logger.getLogger(SolrCoreStandardServer.class);

	/**
	 * Timeout di connessione in Millisecondi
	 */
	private int connectionTimeout = 60000;

	/**
	 * Socket Timeout
	 */
	private int soTimeout = 100000;

	public SolrCoreStandardServer(int connectionTimeout, int soTimeout, String url) throws SolrException{
		this.connectionTimeout = connectionTimeout;
		this.soTimeout = soTimeout;
		server = open(url);
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
	protected HttpSolrClient open(String url) throws SolrException {
		HttpSolrClient server = null;
//		Builder builder = null;

//		builder = new Builder(url);
//		server = builder.build();
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

}
