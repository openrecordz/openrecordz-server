package shoppino.scheduler;

import org.quartz.Job;

public interface SchedulerService {

    void register( Class<? extends Job> jobClass, String cronExpression );

    void reschedule( Class<? extends Job> jobClass, String cronExpression );

}

