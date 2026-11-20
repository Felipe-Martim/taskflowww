package br.com.portfolio.taskflowww.service;

import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import br.com.portfolio.taskflowww.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveriaCadastrarUsuarioComSenhaCriptografada() {

        // Arrange
        String nome = "Felipe";
        String email = "felipe@email.com";
        String senha = "12345678";
        String senhaCriptografada = "senha-criptografada";

        when(usuarioRepository.existsByEmail(email))
                .thenReturn(false);

        when(passwordEncoder.encode(senha))
                .thenReturn(senhaCriptografada);

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario usuario = usuarioService.cadastrar(
                nome,
                email,
                senha
        );

        // Assert
        assertNotNull(usuario);
        assertEquals(nome, usuario.getNome());
        assertEquals(email, usuario.getEmail());
        assertEquals(senhaCriptografada, usuario.getSenha());

        verify(passwordEncoder).encode(senha);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveriaImpedirCadastroComEmailDuplicado() {

        // Arrange
        String email = "felipe@email.com";

        when(usuarioRepository.existsByEmail(email))
                .thenReturn(true);

        // Act e Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.cadastrar(
                        "Felipe",
                        email,
                        "12345678"
                )
        );

        assertEquals("E-mail já cadastrado!", exception.getMessage());

        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
    @Test
    void deveriaLancarExcecaoQuandoFalharAoSalvarUsuario() {

        // Arrange
        String nome = "Felipe";
        String email = "felipe@email.com";
        String senha = "12345678";

        when(usuarioRepository.existsByEmail(email))
                .thenReturn(false);

        when(passwordEncoder.encode(senha))
                .thenReturn("senha-criptografada");

        when(usuarioRepository.save(any(Usuario.class)))
                .thenThrow(new RuntimeException("Erro ao salvar usuário!"));

        // Act e Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.cadastrar(nome, email, senha)
        );

        assertEquals("Erro ao salvar usuário!", exception.getMessage());

        verify(usuarioRepository).save(any(Usuario.class));
    }

}
