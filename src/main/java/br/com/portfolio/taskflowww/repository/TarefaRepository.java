package br.com.portfolio.taskflowww.repository;

import br.com.portfolio.taskflowww.domain.tarefa.Tarefa;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.portfolio.taskflowww.domain.tarefa.StatusTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.PrioridadeTarefa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findAllByUsuarioId(Long usuarioId);

    Optional<Tarefa> findByIdAndUsuarioId(Long id, Long usuarioId);

    Page<Tarefa> findAllByUsuarioId(Long usuarioId, Pageable pageable);

    Page<Tarefa> findAllByUsuarioIdAndStatus(Long usuarioId, StatusTarefa status, Pageable pageable);

    Page<Tarefa> findAllByUsuarioIdAndPrioridade(Long usuarioId, PrioridadeTarefa prioridade, Pageable pageable);

    @Query("""
    SELECT t FROM Tarefa t
    WHERE t.usuario.id = :usuarioId
    AND (:status IS NULL OR t.status = :status)
    AND (:prioridade IS NULL OR t.prioridade = :prioridade)
    AND (
        COALESCE(:titulo, '') = ''
        OR LOWER(t.titulo) LIKE LOWER(CONCAT('%', COALESCE(:titulo, ''), '%'))
    )
    """)
    Page<Tarefa> filtrarTarefas(
            @Param("usuarioId") Long usuarioId,
            @Param("status") StatusTarefa status,
            @Param("prioridade") PrioridadeTarefa prioridade,
            @Param("titulo") String titulo,
            Pageable pageable
    );

}
