package br.com.portfolio.taskflowww.domain.tarefa;

public class TarefaNaoEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaException() {
        super("Tarefa não encontrada!");
    }
}
