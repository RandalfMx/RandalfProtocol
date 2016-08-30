/**
 * 
 */
package mx.randalf.solr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;

import mx.randalf.solr.exception.SolrException;

/**
 * @author massi
 *
 */
public abstract class SolrCoreServer<SC extends SolrClient> {

	protected SC server = null;

	/**
	 * 
	 */
	public SolrCoreServer() throws SolrException {
	}

	protected abstract SC open(String url) throws SolrException;

	public UpdateResponse optimize() throws SolrServerException, IOException{
		return server.optimize();
	}

	public UpdateResponse commit() throws SolrServerException, IOException{
		return server.commit();		
	}

	public UpdateResponse rollback() throws SolrServerException, IOException{
		return server.rollback();
	}

	public NamedList<?> request(UpdateRequest request) throws SolrServerException, IOException{
		return server.request(request); // Returns a backwards compatible condensed response.
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

	public String queryToXML(SolrParams query) throws SolrServerException, IOException{
		ByteArrayOutputStream baos = null;
		OutputStreamWriter osw = null;

		try {
			baos = new ByteArrayOutputStream();
			osw = new OutputStreamWriter(baos, Charset.forName("UTF-8"));
			queryToXML(query, osw);
		} catch (SolrServerException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (osw != null){
				osw.flush();
			}
			if (baos != null){
				baos.flush();
				baos.close();
			}
		}
		return baos.toString();
	}

	public abstract void queryToXML(SolrParams query, Writer writer) throws SolrServerException, IOException;

	public void close() throws IOException{
		server.close();
	}

	
}
