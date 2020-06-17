package gusto.fatec.bilheteria.view;

import java.util.concurrent.Semaphore;

import gusto.fatec.bilheteria.controller.pessoaThread;

public class Main {

	public static void main(String[] args) {
		Semaphore semaforo = new Semaphore(1);
		
		for (int i = 0; i < 300; i++) {
			Thread compraIngressos = new pessoaThread(i,semaforo);
			compraIngressos.start();
		}
	}

}
