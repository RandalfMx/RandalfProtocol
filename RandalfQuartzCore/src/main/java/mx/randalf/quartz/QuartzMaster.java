/**
 * 
 */
package mx.randalf.quartz;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author massi
 *
 */
public class QuartzMaster {
	
	private Hashtable<String, JobKey> listJobs = null;

	private static Logger log = Logger.getLogger(QuartzMaster.class);

//	private Integer nThread = 10;
//
//	private Integer tSleep = 1000;

	private Integer tSleepClosed = 60000;

	private QuartzSocket socketClosed = null;
	
	private boolean shutdown = false;

	/**
	 * Variabile utilizzata per la gestione della schedurlazione delle attività
	 */
	protected Scheduler scheduler = null;

	/**
	 * @throws SchedulerException 
	 * 
	 */
	public QuartzMaster(boolean processing) throws SchedulerException {
		this(null, processing, null, null, false, false);
	}

	/**
	 * @throws SchedulerException 
	 * 
	 */
	public QuartzMaster(boolean processing, String fileQuartz) 
				throws SchedulerException {
		this(null, processing, fileQuartz, null, false, false);
	}

	/**
	 * @throws SchedulerException 
	 * 
	 */
	public QuartzMaster(boolean processing, String fileQuartz, 
			Integer socketPort, boolean closeSocket, boolean reScheduling) throws SchedulerException {
		this(null, processing, fileQuartz, socketPort, closeSocket, reScheduling);
	}
	
	/**
	 * @throws SchedulerException 
	 * 
	 */
	public QuartzMaster(Scheduler scheduler, boolean processing, 
			String fileQuartz, Integer socketPort, boolean closeSocket, boolean reScheduling) throws SchedulerException {
		StdSchedulerFactory sf = null;
		
		listJobs = new Hashtable<String, JobKey>();
		if (!closeSocket){
			if (scheduler == null){
				if (fileQuartz != null){
					sf = new StdSchedulerFactory(fileQuartz);
				} else {
					sf = new StdSchedulerFactory();
				}
				this.scheduler = sf.getScheduler();
				this.scheduler.getListenerManager().addTriggerListener(new TriggerListener(){

				    private Date lastFireTime = null;

				    @Override
				    public String getName() {
				        return "prevent-duplicate-fires";
				    }

				    @Override
				    public void triggerFired(Trigger trigger, JobExecutionContext context) {
				    }

				    @Override
				    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
				        final Date fireTime = context.getScheduledFireTime();
				        if (lastFireTime != null && fireTime.equals(lastFireTime)) {
				            return true;
				        }
				        lastFireTime = fireTime;
				        return false;
				    }

				    @Override
				    public void triggerMisfired(Trigger trigger) {
				    }

				    @Override
				    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
				    }
				});

			} else {
				this.scheduler = scheduler;
			}
			if (processing){
				this.scheduler.start();
			}
		}
		
		if (socketPort != null && !closeSocket && !reScheduling){
			socketClosed = new QuartzSocket(this, socketPort);
			socketClosed.start();
		} else if (socketPort != null && closeSocket && !reScheduling){
			socketClosed = new QuartzSocket(socketPort);
		}
	}

	public void closed(boolean shutdown) throws SchedulerException{
		try {
			try {
				Thread.sleep(tSleepClosed*10);
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
			while (true) {
				if (!QuartzTools.checkExecute(scheduler, listJobs)) {
					log.debug("\nFINEEEE");
					break;
				}
				try {
					Thread.sleep(tSleepClosed);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (shutdown){
				log.debug("\nESCO");
				scheduler.shutdown(true);
			}
		} catch (SchedulerException e) {
			throw e;
		}
	}

	public void add(String prefix, JobKey jobKey) {
//		listJobs = QuartzTools.checkJobs(scheduler, prefix, listJobs, nThread, tSleep);
		log.debug("\nADD: "+jobKey);
		listJobs.put(jobKey.getGroup()+"-"+jobKey.getName(),jobKey);
	}

//	public void setnThread(Integer nThread) {
//		this.nThread = nThread;
//	}
//
//	public void settSleep(Integer tSleep) {
//		this.tSleep = tSleep;
//	}

	public void settSleepClosed(Integer tSleepClosed) {
		this.tSleepClosed = tSleepClosed;
	}

	/**
	 * @param scheduler the scheduler to set
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void shutdown() throws SchedulerException{
		shutdown = true;
		this.scheduler.shutdown(true);
	}

	public boolean isShutdown(){
		return shutdown;
	}

	public TriggerState getStatoJob(String jobGroup, String jobName) throws SchedulerException{
		List<? extends Trigger> triggers = null;
		JobKey jobKey = null;
		TriggerState triggerState = null;

		try {
			jobKey = new JobKey(jobName, jobGroup);
			if (this.scheduler.checkExists(jobKey)){
				triggers = this.scheduler.getTriggersOfJob(jobKey);
				if (triggers != null){
					for (Trigger trigger : triggers){
						triggerState = this.scheduler.getTriggerState(trigger.getKey());
						if (triggerState != null){
							break;
						}
					}
				}
			}
		} catch (SchedulerException e) {
			throw e;
		}
		return triggerState;
	}
}