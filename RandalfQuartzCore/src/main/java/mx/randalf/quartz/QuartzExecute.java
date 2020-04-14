/**
 * 
 */
package mx.randalf.quartz;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public abstract class QuartzExecute{

	/**
	 * Variabile utilizzata per loggare l'applicazione
	 */
	private static Logger log = LogManager.getLogger(QuartzExecute.class);

	/**
	 * Variabile utilizzata per la gestione della schedurlazione delle attività
	 */
	protected Scheduler scheduler = null;

	protected Hashtable<String, JobKey> jList = null;

	/**
	 * @throws ConfigurationException 
	 * @throws SchedulerException 
	 * 
	 */
	public QuartzExecute(String fileProp) throws ConfigurationException, SchedulerException {
		File f = null;

		try {
			f = new File(fileProp);
			Configuration.init((f.getParentFile()==null?"./":f.getParentFile().getAbsolutePath()));
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();

			jList = new Hashtable<String, JobKey>();
		} catch (ConfigurationException e) {
			throw e;
		} catch (SchedulerException e) {
			throw e;
		}
	}

	public void close(){
		try {
			scheduler.shutdown(true);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	public boolean checkExecute(){
		Enumeration<String> keys = null;
		String key = null;
		boolean result = false;

		if (scheduler==null){
			result = true;
		} else {
			keys = jList.keys();
			while(keys.hasMoreElements()){
				try {
					key = keys.nextElement();
					if (!(jList.get(key) == null
						|| !scheduler.checkExists(jList.get(key)))) {
						result = true;
						break;
					}
				} catch (SchedulerException e) {
					log.error(e.getMessage(), e);
					result = true;
					break;
				}
			}
		}
		return result;
	}

	protected JobKey start(Class<? extends JobExecute> jClass, 
			String jobGroup, String jobName, String triggerGroup, String triggerName, Hashtable<String, Object> params) 
			throws SchedulerException{
		JobKey jobKey = null;

		try {
			jobKey = QuartzTools.startJob(scheduler, jClass, jobGroup, jobName,
					triggerGroup, triggerName, params);
		} catch (SchedulerException e) {
			throw e;
		}
		return jobKey;
	}

}
