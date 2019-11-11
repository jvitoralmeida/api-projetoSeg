package br.com.joaoalmeida.login.mail;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class RunQuartz {

	public static void runQuartz() {

		final SchedulerFactory shedFact = new StdSchedulerFactory();

		try {
			final Scheduler scheduler = shedFact.getScheduler();
			scheduler.start();
			final JobDetail job = JobBuilder.newJob(JobEmail.class).withIdentity("JobEmail", "grupo01").build();
			final Trigger trigger = TriggerBuilder.newTrigger().withIdentity("EmailTrigger", "groupo01")
					.withSchedule(CronScheduleBuilder.cronSchedule("0/10 0 0 ? * * *")).build();

			scheduler.scheduleJob(job, trigger);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
