package gusto.fatec.bilheteria.thread;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class PessoaThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(PessoaThread.class.getName());
    private static int totalDeIngressos = 100;
    private final Random random = new Random();

    private final int pessoa;
    private final int ingressos;
    private final Semaphore semaforo;

    public PessoaThread(int pessoa, int ingressos, Semaphore semaforo) {
        this.pessoa = pessoa;
        this.ingressos = ingressos;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        fazerLogin();
    }

    private void fazerLogin() {
        int tempo = random.nextInt(1951) + 50;

        executar(tempo, "Erro ao tentar fazer login");

        int tempoMaximo = 1000;
        String mensagemSucesso = "Olá pessoa " + pessoa + ", login efetuado com sucesso";
        String mensagemErro = "Desculpe, " + pessoa + " site lotado, tente outra vez";
        if (retorno(tempo, tempoMaximo, mensagemSucesso, mensagemErro)) {
            comprarIngresso();
        }
    }

    private void comprarIngresso() {
        int tempo = random.nextInt(2001) + 1000;

        executar(tempo, "Erro ao tentar realizar a compra");

        int tempoMaximo = 2500;
        String mensagemSucesso = "Pessoa " + pessoa + ", estamos validando a sua compra";
        String mensagemErro = "Desculpe Pessoa " + pessoa + ", infelizmente seu tempo expirou";
        if (retorno(tempo, tempoMaximo, mensagemSucesso, mensagemErro)) {
            validarIngresso();
        }
    }

    private void validarIngresso() {
        try {
            semaforo.acquire();
            realizarValidacao();
        } catch (InterruptedException e) {
            LOGGER.warning("Erro durante a validação");
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }
    }

    private void realizarValidacao() {
        String mensgem;

        if (ingressos > getTotalDeIngressos()) {
            mensgem = MessageFormat.format(
                    "Infelizmente a quantidade de ingressos em estoque não é o suficiente, ingressos restantes - {0}",
                    totalDeIngressos
            );

            LOGGER.info(mensgem);
            return;
        }

        validarCompraDoIngresso(ingressos);

        mensgem = MessageFormat.format(
                "Parabéns você adquiriu {0} ingresso(s). Quantidade restante de ingressos : {1}",
                ingressos,
                getTotalDeIngressos()
        );

        LOGGER.info(mensgem);
    }

    private void executar(int tempo, String mensagemErro) {
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            LOGGER.warning(mensagemErro);
            Thread.currentThread().interrupt();
        }
    }

    private boolean retorno(int tempo, int tempoMaximo, String mensagemSucesso, String mensagemErro) {
        boolean condicao = tempo > tempoMaximo;

        String menssagem = condicao
                ? mensagemErro
                : mensagemSucesso;

        LOGGER.info(menssagem);

        return condicao;
    }

    private static int getTotalDeIngressos() {
        return totalDeIngressos;
    }

    private static void validarCompraDoIngresso(int quantidade) {
        totalDeIngressos = totalDeIngressos - quantidade;
    }

}