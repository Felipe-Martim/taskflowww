package br.com.portfolio.taskflowww.infra.exception;

public record DadosErro(
        int status,
        String erro,
        String mensagem
) {
}

