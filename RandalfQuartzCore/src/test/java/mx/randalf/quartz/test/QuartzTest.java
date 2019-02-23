/**
 * 
 */
package mx.randalf.quartz.test;

import java.util.Collection;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author massi
 *
 */
public class QuartzTest {

	/**
	 * 
	 */
	public QuartzTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		QuartzTest quartzTest = null;
		
		if (args.length==1) {
			quartzTest = new QuartzTest();
			quartzTest.esegui(args[0]);
		} else {
			System.out.println("Indicare il nome del file di configurazione di Quartz");
		}
	}

	public void esegui(String fileName) {
		StdSchedulerFactory sf = null;
		Collection<Scheduler> schedulers = null; 
		
		try {
			sf = new StdSchedulerFactory(fileName);
			schedulers = sf.getAllSchedulers();
			
			for (Scheduler scheduler : schedulers) {
				System.out.println("SchedulerInstanceId: "+
						scheduler.getSchedulerInstanceId());
			}
			Scheduler scheduler = sf.getScheduler();
			monitor(scheduler);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void monitor(Scheduler scheduler) throws SchedulerException {
		QuartzMonitor.monitorListString("", "CalendarName", scheduler.getCalendarNames());
		QuartzMonitor.monitor("", "Context", scheduler.getContext());
		QuartzMonitor.monitor("", "CurrentlyExecutingJobs",scheduler.getCurrentlyExecutingJobs());
		QuartzMonitor.monitorJobGroupNames("", "JobGroupNames",scheduler.getJobGroupNames(), scheduler);
		QuartzMonitor.monitor("", "ListenerManager",scheduler.getListenerManager());
		QuartzMonitor.monitor("", "MetaData",scheduler.getMetaData());
		QuartzMonitor.monitor("", "PausedTriggerGroups",scheduler.getPausedTriggerGroups());
		QuartzMonitor.monitor("", "SchedulerInstanceId",scheduler.getSchedulerInstanceId());
		QuartzMonitor.monitor("", "SchedulerName",scheduler.getSchedulerName());
		QuartzMonitor.monitorTriggerGroupNames("", "TriggerGroupNames",scheduler.getTriggerGroupNames());
	}

}
