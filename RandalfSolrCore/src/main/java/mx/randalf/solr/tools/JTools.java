/**
 * 
 */
package mx.randalf.solr.tools;

import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.UUID;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocumentList;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.quartz.QuartzTools;
import mx.randalf.solr.FindDocument;
import mx.randalf.solr.Params;
import mx.randalf.solr.exception.SolrException;

/**
 * @author massi
 *
 */
public abstract class JTools implements Job {

	private static Logger log = Logger.getLogger(JTools.class);

	protected Params params = null;

	/**
	 * 
	 */
	public JTools() {
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public abstract void execute(JobExecutionContext context) throws JobExecutionException;

	protected void read(String key, String value){
		if (value != null && !value.trim().equals("")){
			params.add(key, value);
		}
	}

	protected void read(String key, String[] value){
		if (value != null && value.length>0){
			for(int x=0; x<value.length; x++){
				if (value[x]!= null && !value[x].trim().equals("")){
					params.add(key, value[x]);
				}
			}
		}
	}

	protected void read(String key, Vector<String> value){
		if (value != null && value.size()>0){
			for(int x=0; x<value.size(); x++){
				if (value.get(x)!= null && !value.get(x).trim().equals("")){
					params.add(key, value.get(x));
				}
			}
		}
	}

	protected String findId(JobExecutionContext context, String key, String value, String id) throws JobExecutionException{
		FindDocument find = null;
		String query = "";
		QueryResponse qr = null;
		SolrDocumentList response = null;
		String objectIdentifier = null;

		try {
			find = new FindDocument(Configuration.getValue("solr.URL"),
					Boolean.parseBoolean(Configuration
							.getValue("solr.Cloud")),
					Configuration
							.getValue("solr.collection"),
					Integer.parseInt(Configuration
							.getValue("solr.connectionTimeOut")),
					Integer.parseInt(Configuration
							.getValue("solr.clientTimeOut")));
			query = key+":\""+value+"\"";
			
			qr = find.find(query);
			if (qr.getResponse() != null &&
					qr.getResponse().get("response")!=null){
				response = (SolrDocumentList) qr.getResponse().get("response");
				if (response.getNumFound()>0){
					objectIdentifier = (String) response.get(0).get(id);
				}
			}
		} catch (NumberFormatException e) {
			log.error("["+QuartzTools.getName(context)+"] "+e.getMessage(),e);
			throw new JobExecutionException(e.getMessage(), e, false);
		} catch (SolrException e) {
			log.error("["+QuartzTools.getName(context)+"] "+e.getMessage(),e);
			throw new JobExecutionException(e.getMessage(), e, false);
		} catch (ConfigurationException e) {
			log.error("["+QuartzTools.getName(context)+"] "+e.getMessage(),e);
			throw new JobExecutionException(e.getMessage(), e, false);
		} catch (SolrServerException e) {
			log.error("["+QuartzTools.getName(context)+"] "+e.getMessage(),e);
			throw new JobExecutionException(e.getMessage(), e, false);
		} finally {
			try {
				if (find != null){
					find.close();
				}
			} catch (IOException e) {
				log.error("["+QuartzTools.getName(context)+"] "+e.getMessage(),e);
				throw new JobExecutionException(e.getMessage(), e, false);
			}
		}
		if (objectIdentifier == null){
			objectIdentifier = UUID.randomUUID().toString();
		}
		return objectIdentifier;
	}

	public static JobKey start (JobExecutionContext context, 
			Class<? extends Job> myClass, String jobGroup, String jobName, 
			String triggerGroup, String triggerName, Hashtable<String, Object> params) 
					throws SchedulerException{
		JobKey jobKey = null;

		try {
			jobKey = QuartzTools.startJob(context.getScheduler(), myClass, 
					jobGroup,  jobName, triggerGroup, triggerName,
					params);
		} catch (SchedulerException e) {
			log.error("["+QuartzTools.getName(context)+"] "+e.getMessage(), e);
			throw e;
		}
		return jobKey;
	}

	public static String toXML(QueryResponse qr, int pos){
		String xml = null;
		
		xml = ClientUtils.toXML(ClientUtils.toSolrInputDocument(qr.getResults().get(pos)));
		return xml;
	}

	public static void writeXML(QueryResponse qr, int pos, Writer output) throws IOException{
		ClientUtils.writeXML(ClientUtils.toSolrInputDocument(qr.getResults().get(pos)), output);
	}
}
