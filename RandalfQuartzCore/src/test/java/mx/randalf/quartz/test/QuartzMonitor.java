/**
 * 
 */
package mx.randalf.quartz.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.Calendar;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.ListenerManager;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;
import org.quartz.impl.matchers.GroupMatcher;

import mx.randalf.quartz.QuartzMaster;

/**
 * @author massi
 *
 */
public class QuartzMonitor extends QuartzMaster {

	/**
	 * @throws SchedulerException 
	 * 
	 */
	public QuartzMonitor(String fileQuartz) throws SchedulerException {
		super(false, fileQuartz);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		QuartzMonitor quartzMaster = null;
		
		try {
			quartzMaster = new QuartzMonitor(args[0]);
			quartzMaster.monitor();
			quartzMaster.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void monitor() {

		try {
			monitorListString("", "CalendarName", this.scheduler.getCalendarNames());
			monitor("", "Context", this.scheduler.getContext());
			monitor("", "CurrentlyExecutingJobs",this.scheduler.getCurrentlyExecutingJobs());
			monitorJobGroupNames("", "JobGroupNames",this.scheduler.getJobGroupNames(), this.scheduler);
			monitor("", "ListenerManager",this.scheduler.getListenerManager());
			monitor("", "PausedTriggerGroups",this.scheduler.getPausedTriggerGroups());
			monitor("", "SchedulerInstanceId",this.scheduler.getSchedulerInstanceId());
			monitor("", "SchedulerName",this.scheduler.getSchedulerName());
			monitorTriggerGroupNames("", "TriggerGroupNames",this.scheduler.getTriggerGroupNames());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public static void monitorTriggerGroupNames(String prefix, String titolo, List<String> triggerGroupNames) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		for (String string : triggerGroupNames){
			monitor(prefix, "String",string);
		}
	}

	public static void monitor(String prefix, String titolo, Set<String> pausedTriggerGroups) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		for (String string : pausedTriggerGroups){
			monitor(prefix, "String",string);
		}
	}

	public static void monitor(String prefix, String titolo, ListenerManager listenerManager) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		monitorJobListeners(prefix, "JobListeners", listenerManager.getJobListeners());
		monitorSchedulerListeners(prefix, "SchedulerListeners",listenerManager.getSchedulerListeners());
		monitorTriggerListeners(prefix, "TriggerListeners",listenerManager.getTriggerListeners());
	}

	public static void monitorTriggerListeners(String prefix, String titolo, List<TriggerListener> triggerListeners) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		for (TriggerListener triggerListener : triggerListeners){
			monitor(prefix, "Name",triggerListener.getName());
		}
	}

	public static void monitorSchedulerListeners(String prefix, String titolo, List<SchedulerListener> schedulerListeners) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		for (SchedulerListener schedulerListener : schedulerListeners){
			monitor(prefix, "toString",schedulerListener.toString());
		}
	}

	public static void monitorJobListeners(String prefix, String titolo, List<JobListener> jobListeners) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		for (JobListener jobListener : jobListeners){
			monitor(prefix, "Name",jobListener.getName());
		}
	}

	public static void monitorJobGroupNames(String prefix, String titolo, List<String> jobGroupNames, Scheduler scheduler) throws SchedulerException {
		GroupMatcher<JobKey> groupMatcher = null;

		try {
			System.out.println(prefix+titolo+":");
			prefix += "\t";
			for (String jobGroupName : jobGroupNames){
				groupMatcher = GroupMatcher.jobGroupEquals(jobGroupName);
				for (JobKey jobKey : scheduler.getJobKeys(groupMatcher)){
					
					System.out.println(prefix+"JobName: "+jobKey.getName()+" JobGroup: "+jobKey.getGroup());
//					monitor(prefix, (List<Trigger>) scheduler.getTriggersOfJob(jobKey));

				}
			}
		} catch (SchedulerException e) {
			throw e;
		}
	}

//	private void monitor(String prefix, List<Trigger> triggers) {
//		prefix += "\t";
//		
//		for (Trigger trigger : triggers){
//			monitor(prefix, "CalendarName",trigger.getCalendarName());
//			monitor(prefix, "Description",trigger.getDescription());
//			monitor(prefix, "EndTime",trigger.getEndTime());
//			monitor(prefix, "FinalFireTime",trigger.getFinalFireTime());
//			monitor(prefix, "JobData",trigger.getJobDataMap());
//			monitor(prefix, "JobKey",trigger.getJobKey());
//			monitor(prefix, "Key",trigger.getKey());
//			monitor(prefix, "MisfireInstruction",trigger.getMisfireInstruction());
//			monitor(prefix, "NextFireTime",trigger.getNextFireTime());
//			monitor(prefix, "PreviousFireTime",trigger.getPreviousFireTime());
//			monitor(prefix, "Priority",trigger.getPriority());
//			monitor(prefix, "ScheduleBuilder",trigger.getScheduleBuilder());
//			monitor(prefix, "StartTime",trigger.getStartTime());
//			monitor(prefix, "TriggerBuilder",trigger.getTriggerBuilder());
//		}
//	}

	public static void monitor(String prefix, String titolo, List<JobExecutionContext> currentlyExecutingJobs) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		for (JobExecutionContext currentlyExecutingJob : currentlyExecutingJobs){
			monitor(prefix, "Calendar",currentlyExecutingJob.getCalendar());
			monitor(prefix, "FireInstanceId",currentlyExecutingJob.getFireInstanceId());
			monitor(prefix, "FireTime",currentlyExecutingJob.getFireTime());
			monitor(prefix, "JobDetaild",currentlyExecutingJob.getJobDetail());
			monitor(prefix, "JobInstance",currentlyExecutingJob.getJobInstance());
			monitor(prefix, "JobRunTime",currentlyExecutingJob.getJobRunTime());
			monitor(prefix, "MergedJobDataMap",currentlyExecutingJob.getMergedJobDataMap());
			monitor(prefix, "NextFireTime",currentlyExecutingJob.getNextFireTime());
			monitor(prefix, "PreviousFireTime",currentlyExecutingJob.getPreviousFireTime());
			monitor(prefix, "RecoveringTriggerKey",currentlyExecutingJob.getRecoveringTriggerKey());
			monitor(prefix, "RefireCount",currentlyExecutingJob.getRefireCount());
			monitorObject(prefix, "Result",currentlyExecutingJob.getResult());
			monitor(prefix, "ScheduledFireTime",currentlyExecutingJob.getScheduledFireTime());
			monitor(prefix, "Scheduler",currentlyExecutingJob.getScheduler());
			monitor(prefix, "Trigger",currentlyExecutingJob.getTrigger());
		}
	}

	public static void monitor(String prefix, String titolo, Trigger context) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		monitor(prefix, "getCalendarName",context.getCalendarName());
		monitor(prefix, "getDescription",context.getDescription());
		monitor(prefix, "getEndTime",context.getEndTime());
		monitor(prefix, "getFinalFireTime",context.getFinalFireTime());
		monitor(prefix, "getJobDataMap",context.getJobDataMap());
		monitor(prefix, "getJobKey",context.getJobKey());
		monitor(prefix, "getKey",context.getKey());
		monitor(prefix, "getMisfireInstruction",context.getMisfireInstruction());
		monitor(prefix, "getNextFireTime",context.getNextFireTime());
		monitor(prefix, "getPreviousFireTime",context.getPreviousFireTime());
		monitor(prefix, "getPriority",context.getPriority());
		monitor(prefix, "getScheduleBuilder",context.getScheduleBuilder());
		monitor(prefix, "getStartTime",context.getStartTime());
		monitor(prefix, "getTriggerBuilder",context.getTriggerBuilder());
	}

	public static void monitor(String prefix, String titolo, TriggerBuilder<? extends Trigger> triggerBuilder) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
	}

	public static void monitor(String prefix, String titolo, ScheduleBuilder<? extends Trigger> scheduleBuilder) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
	}

	public static void monitor(String prefix, String titolo, JobKey jobKey) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
	}

	public static void monitor(String prefix, String titolo, Scheduler scheduler) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
//		monitor(prefix, "Group",context.getGroup());
	}

	public static void monitorObject(String prefix, String titolo, Object context) {
		if (context instanceof String){
			monitor(prefix, titolo, (String)context);
		} else {
			System.out.println(prefix+"ERR: "+context.getClass().getName());
		}
	}

	public static void monitor(String prefix, String titolo, TriggerKey context) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		monitor(prefix, "Group",context.getGroup());
		monitor(prefix, "Name",context.getName());
	}

	public static void monitor(String prefix, String titolo, JobDataMap context) {
		String[] keys = null;
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		keys = context.getKeys();
		for (int x=0; x<keys.length; x++){
			monitorObject(prefix, keys[x], context.get(keys[x]));
		}
		
	}

	public static void monitor(String prefix, String titolo, Job jobInstance) {
		System.out.println(prefix+titolo+": "+jobInstance);
	}

	public static void monitor(String prefix, String titolo, Calendar calendar) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		monitor(prefix, "BaseCalendar",calendar.getBaseCalendar());
		monitor(prefix, "Description",calendar.getDescription());
	}

	public static void monitor(String prefix, String titolo, JobDetail jobDetail) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		monitor(prefix, "Description",jobDetail.getDescription());
	}

	public static void monitor(String prefix, String titolo, Date object) {
		SimpleDateFormat simpleDateFormat = null;
		
		simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		System.out.println(prefix+titolo+": "+(object != null?simpleDateFormat.format(object):"null"));
	}

	public static void monitor(String prefix, String titolo, long object) {
		System.out.println(prefix+titolo+": "+object);
	}

	public static void monitor(String prefix, String titolo, SchedulerContext context) {
		String[] keys = null;
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		keys = context.getKeys();
		for (int x=0; x<keys.length; x++){
			monitorObject(prefix, keys[x], context.get(keys[x]));
		}
	}

	public static void monitor(String prefix, String titolo, String object) {
		System.out.println(prefix+titolo+": "+object);
	}

	public static void monitorListString(String prefix, String titolo, List<String> calendarNames) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		for (String calendarName : calendarNames){
			monitor(prefix, calendarName);
		}
	}

	public static void monitor(String prefix, String testo) {
		System.out.println(prefix+testo);
	}

	public static void monitor(String prefix, String titolo, SchedulerMetaData metaData) {
		System.out.println(prefix+titolo+":");
		prefix += "\t";
		monitor(prefix, "RunningSince",metaData.getRunningSince());
		monitor(prefix, "SchedulerInstanceId",metaData.getSchedulerInstanceId());
		monitor(prefix, "chedulerName",metaData.getSchedulerName());
		try {
			monitor(prefix, "Summary",metaData.getSummary());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		monitor(prefix, "Version",metaData.getVersion());
		monitor(prefix, "NumberOfJobsExecuted",metaData.getNumberOfJobsExecuted());
		monitor(prefix, "ThreadPoolSize",metaData.getThreadPoolSize());
	}

}
