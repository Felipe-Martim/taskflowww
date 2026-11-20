package br.com.portfolio.taskflowww.service;

import br.com.portfolio.taskflowww.domain.tarefa.Tarefa;
import br.com.portfolio.taskflowww.domain.tarefa.TarefaNaoEncontradaException;
import br.com.portfolio.taskflowww.repository.TarefaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.portfolio.taskflowww.domain.tarefa.StatusTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.PrioridadeTarefa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void deveriaCadastrarTarefa() {

        // Arrange
        Tarefa tarefa = mock(Tarefa.class);

        when(tarefaRepository.save(tarefa))
                .thenReturn(tarefa);

        // Act
        Tarefa resultado = tarefaService.cadastrar(tarefa);

        // Assert
        assertNotNull(resultado);
        assertEquals(tarefa, resultado);

        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveriaLancarErroQuandoTarefaNaoPertencerAoUsuario() {

        // Arrange
        Long tarefaId = 1L;
        Long usuarioId = 2L;

        when(tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId))
                .thenReturn(java.util.Optional.empty());

        // Act e Assert
        assertThrows(
                br.com.portfolio.taskflowww.domain.tarefa.TarefaNaoEncontradaException.class,
                () -> tarefaService.buscarPorId(tarefaId, usuarioId)
        );

        verify(tarefaRepository).findByIdAndUsuarioId(tarefaId, usuarioId);
    }

    @Test
    void deveriaAtualizarStatusDaTarefa() {

        // Arrange
        Long tarefaId = 1L;
        Long usuarioId = 2L;

        Tarefa tarefa = mock(Tarefa.class);

        when(tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId))
                .thenReturn(java.util.Optional.of(tarefa));

        when(tarefaRepository.save(tarefa))
                .thenReturn(tarefa);

        // Act
        Tarefa resultado = tarefaService.atualizarStatus(
                tarefaId,
                usuarioId,
                br.com.portfolio.taskflowww.domain.tarefa.StatusTarefa.CONCLUIDA
        );

        // Assert
        assertNotNull(resultado);

        verify(tarefa).atualizarStatus(
                br.com.portfolio.taskflowww.domain.tarefa.StatusTarefa.CONCLUIDA
        );

        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveriaExcluirTarefaDoUsuario() {

        // Arrange
        Long tarefaId = 1L;
        Long usuarioId = 2L;

        Tarefa tarefa = mock(Tarefa.class);

        when(tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId))
                .thenReturn(java.util.Optional.of(tarefa));

        // Act
        tarefaService.excluir(tarefaId, usuarioId);

        // Assert
        verify(tarefaRepository).findByIdAndUsuarioId(tarefaId, usuarioId);
        verify(tarefaRepository).delete(tarefa);
    }

    @Test
    void deveriaListarTarefasDoUsuario() {

        // Arrange
        Long usuarioId = 1L;

        when(tarefaRepository.findAllByUsuarioId(usuarioId))
                .thenReturn(java.util.List.of());

        // Act
        java.util.List<Tarefa> resultado =
                tarefaService.listarPorUsuario(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(tarefaRepository).findAllByUsuarioId(usuarioId);
    }

    @Test
    void deveriaAtualizarDadosDaTarefa() {

        // Arrange
        Long tarefaId = 1L;
        Long usuarioId = 2L;

        Tarefa tarefa = mock(Tarefa.class);

        when(tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId))
                .thenReturn(java.util.Optional.of(tarefa));

        when(tarefaRepository.save(tarefa))
                .thenReturn(tarefa);

        // Act
        Tarefa resultado = tarefaService.atualizar(
                tarefaId,
                usuarioId,
                "Estudar Spring Boot",
                "Praticar desenvolvimento de APIs",
                br.com.portfolio.taskflowww.domain.tarefa.PrioridadeTarefa.ALTA,
                null
        );

        // Assert
        assertNotNull(resultado);

        verify(tarefa).atualizar(
                "Estudar Spring Boot",
                "Praticar desenvolvimento de APIs",
                br.com.portfolio.taskflowww.domain.tarefa.PrioridadeTarefa.ALTA,
                null
        );

        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveriaFiltrarTarefasDoUsuario() {

        // Arrange
        Long usuarioId = 1L;

        StatusTarefa status = StatusTarefa.PENDENTE;
        PrioridadeTarefa prioridade = PrioridadeTarefa.ALTA;
        String titulo = "Estudar";

        org.springframework.data.domain.Pageable pageable =
                mock(org.springframework.data.domain.Pageable.class);

        org.springframework.data.domain.Page<Tarefa> pagina =
                mock(org.springframework.data.domain.Page.class);

        when(tarefaRepository.filtrarTarefas(
                usuarioId, status, prioridade, titulo, pageable
        )).thenReturn(pagina);

        // Act
        var resultado = tarefaService.filtrarTarefas(
                usuarioId, status, prioridade, titulo, pageable
        );

        // Assert
        assertSame(pagina, resultado);

        verify(tarefaRepository).filtrarTarefas(
                usuarioId, status, prioridade, titulo, pageable
        );
    }

    @Test
    void deveriaListarTarefasComPaginacao() {

        // Arrange
        Long usuarioId = 1L;

        Pageable pageable = mock(Pageable.class);
        Page<Tarefa> pagina = mock(Page.class);

        when(tarefaRepository.findAllByUsuarioId(usuarioId, pageable))
                .thenReturn(pagina);

        // Act
        Page<Tarefa> resultado =
                tarefaService.listarPorUsuario(usuarioId, pageable);

        // Assert
        assertSame(pagina, resultado);

        verify(tarefaRepository).findAllByUsuarioId(usuarioId, pageable);
    }

    @Test
    void naoDeveriaExcluirTarefaInexistente() {

        // Arrange
        Long tarefaId = 999L;
        Long usuarioId = 1L;

        when(tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId))
                .thenReturn(java.util.Optional.empty());

        // Act e Assert
        assertThrows(
                TarefaNaoEncontradaException.class,
                () -> tarefaService.excluir(tarefaId, usuarioId)
        );

        verify(tarefaRepository, never()).delete(any(Tarefa.class));
    }

    @Test
    void deveriaBuscarTarefaPorIdEUsuario() {

        // Arrange
        Long tarefaId = 1L;
        Long usuarioId = 2L;

        Tarefa tarefa = mock(Tarefa.class);

        when(tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId))
                .thenReturn(java.util.Optional.of(tarefa));

        // Act
        Tarefa resultado = tarefaService.buscarPorId(tarefaId, usuarioId);

        // Assert
        assertNotNull(resultado);
        assertSame(tarefa, resultado);

        verify(tarefaRepository).findByIdAndUsuarioId(tarefaId, usuarioId);
    }


}

