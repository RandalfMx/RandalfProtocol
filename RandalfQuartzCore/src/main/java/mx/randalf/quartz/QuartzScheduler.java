/**
 * 
 */
package mx.randalf.quartz;

import java.util.Hashtable;
import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * @author massi
 *
 */
public abstract class QuartzScheduler extends QuartzMaster {

	/**
	 * @param processing
	 * @param fileQuartz
	 * @param socketPort
	 * @param closeSocket
	 * @throws SchedulerException
	 */
	public QuartzScheduler(boolean processing, String fileQuartz, Integer socketPort, boolean closeSocket, 
			boolean reScheduling, boolean quartzScheduler)
			throws SchedulerException {
		super(processing, fileQuartz, socketPort, closeSocket, reScheduling, quartzScheduler);
	}

	protected static Hashtable<String, Object> checkConf(String[] args){
		Hashtable<String, Object> result = null;
		
		result = new Hashtable<String, Object>();
		
		for (int x=0; x<args.length; x++){
			if (args[x].equals("-h") || args[x].equals("/?")){
				QuartzScheduler.help();
				System.exit(0);
			} else if (args[x].equals("-f")){
				x++;
				result.put("fileQuartz",args[x]);
			} else if (args[x].equals("-s")){
				result.put("scheduling",true);
			} else if (args[x].equals("-p")){
				result.put("startScheduler",true);
			} else if (args[x].equals("-P")){
				x++;
				result.put("socketPort",new Integer(args[x]));
			} else if (args[x].equals("-S")){
				result.put("closeSocket",true);
			} else if (args[x].equals("-v")){
				result.put("printStatus",true);
			}
		}

		return result;
	}

	protected abstract void scheduling() throws SchedulerException;

	protected abstract void reScheduling() throws SchedulerException;

	public static void help() {
		System.out.println("E' necessario indicare i seguenti parametri");
		System.out.println("1) -f <Nome files Quartz> (Obbligatorio)");
		System.out.println("2) -s l'applicazione si occuperà di schedulare i Jobs (Opzionale) !!! ATTENZIONE !!! non attivate più processi di qusto tipo");
		System.out.println("3) -p l'applicazione si occuperà di processare i Jobs in attesa (Opzionale)");
		System.out.println("4) -P <numero Porta Socket closing> default 9000 (Opzionale)");
		System.out.println("5) -S Usato per eseguire lo shutdown dell'applicazione (Opzionale)");
	}

	public void print() throws SchedulerException{
		String jobName = null;
		String jobGroup = null;

		try {
		for (String groupName : scheduler.getJobGroupNames()) {

			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

				jobName = jobKey.getName();
				jobGroup = jobKey.getGroup();

				System.out.println("[jobName] : " + jobName + "\t[groupName] : "+jobGroup);
				print(scheduler.getTriggersOfJob(jobKey));

		  }

	    }
		} catch (SchedulerException e) {
			throw e;
		} finally {
			scheduler.shutdown();
		}
	}

	private void print(List<? extends Trigger> list){
		for (Trigger trigger : list){
			System.out.println("\tgetCalendarName\t\t"+trigger.getCalendarName());
			System.out.println("\tgetDescription\t\t"+trigger.getDescription());
			System.out.println("\tgetEndTime\t\t"+trigger.getEndTime());
			System.out.println("\tgetFinalFireTime\t"+trigger.getFinalFireTime());
			System.out.println("\tgetJobDataMap");
			print(trigger.getJobDataMap());
			System.out.println("\tgetKey\t\t\t"+trigger.getKey());
			System.out.println("\tgetMisfireInstruction\t"+trigger.getMisfireInstruction());
			System.out.println("\tgetNextFireTime\t\t"+trigger.getNextFireTime());
			System.out.println("\tgetPreviousFireTime\t"+trigger.getPreviousFireTime());
			System.out.println("\tgetPriority\t\t"+trigger.getPriority());
			System.out.println("\tgetScheduleBuilder\t"+trigger.getScheduleBuilder());
			System.out.println("\tgetStartTime\t\t"+trigger.getStartTime());
			System.out.println("\tgetTriggerBuilder\t"+trigger.getTriggerBuilder());
			
		}
		
	}

	private void print(JobDataMap jobDataMap){
		String[] keys = null;

		keys = jobDataMap.getKeys();
		
		for (int x=0; x<keys.length; x++){
			System.out.println("\t\tKeys: "+keys[x]);
		}
	}

}
