package br.com.joaoalmeida.login.mail;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.joaovitor.Mail;

public class JobEmail implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {

			final Mail firstMailOfHead = Fila.headOfQueueWithDelete();

			if (firstMailOfHead != null) {
				firstMailOfHead.enviaEmail();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}