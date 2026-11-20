package br.com.portfolio.taskflowww.domain.tarefa;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoStatus(

        @NotNull
        StatusTarefa status

) {
}
