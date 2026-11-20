package br.com.portfolio.taskflowww.controller;

import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import br.com.portfolio.taskflowww.service.UsuarioService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import br.com.portfolio.taskflowww.infra.security.TokenService;
import br.com.portfolio.taskflowww.repository.UsuarioRepository;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import br.com.portfolio.taskflowww.config.SecurityConfig;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@WebMvcTest(UsuarioController.class)
@Import(SecurityConfig.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    void deveriaCadastrarUsuarioComSucesso() throws Exception {

        Usuario usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        when(usuarioService.cadastrar(
                anyString(),
                anyString(),
                anyString()
        )).thenAnswer(invocation -> {
            try {
                var campoId = Usuario.class.getDeclaredField("id");
                campoId.setAccessible(true);
                campoId.set(usuario, 1L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return usuario;
        });

        mockMvc.perform(post("/usuarios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "Felipe",
                                    "email": "felipe@email.com",
                                    "senha": "12345678"
                                }
                                """))
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName("Deveria retornar 400 quando o email for inválido")
    void cadastrarUsuarioCenario2() throws Exception {

        var dados = """
            {
                "nome": "Felipe",
                "email": "email-invalido",
                "senha": "12345678"
            }
            """;

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dados))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never())
                .cadastrar(any(), any(), any());
    }
    @Test
    @DisplayName("Deveria retornar 400 quando o nome estiver vazio")
    void cadastrarUsuarioCenario3() throws Exception {

        var dados = """
            {
                "nome": "",
                "email": "felipe@email.com",
                "senha": "12345678"
            }
            """;

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dados))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never())
                .cadastrar(any(), any(), any());
    }
    @Test
    @DisplayName("Deveria retornar 400 quando a senha tiver menos de 8 caracteres")
    void cadastrarUsuarioCenario4() throws Exception {

        var dados = """
            {
                "nome": "Felipe",
                "email": "felipe@email.com",
                "senha": "12345"
            }
            """;

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dados))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never())
                .cadastrar(any(), any(), any());
    }
    @Test
    @DisplayName("Deveria cadastrar um usuário com dados válidos")
    void cadastrarUsuarioCenario5() throws Exception {

        var dados = """
            {
                "nome": "Felipe",
                "email": "felipe@email.com",
                "senha": "12345678"
            }
            """;

        var usuario = new Usuario(
                "Felipe",
                "felipe@email.com",
                "senha-criptografada"
        );

        var campoId = Usuario.class.getDeclaredField("id");
        campoId.setAccessible(true);
        campoId.set(usuario, 1L);

        when(usuarioService.cadastrar(
                "Felipe",
                "felipe@email.com",
                "12345678"
        )).thenReturn(usuario);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dados))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/usuarios/1"
                ));

        verify(usuarioService).cadastrar(
                "Felipe",
                "felipe@email.com",
                "12345678"
        );
    }
}

