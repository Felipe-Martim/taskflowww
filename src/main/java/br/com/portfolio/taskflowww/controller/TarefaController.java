package br.com.portfolio.taskflowww.controller;

import br.com.portfolio.taskflowww.domain.tarefa.DadosCadastroTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.DadosDetalhamentoTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.Tarefa;
import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import br.com.portfolio.taskflowww.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import br.com.portfolio.taskflowww.domain.tarefa.DadosAtualizacaoTarefa;
import org.springframework.web.bind.annotation.PutMapping;
import br.com.portfolio.taskflowww.domain.tarefa.DadosAtualizacaoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import br.com.portfolio.taskflowww.domain.tarefa.StatusTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.PrioridadeTarefa;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/tarefas")
@SecurityRequirement(name = "bearerAuth")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosDetalhamentoTarefa cadastrar(
            @RequestBody @Valid DadosCadastroTarefa dados,
            @AuthenticationPrincipal Usuario usuario) {

        Tarefa tarefa = new Tarefa(
                dados.titulo(),
                dados.descricao(),
                dados.prioridade(),
                dados.dataPrazo(),
                usuario
        );

        Tarefa tarefaSalva = tarefaService.cadastrar(tarefa);

        return new DadosDetalhamentoTarefa(tarefaSalva);
    }

    @GetMapping
    public Page<DadosDetalhamentoTarefa> listar(
            @AuthenticationPrincipal Usuario usuario,

            @RequestParam(required = false)
            StatusTarefa status,

            @RequestParam(required = false)
            PrioridadeTarefa prioridade,

            @RequestParam(required = false)
            String titulo,

            @ParameterObject
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {

        return tarefaService.filtrarTarefas(
                usuario.getId(),
                status,
                prioridade,
                titulo,
                pageable
        ).map(DadosDetalhamentoTarefa::new);
    }

    @GetMapping("/{id}")
    public DadosDetalhamentoTarefa buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        Tarefa tarefa = tarefaService.buscarPorId(id, usuario.getId());

        return new DadosDetalhamentoTarefa(tarefa);
    }

    @PutMapping("/{id}")
    public DadosDetalhamentoTarefa atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTarefa dados,
            @AuthenticationPrincipal Usuario usuario) {

        Tarefa tarefa = tarefaService.atualizar(
                id,
                usuario.getId(),
                dados.titulo(),
                dados.descricao(),
                dados.prioridade(),
                dados.dataPrazo()
        );

        return new DadosDetalhamentoTarefa(tarefa);
    }

    @PatchMapping("/{id}/status")
    public DadosDetalhamentoTarefa atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoStatus dados,
            @AuthenticationPrincipal Usuario usuario) {

        Tarefa tarefa = tarefaService.atualizarStatus(
                id,
                usuario.getId(),
                dados.status()
        );

        return new DadosDetalhamentoTarefa(tarefa);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        tarefaService.excluir(id, usuario.getId());
    }
}

