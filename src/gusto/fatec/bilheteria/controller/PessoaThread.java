package gusto.fatec.bilheteria.controller;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import static java.lang.Math.random;
import static java.text.MessageFormat.format;

public class PessoaThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(PessoaThread.class.getName());

    private static int qtdeIngressos = 100;

    private int ingressos;

    private Semaphore semaforo;

    public PessoaThread(int pessoa, Semaphore semaforo) {
        this.ingressos = pessoa;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        if (login() && compraIngresso()) {
            try {
                semaforo.acquire();
                validaIngresso();
            } catch (InterruptedException e) {
                e.printStackTrace();
                currentThread().interrupt();
            } finally {
                semaforo.release();
            }
        }
    }

    public boolean login() {
        ingressos = (int) ((random() * 3) + 4);
        int tempo = 50 + (int) (random() * (2000 - 50));

        try {
            sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
            currentThread().interrupt();
        }

        if (tempo < 1000) {
            LOGGER.info("Login efetuado com sucesso");
            return true;
        } else {
            LOGGER.info("Desculpe site lotado tente outra vez ;S");
            return false;
        }
    }

    public boolean compraIngresso() {
        int tempo = 1000 + (int) (random() * (3000 - 1000));

        try {
            sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
            currentThread().interrupt();
        }

        if (tempo >= 2500) {
            LOGGER.info("Desculpe infelizmente seu tempo expirou");
            return false;
        } else {
            return true;
        }
    }

    public void validaIngresso() {
        if (ingressos >= qtdeIngressos) {
            String logCompraNaoConcluida = format(
                    "Infelizmente a quantidade de ingressos em estoque não é o suficiente, ingressos restantes - {0}", qtdeIngressos);

            LOGGER.info(logCompraNaoConcluida);
        } else {
            setQtdeIngressos(qtdeIngressos - ingressos);
            String logCompraConcluida = format(
                    "Parabéns você adquiriu {0} quantidade restante de ingressos : {1}", ingressos, qtdeIngressos);

            LOGGER.info(logCompraConcluida);
        }
    }

    public static int getQtdeIngressos() {
        return qtdeIngressos;
    }

    public static void setQtdeIngressos(int qtdeIngressos) {
        PessoaThread.qtdeIngressos = qtdeIngressos;
    }

}
