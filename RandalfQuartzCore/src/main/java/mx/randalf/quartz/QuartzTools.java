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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;

import mx.randalf.quartz.job.JobExecute;

/**
 * @author massi
 *
 */
public class QuartzTools {

	private static Logger log = LogManager.getLogger(QuartzTools.class);

	/**
	 * 
	 */
	public QuartzTools() {
	}

	public static JobKey startJob(Scheduler scheduler, Class<? extends JobExecute> jClass, 
			String jobGroup, String jobName, String triggerGroup, String triggerName, Hashtable<String, Object> params) 
			throws SchedulerException{
		return startJob(scheduler, jClass, jobGroup, jobName, triggerGroup, triggerName, params, null);
	}

	public static JobKey startJob(Scheduler scheduler, Class<? extends JobExecute> jClass, 
			String jobGroup, String jobName, String triggerGroup, String triggerName, 
			Hashtable<String, Object> params,
			ScheduleBuilder<?> schedBuilder) 
			throws SchedulerException{
		return startJob(scheduler, jClass, jobGroup, jobName, triggerGroup, triggerName, params, schedBuilder, null);
	}

	public static JobKey startJob(Scheduler scheduler, Class<? extends JobExecute> jClass, 
			String jobGroup, String jobName, String triggerGroup, String triggerName, 
			Hashtable<String, Object> params,
			ScheduleBuilder<?> schedBuilder, Integer priority) 
			throws SchedulerException{
		JobDetail job = null;
		Trigger trigger = null;
		Set<Trigger> triggers = null;
		Enumeration<String> keys = null;
		String key = null;

		try {
			job = newJob(jClass).
					withIdentity(jobName, jobGroup)
//					.storeDurably(true)
					.build();

			if (params != null){
				keys = params.keys();
				while (keys.hasMoreElements()){
					key = keys.nextElement();
					job.getJobDataMap().put(key, params.get(key));
				}
			}

			if (schedBuilder != null){
				if (priority != null){
					trigger = newTrigger()
							.withIdentity(triggerName, triggerGroup)
							.withSchedule(schedBuilder)
							.withPriority(priority)
							.startNow()
							.build();
				} else {
					trigger = newTrigger()
							.withIdentity(triggerName, triggerGroup)
							.withSchedule(schedBuilder)
							.startNow()
							.build();
				}
			} else {
				if (priority != null){
					trigger = newTrigger()
							.withIdentity(triggerName, triggerGroup)
							.withPriority(priority)
							.startNow()
							.withSchedule(SimpleScheduleBuilder.simpleSchedule()
							        .withRepeatCount(300)
							        .withIntervalInMinutes(2))
							.build();
				} else {
					trigger = newTrigger()
							.withIdentity(triggerName, triggerGroup)
							.startNow()
							.withSchedule(SimpleScheduleBuilder.simpleSchedule()
							        .withRepeatCount(1000)
							        .withIntervalInSeconds(2))
							.build();
				}
			}

			checkExistJob(scheduler, job.getKey(), trigger.getKey());
			if (!scheduler.isShutdown()){
				triggers = new HashSet<Trigger>();
				triggers.add(trigger);
				scheduler.scheduleJob(job, triggers, false);
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SchedulerException(e.getMessage(),e);
		}
		return job.getKey();
	}

	private static void checkExistJob(Scheduler scheduler, JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
		int numJobs = 0;
		try {
			while(true){
				
				numJobs = checkJob(scheduler, jobKey, triggerKey);
				if (numJobs == -1){
					break;
				} else if (numJobs ==0){
					if (scheduler.checkExists(jobKey)){
						if  (!checkTriggers(scheduler, scheduler.getTriggersOfJob(jobKey))){
							break;
						}
					} else {
						break;
					}
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					log.error("["+jobKey.getGroup()+" => "+jobKey.getName()+"] "+e.getMessage(), e);
				}
			}
		} catch (SchedulerException e) {
			throw e;
		}
	}

	public static boolean checkJob(JobExecutionContext context) throws SchedulerException{
		int conta = 0;
		
		conta = checkJob(context.getScheduler(), context.getJobDetail().getKey(),
				context.getTrigger().getKey());
		return (conta==-1?false:conta<=1);
	}

	public static int checkJob(Scheduler scheduler, JobKey jobKey, TriggerKey triggerKey) throws SchedulerException{
		List<JobExecutionContext> jobs = null;
		int conta = 0;

		try {
			if (scheduler.isShutdown()){
				System.out.println("IN SHUTDOWN " + jobKey.getGroup()+"/"+jobKey.getName()+" - "+triggerKey.getGroup()+"/"+triggerKey.getName());
				conta = -1;
			} else if (scheduler.isStarted()){
				jobs = scheduler.getCurrentlyExecutingJobs();
				if (jobs != null && jobs.size()>0){
					for(JobExecutionContext job : jobs){
						if (job.getJobDetail().getKey().getGroup().equals(jobKey.getGroup()) &&
								job.getJobDetail().getKey().getName().equals(jobKey.getName()) &&
								job.getTrigger().getKey().getGroup().equals(triggerKey.getGroup()) &&
								job.getTrigger().getKey().getName().equals(triggerKey.getName()) ){
							conta++;
						}
					}
				}
			}
		} catch (SchedulerException e) {
			throw e;
		}
		return conta;
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

	public static Hashtable<String, JobKey> checkJobs(JobExecutionContext context, Hashtable<String, JobKey> listJobs, Integer nThread, Integer tSleep){
		return checkJobs(context.getScheduler(), QuartzTools.getName(context), listJobs, nThread, tSleep);
	}

	public static Hashtable<String, JobKey> checkJobs(Scheduler scheduler, String prefix, 
			Hashtable<String,JobKey> listJobs, Integer nThread, Integer tSleep){
		int numberThread = 10;
		int sleep = 5000;
		Enumeration<String> keys = null;
		String key = null;

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
			keys = listJobs.keys();
			while(keys.hasMoreElements()){
				key = keys.nextElement();
				try {
					
					if (!scheduler.checkExists(listJobs.get(key))){
						log.debug("\n"+"Remove: "+key);
						listJobs.remove(key);
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

	private static boolean checkTriggers(Scheduler scheduler, List<? extends Trigger> triggers) throws SchedulerException{
		boolean result = false;
		TriggerState triggerState = null;

		try {
			if  (triggers != null && triggers.size()>0){
				triggerState = null;
				for (Trigger trigger : triggers){
					triggerState = scheduler.getTriggerState(trigger.getKey());
					if (triggerState != null){
						break;
					}
				}
				if (triggerState != null){
					if (TriggerState.BLOCKED.equals(triggerState)){
						result = true;
					} else if (TriggerState.COMPLETE.equals(triggerState)){
						result = false;
					} else if (TriggerState.ERROR.equals(triggerState)){
						result = true;
					} else if (TriggerState.NORMAL.equals(triggerState)){
						result = true;
					} else if (TriggerState.PAUSED.equals(triggerState)){
						result = true;
					} else if (TriggerState.NONE.equals(triggerState)){
						result = false;
					} else {
						result = false;
					}

				}
			}
		} catch (SchedulerException e) {
			throw e;
		}
		return result;
	}

	public static boolean checkExecute(Scheduler scheduler, Hashtable<String, JobKey> jFolder){
//		 = null;
		JobKey jobKey = null;
		String key = null;
		JobDetail jobDetail = null;
		Enumeration<String> keys = null;
//		keys = parameter.getFolders().keys();
		boolean result = false;
		
		log.debug("\n\ncheckExecute START");
		if (jFolder== null){
			result = true;
		} else {
			try {
				if (!scheduler.getCurrentlyExecutingJobs().isEmpty() && scheduler.getCurrentlyExecutingJobs().size()>1){
					log.debug("\n"+"CurrentlyExecutingJobs => "+scheduler.getCurrentlyExecutingJobs().size());
					result=true;
				} else {
					keys = jFolder.keys();
					while (keys.hasMoreElements()){
//			for (int x=0; x<jFolder.size(); x++){
						try {
							key = keys.nextElement();
							jobKey = jFolder.get(key);
							log.debug("\n"+"CheckExecute: "+jobKey.getGroup()+" => "+jobKey.getName()+" ");
							if (scheduler==null){
								log.debug("\n"+"scheduler IS NULL");
								result = true;
							} else {
								jobDetail = scheduler.getJobDetail(jobKey);
								
								if  (checkTriggers(scheduler, scheduler.getTriggersOfJob(jobKey))){
									result = true;
								} else if (jobDetail != null && jobDetail.getDescription() != null){
									log.debug("\n"+"jobDetail: "+jobDetail.getDescription());
									result = true;
								} else if (scheduler.checkExists(jobKey)) {
									log.debug("\n"+"checkExists ID TRUE");
									result = true;
								} else  {
									log.debug("\n"+"E' finito");
								}
								log.debug("\n"+" "+result);
							}
						} catch (SchedulerException e) {
							log.error(e.getMessage(), e);
							result = true;
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							result = true;
						}
					}
				}
			} catch (SchedulerException e) {
				log.error(e.getMessage(), e);
				result = true;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				result = true;
			}
		}
		System.out.println("checkExecute END => result: "+result);
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
