/**
 * 
 */
package mx.randalf.quartz.job;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import mx.randalf.quartz.QuartzTools;

/**
 * @author massi
 *
 */
public abstract class JobExecute implements Job {

	private Logger log = Logger.getLogger(JobExecute.class);

	private Vector<JobKey> listJobs = null;

	/**
	 * 
	 */
	public JobExecute() {
		listJobs = new Vector<JobKey>();
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if (QuartzTools.checkJob(context)) {
				context.setResult(jobExecute(context));
				context.getScheduler().unscheduleJob(context.getTrigger().getKey());
			} else if (context.getScheduler().isShutdown()) {
				context.setResult("Processo interrotto a causa dello spengimento del processo");
				context.getScheduler().unscheduleJob(context.getTrigger().getKey());
			}
		} catch (JobExecutionException e) {
			e.setUnscheduleAllTriggers(true);
			throw e;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			JobExecutionException e1 = new JobExecutionException(e.getMessage(), e);
			e1.setUnscheduleAllTriggers(true);
			throw e1;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			JobExecutionException e1 = new JobExecutionException(e.getMessage(), e);
			e1.setUnscheduleAllTriggers(true);
			throw e1;
		}
	}

	protected abstract String jobExecute(JobExecutionContext context) throws JobExecutionException;

	protected void waithEndJobs(JobExecutionContext context) {
		while (true) {
			for (int x = 0; x < listJobs.size(); x++) {
				try {
					if (!context.getScheduler().checkExists(listJobs.get(x))) {
						listJobs.remove(x);
					}
				} catch (SchedulerException e) {
					log.error("[" + QuartzTools.getName(context) + "] " + e.getMessage(), e);
				}
			}
			if (listJobs.size() == 0) {
				break;
			} else {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					log.error("[" + QuartzTools.getName(context) + "] " + e.getMessage(), e);
				}
			}
		}
	}

	protected void start(JobExecutionContext context, Class<? extends JobExecute> jClass, String jobGroup,
			String jobName, String triggerGroup, String triggerName, Hashtable<String, Object> params)
			throws SchedulerException {

		try {
			log.info("\n"+"[" + QuartzTools.getName(context) + "] Schedulo il Job");
			listJobs.add(QuartzTools.startJob(context.getScheduler(), jClass, jobGroup, jobName, triggerGroup,
					triggerName, params));
		} catch (SchedulerException e) {
			log.error("[" + QuartzTools.getName(context) + "] " + e.getMessage(), e);
			throw e;
		}
	}

}
