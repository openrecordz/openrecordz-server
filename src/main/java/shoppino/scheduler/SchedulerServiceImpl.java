package shoppino.scheduler;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;




//@Service
public class SchedulerServiceImpl implements SchedulerService {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Override
	public void register(Class<? extends Job> jobClass, String cronExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reschedule(Class<? extends Job> jobClass, String cronExpression) {
		// TODO Auto-generated method stub
		
	}
	@PostConstruct
  void init() {
		System.out.println("test");
	}
	
//    private Scheduler scheduler;
//
//    @PostConstruct
//    void init() {
//        try {
//            scheduler = StdSchedulerFactory.getDefaultScheduler();
//            scheduler.start();
//        } catch ( Exception e ) {
//            // handle exception
//        }
//    }
//
//    @PreDestroy
//    void destroy() {
//        try {
//            scheduler.shutdown();
//        } catch ( Exception e ) {
//            // handle exception
//        }
//    }
//
//    @Override
//    public void register( Class<? extends Job> jobClass, String cronExpression ) {
//        try {
//            String name = jobClass.getSimpleName();
//            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule( cronExpression );
//
//            JobDetail jobDetail = JobBuilder.newJob( jobClass ).withIdentity( name ).build();
//            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity( name ).withSchedule( cronScheduleBuilder ).build();
//
//            scheduler.scheduleJob( jobDetail, cronTrigger );
//        } catch ( Exception e ) {
//            // handle exception
//        }
//    }

//    @Override
//    public void reschedule( Class<? extends Job> jobClass, String cronExpression ) {
//        try {
//            String name = jobClass.getSimpleName();
//            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule( cronExpression );
//
//            CronTrigger newCronTrigger = TriggerBuilder.newTrigger().withIdentity( name ).withSchedule( cronScheduleBuilder ).build();
//
//            scheduler.rescheduleJob( TriggerKey.triggerKey( name ), newCronTrigger );
//        } catch ( Exception e ) {
//            // handle exception
//        }
//    }

}
