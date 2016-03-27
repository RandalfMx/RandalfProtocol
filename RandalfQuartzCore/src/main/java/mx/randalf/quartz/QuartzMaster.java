/**
 * 
 */
package mx.randalf.quartz;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author massi
 *
 */
public class QuartzMaster {
	
	private Vector<JobKey> listJobs = null;

	private static Logger log = Logger.getLogger(QuartzMaster.class);

	private Integer nThread = 10;

	private Integer tSleep = 1000;

	private Integer tSleepClosed = 60000;

	/**
	 * Variabile utilizzata per la gestione della schedurlazione delle attività
	 */
	protected Scheduler scheduler = null;

	/**
	 * @throws SchedulerException 
	 * 
	 */
	public QuartzMaster(boolean startScheduler) throws SchedulerException {

		listJobs = new Vector<JobKey>();
		if (startScheduler){
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		}
	}

	public void closed(boolean shutdown) throws SchedulerException{
		try {
			while (true) {
				if (!QuartzTools.checkExecute(scheduler, listJobs)) {
					break;
				}
				try {
					Thread.sleep(tSleepClosed);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (shutdown){
				System.out.println("ESCO");
				scheduler.shutdown(true);
			}
		} catch (SchedulerException e) {
			throw e;
		}
	}

	public void add(String prefix, JobKey jobKey) {
		listJobs = QuartzTools.checkJobs(scheduler, prefix, listJobs, nThread, tSleep);
		listJobs.add(jobKey);
	}

	public void setnThread(Integer nThread) {
		this.nThread = nThread;
	}

	public void settSleep(Integer tSleep) {
		this.tSleep = tSleep;
	}

	public void settSleepClosed(Integer tSleepClosed) {
		this.tSleepClosed = tSleepClosed;
	}

	/**
	 * @param scheduler the scheduler to set
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}
