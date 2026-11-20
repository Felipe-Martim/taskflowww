package br.com.portfolio.taskflowww.domain.tarefa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record DadosCadastroTarefa(

        @NotBlank
        @Size(max = 150)
        String titulo,

        @NotBlank
        String descricao,

        @NotNull
        PrioridadeTarefa prioridade,

        LocalDateTime dataPrazo

) {
}
