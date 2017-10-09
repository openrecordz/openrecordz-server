package shoppino.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class Test {

	/**
	 * @param args
	 * @throws SchedulerException 
	 */
//	public static void main(String[] args) throws SchedulerException {
//		//Quartz 1.6.3
//    	//JobDetail job = new JobDetail();
//    	//job.setName("dummyJobName");
//    	//job.setJobClass(HelloJob.class);    	
//    	JobDetail job = JobBuilder.newJob(HelloJob.class)
//		.withIdentity("dummyJobName", "group1").build();
// 
//	//Quartz 1.6.3
//    	//CronTrigger trigger = new CronTrigger();
//    	//trigger.setName("dummyTriggerName");
//    	//trigger.setCronExpression("0/5 * * * * ?");
// 
//    	Trigger trigger = Trigger
//		.newTrigger()
//		.withIdentity("dummyTriggerName", "group1")
//		.withSchedule(
//			CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//		.build();
// 
//    	//schedule it
//    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//    	scheduler.start();
//    	scheduler.scheduleJob(job, trigger);
//	}

}
