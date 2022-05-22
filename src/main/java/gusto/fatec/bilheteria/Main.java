package gusto.fatec.bilheteria;

import gusto.fatec.bilheteria.thread.PessoaThread;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        Semaphore semaforo = new Semaphore(1);

        for (int i = 0; i < 300; i++) {
            int ingressosSolicitados = random.nextInt(4) + 1;
            Thread compraIngressos = new PessoaThread(i, ingressosSolicitados, semaforo);
            compraIngressos.start();
        }
    }

}
