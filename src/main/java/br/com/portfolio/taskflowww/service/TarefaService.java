package br.com.portfolio.taskflowww.service;

import br.com.portfolio.taskflowww.domain.tarefa.PrioridadeTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.StatusTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.Tarefa;
import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import br.com.portfolio.taskflowww.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.portfolio.taskflowww.domain.tarefa.TarefaNaoEncontradaException;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public Tarefa cadastrar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> listarPorUsuario(Long usuarioId) {
        return tarefaRepository.findAllByUsuarioId(usuarioId);
    }

    public Page<Tarefa> listarPorUsuario(Long usuarioId, Pageable pageable) {
        return tarefaRepository.findAllByUsuarioId(usuarioId, pageable);
    }

    public Page<Tarefa> filtrarTarefas(Long usuarioId, StatusTarefa status, PrioridadeTarefa prioridade, String titulo, Pageable pageable) {
        return tarefaRepository.filtrarTarefas(usuarioId, status, prioridade, titulo, pageable);
    }

    public Tarefa buscarPorId(Long id, Long usuarioId) {
        return tarefaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(TarefaNaoEncontradaException::new);
    }

    public void excluir(Long id, Long usuarioId) {
        Tarefa tarefa = buscarPorId(id, usuarioId);
        tarefaRepository.delete(tarefa);
    }

    public Tarefa atualizar(
            Long id,
            Long usuarioId,
            String titulo,
            String descricao,
            br.com.portfolio.taskflowww.domain.tarefa.PrioridadeTarefa prioridade,
            java.time.LocalDateTime dataPrazo) {

        Tarefa tarefa = buscarPorId(id, usuarioId);

        tarefa.atualizar(titulo, descricao, prioridade, dataPrazo);

        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizarStatus(
            Long id,
            Long usuarioId,
            br.com.portfolio.taskflowww.domain.tarefa.StatusTarefa status) {

        Tarefa tarefa = buscarPorId(id, usuarioId);

        tarefa.atualizarStatus(status);

        return tarefaRepository.save(tarefa);
    }
}
