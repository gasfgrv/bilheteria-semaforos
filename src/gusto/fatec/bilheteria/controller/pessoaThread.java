package gusto.fatec.bilheteria.controller;

import java.util.concurrent.Semaphore;

public class pessoaThread extends Thread {

	public static int qtdeIngressos = 100;

	private int ingressos;
	private Semaphore semaforo;

	public pessoaThread(int pessoa, Semaphore semaforo) {
		this.ingressos = pessoa;
		this.semaforo = semaforo;
	}

	@Override
	public void run() {
		if (login()) {
			if (compraIngresso()) {
				try {
					semaforo.acquire();
					validaIngresso();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					semaforo.release();
				}
			}
		}
	}

	public boolean login() {
		ingressos = (int) ((Math.random() * 3) + 4);
		int tempo = 50 + (int) (Math.random() * (2000 - 50));

		try {
			pessoaThread.sleep(tempo);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (tempo < 1000) {
			System.out.println("Login efetuado com sucesso");
			return true;
		} else {
			System.out.println("Desculpe site lotado tente outra vez ;S");
			return false;
		}
	}

	public boolean compraIngresso() {
		int tempo = 1000 + (int) (Math.random() * (3000 - 1000));
		
		try {
			pessoaThread.sleep(tempo);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (tempo >= 2500) {
			System.out.println("Desculpe infelizmente seu tempo expirou");
			return false;
		} else {
			return true;
		}
	}

	public void validaIngresso() {
		if (ingressos >= qtdeIngressos) {
			System.out.println("Infelizmente a quantidade de ingressos em estoque "
					+ "não é o suficiente, ingressos restantes - " + qtdeIngressos);
		} else {
			qtdeIngressos = qtdeIngressos - ingressos;
			System.out.println(
					"Parabéns você adquiriu " + ingressos + " quantidade restante de ingressos : " + qtdeIngressos);
		}
	}

}
