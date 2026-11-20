package br.com.portfolio.taskflowww.controller;

import br.com.portfolio.taskflowww.config.SecurityConfig;
import br.com.portfolio.taskflowww.domain.tarefa.PrioridadeTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.StatusTarefa;
import br.com.portfolio.taskflowww.domain.tarefa.Tarefa;
import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import br.com.portfolio.taskflowww.infra.security.TokenService;
import br.com.portfolio.taskflowww.repository.UsuarioRepository;
import br.com.portfolio.taskflowww.service.TarefaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.data.domain.Page;

@WebMvcTest(TarefaController.class)
@Import(SecurityConfig.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TarefaService tarefaService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    void deveriaCadastrarTarefaComSucesso() throws Exception {

        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        when(tarefaService.cadastrar(any(Tarefa.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var dados = """
                {
                    "titulo": "Estudar Spring Boot",
                    "descricao": "Finalizar os testes automatizados do TaskFlow",
                    "prioridade": "ALTA",
                    "dataPrazo": "2026-11-20T18:00:00"
                }
                """;

        mockMvc.perform(post("/tarefas")
                        .with(authentication(autenticacao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dados))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo")
                        .value("Estudar Spring Boot"))
                .andExpect(jsonPath("$.descricao")
                        .value("Finalizar os testes automatizados do TaskFlow"))
                .andExpect(jsonPath("$.status")
                        .value("PENDENTE"))
                .andExpect(jsonPath("$.prioridade")
                        .value("ALTA"));

        verify(tarefaService).cadastrar(any(Tarefa.class));
    }
    @Test
    void deveriaListarTarefasDoUsuarioAutenticado() throws Exception {

        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        when(tarefaService.filtrarTarefas(
                any(),
                isNull(),
                isNull(),
                isNull(),
                any()
        )).thenReturn(Page.empty());

        mockMvc.perform(get("/tarefas")
                        .with(authentication(autenticacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(tarefaService).filtrarTarefas(
                any(),
                isNull(),
                isNull(),
                isNull(),
                any()
        );
    }
    @Test
    void deveriaBuscarTarefaPorId() throws Exception {
        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        Tarefa tarefa = new Tarefa(
                "Estudar Spring Boot",
                "Estudar testes automatizados",
                PrioridadeTarefa.ALTA,
                null,
                usuario
        );

        when(tarefaService.buscarPorId(1L, usuario.getId()))
                .thenReturn(tarefa);

        mockMvc.perform(get("/tarefas/1")
                        .with(authentication(autenticacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Estudar Spring Boot"))
                .andExpect(jsonPath("$.descricao").value("Estudar testes automatizados"))
                .andExpect(jsonPath("$.prioridade").value("ALTA"));

        verify(tarefaService).buscarPorId(1L, usuario.getId());
    }
    @Test
    void deveriaAtualizarStatusDaTarefa() throws Exception {
        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        Tarefa tarefa = new Tarefa(
                "Estudar Spring Boot",
                "Finalizar os testes automatizados",
                PrioridadeTarefa.ALTA,
                null,
                usuario
        );

        when(tarefaService.atualizarStatus(
                1L,
                usuario.getId(),
                StatusTarefa.CONCLUIDA
        )).thenAnswer(invocation -> {
            tarefa.atualizarStatus(invocation.getArgument(2));
            return tarefa;
        });

        mockMvc.perform(patch("/tarefas/1/status")
                        .with(authentication(autenticacao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "status": "CONCLUIDA"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));

        verify(tarefaService).atualizarStatus(
                1L,
                usuario.getId(),
                StatusTarefa.CONCLUIDA
        );
    }
    @Test
    void deveriaAtualizarTarefaComSucesso() throws Exception {
        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        Tarefa tarefa = new Tarefa(
                "Estudar Java",
                "Revisar orientação a objetos",
                PrioridadeTarefa.MEDIA,
                null,
                usuario
        );

        when(tarefaService.atualizar(
                eq(1L),
                eq(usuario.getId()),
                eq("Estudar Spring Boot"),
                eq("Estudar testes automatizados"),
                eq(PrioridadeTarefa.ALTA),
                isNull()
        )).thenAnswer(invocation -> {
            tarefa.atualizar(
                    invocation.getArgument(2),
                    invocation.getArgument(3),
                    invocation.getArgument(4),
                    invocation.getArgument(5)
            );
            return tarefa;
        });

        mockMvc.perform(put("/tarefas/1")
                        .with(authentication(autenticacao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "titulo": "Estudar Spring Boot",
                                "descricao": "Estudar testes automatizados",
                                "prioridade": "ALTA",
                                "dataPrazo": null
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Estudar Spring Boot"))
                .andExpect(jsonPath("$.descricao").value("Estudar testes automatizados"))
                .andExpect(jsonPath("$.prioridade").value("ALTA"));

        verify(tarefaService).atualizar(
                eq(1L),
                eq(usuario.getId()),
                eq("Estudar Spring Boot"),
                eq("Estudar testes automatizados"),
                eq(PrioridadeTarefa.ALTA),
                isNull()
        );
    }
    @Test
    void deveriaExcluirTarefaComSucesso() throws Exception {
        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        doNothing().when(tarefaService).excluir(1L, usuario.getId());

        mockMvc.perform(delete("/tarefas/1")
                        .with(authentication(autenticacao)))
                .andExpect(status().isNoContent());

        verify(tarefaService).excluir(1L, usuario.getId());
    }
    @Test
    void naoDeveriaListarTarefasSemAutenticacao() throws Exception {

        mockMvc.perform(get("/tarefas"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(tarefaService);
    }
    @Test
    void naoDeveriaCadastrarTarefaComTituloVazio() throws Exception {
        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        mockMvc.perform(post("/tarefas")
                        .with(authentication(autenticacao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "titulo": "",
                                "descricao": "Estudar testes automatizados",
                                "prioridade": "ALTA",
                                "dataPrazo": null
                            }
                            """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tarefaService);
    }
    @Test
    void naoDeveriaCadastrarTarefaSemPrioridade() throws Exception {
        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        mockMvc.perform(post("/tarefas")
                        .with(authentication(autenticacao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "titulo": "Estudar Spring Boot",
                                "descricao": "Estudar testes automatizados",
                                "prioridade": null,
                                "dataPrazo": null
                            }
                            """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tarefaService);
    }
    @Test
    void naoDeveriaCadastrarTarefaComDescricaoVazia() throws Exception {
        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities()
        );

        mockMvc.perform(post("/tarefas")
                        .with(authentication(autenticacao))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "titulo": "Estudar Spring Boot",
                                "descricao": "",
                                "prioridade": "ALTA",
                                "dataPrazo": null
                            }
                            """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(tarefaService);
    }
}
