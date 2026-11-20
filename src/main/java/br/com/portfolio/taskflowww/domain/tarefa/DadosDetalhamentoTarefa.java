package br.com.portfolio.taskflowww.domain.tarefa;

import java.time.LocalDateTime;

public record DadosDetalhamentoTarefa(
        Long id,
        String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        LocalDateTime dataConclusao,
        LocalDateTime dataPrazo
) {

    public DadosDetalhamentoTarefa(Tarefa tarefa) {
        this(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getPrioridade(),
                tarefa.getDataCriacao(),
                tarefa.getDataAtualizacao(),
                tarefa.getDataConclusao(),
                tarefa.getDataPrazo()
        );
    }
}
