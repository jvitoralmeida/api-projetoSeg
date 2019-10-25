package br.com.joaoalmeida.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.joaoalmeida.login.mail.RunQuartz;

@SpringBootApplication
public class LoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
		RunQuartz.runQuartz();
	}

}
