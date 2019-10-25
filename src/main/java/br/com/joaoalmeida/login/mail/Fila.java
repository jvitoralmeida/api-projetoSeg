package br.com.joaoalmeida.login.mail;

import java.util.LinkedList;
import java.util.Queue;

import br.com.joaovitor.Mail;

public class Fila {

	private static Queue<Mail> fila = new LinkedList<Mail>();

	public static void addInQueue(Mail mail) {// adiciona na fila
		fila.add(mail);
	}

	public static Mail headOfQueue() { // retorna o primeiro da fila sem excluir ele
		return fila.peek();
	}

	public static void removeElementOfQueue() {
		fila.remove();
	}

	public static Mail headOfQueueWithDelete() {// retorna o primeiro da fila e depois exclui ele
		return fila.poll();
	}

	public static Integer sizeOfQueue() { // retorna tamanho da fila
		return fila.size();
	}
}
