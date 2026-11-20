package br.com.portfolio.taskflowww.domain.usuario;

import java.time.LocalDateTime;

public record DadosDetalhamentoUsuario(
        Long id,
        String nome,
        String email,
        LocalDateTime criadoEm
) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCriadoEm()
        );
    }
}
