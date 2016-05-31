/**
 * 
 */
package mx.randalf.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * @author massi
 *
 */
public class QuartzTools {

	private static Logger log = Logger.getLogger(QuartzTools.class);

	/**
	 * 
	 */
	public QuartzTools() {
	}

	public static JobKey startJob(Scheduler scheduler, Class<? extends Job> jClass, 
			String jobGroup, String jobName, String triggerGroup, String triggerName, Hashtable<String, Object> params) 
			throws SchedulerException{
		return startJob(scheduler, jClass, jobGroup, jobName, triggerGroup, triggerName, params, null);
	}

	public static JobKey startJob(Scheduler scheduler, Class<? extends Job> jClass, 
			String jobGroup, String jobName, String triggerGroup, String triggerName, 
			Hashtable<String, Object> params,
			ScheduleBuilder<?> schedBuilder) 
			throws SchedulerException{
		JobDetail job = null;
		Trigger trigger = null;
		Set<Trigger> triggers = null;
		Enumeration<String> keys = null;
		String key = null;

		try {
			job = newJob(jClass).
					withIdentity(jobName, jobGroup)
					
					.build();

			if (params != null){
				keys = params.keys();
				while (keys.hasMoreElements()){
					key = keys.nextElement();
					job.getJobDataMap().put(key, params.get(key));
				}
			}

			if (schedBuilder != null){
				trigger = newTrigger()
						.withIdentity(triggerName, triggerGroup)
						.withSchedule(schedBuilder)
						.startNow()
						.build();
			} else {
				trigger = newTrigger()
						.withIdentity(triggerName, triggerGroup)
						.startNow()
						.build();
				
			}
			triggers = new HashSet<Trigger>();
			triggers.add(trigger);
			scheduler.scheduleJob(job, triggers, false);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SchedulerException(e.getMessage(),e);
		}
		return job.getKey();
	}

	public static boolean checkJob(JobExecutionContext context) throws SchedulerException{
		List<JobExecutionContext> jobs = null;
		int conta = 0;
		boolean result = false;

		try {
			if (context.getScheduler().isShutdown()){
				result = false;
			} else if (context.getScheduler().isStarted()){
				jobs = context.getScheduler().getCurrentlyExecutingJobs();
				if (jobs != null && jobs.size()>0){
					for(JobExecutionContext job : jobs){
						if (job.getJobDetail().getKey().getGroup().equals(context.getJobDetail().getKey().getGroup()) &&
								job.getJobDetail().getKey().getName().equals(context.getJobDetail().getKey().getName()) &&
								job.getTrigger().getKey().getGroup().equals(context.getTrigger().getKey().getGroup()) &&
								job.getTrigger().getKey().getName().equals(context.getTrigger().getKey().getName()) ){
							conta++;
						}
					}
				}
				result = conta<=1;
			}
		} catch (SchedulerException e) {
			throw e;
		}
		return result;
	}

	public static String getName(JobExecutionContext context) {
		return "Detail: "+
					context.getJobDetail().getKey().getGroup() +
					"."  +
					context.getJobDetail().getKey().getName() +
					" Trigger: " +
					context.getTrigger().getKey().getGroup() +
					"." +
					context.getTrigger().getKey().getName();
	}

	public static Vector<JobKey> checkJobs(JobExecutionContext context, Vector<JobKey> listJobs, Integer nThread, Integer tSleep){
		return checkJobs(context.getScheduler(), QuartzTools.getName(context), listJobs, nThread, tSleep);
	}

	public static Vector<JobKey> checkJobs(Scheduler scheduler, String prefix, 
			Vector<JobKey> listJobs, Integer nThread, Integer tSleep){
		int numberThread = 10;
		int sleep = 5000;

		try {
			if (nThread!=null){
				numberThread = nThread.intValue();
			}

			if (tSleep!=null){
				sleep = tSleep.intValue();
			}
		} catch (NumberFormatException e) {
			log.error("["+prefix+"] "+e.getMessage(), e);
//		} catch (ConfigurationException e) {
//			log.error("["+QuartzTools.getName(context)+"] "+e.getMessage(), e);
		}
		while(true){
			for (int x=0; x<listJobs.size();x++){
				try {
					if (!scheduler.checkExists(listJobs.get(x))){
						listJobs.remove(x);
					}
				} catch (SchedulerException e) {
					log.error("["+prefix+"] "+e.getMessage(), e);
				}
			}
			if (listJobs.size()<numberThread){
				break;
			} else {
//				System.out.println("Dormo ["+sleep+"]");
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					log.error("["+prefix+"] "+e.getMessage(), e);
				}
			}
		}
		return listJobs;
	}

	public static boolean checkExecute(Scheduler scheduler, Vector<JobKey> jFolder){
//		 = null;
		JobKey key = null;
//		keys = parameter.getFolders().keys();
		boolean result = false;
		if (jFolder== null){
			result = true;
		} else {
			for (int x=0; x<jFolder.size(); x++){
				try {
					key = jFolder.get(x);
					if (scheduler==null){
						result = true;
					} else {
						if (scheduler.checkExists(key)) {
							result = true;
						}
					}
				} catch (SchedulerException e) {
					log.error(e.getMessage(), e);
					result = true;
				}
			}
		}
		return result;
	}

	public static boolean checkExecute(Enumeration<String> keys, Scheduler scheduler, Hashtable<String, JobKey> jFolder){
//		 = null;
		String key = null;
//		keys = parameter.getFolders().keys();
		boolean result = false;
		while (keys.hasMoreElements()) {
			try {
				key = keys.nextElement();
				if (jFolder== null){
					result = true;
				} else if (scheduler==null){
					result = true;
				} else {
					if (!(jFolder.get(key) == null
						|| !scheduler.checkExists(jFolder.get(key)))) {
						result = true;
					}
				}
			} catch (SchedulerException e) {
				log.error(e.getMessage(), e);
				result = true;
			}
		}
		return result;
	}
}
