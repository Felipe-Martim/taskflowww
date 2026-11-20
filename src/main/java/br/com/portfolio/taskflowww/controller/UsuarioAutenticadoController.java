package br.com.portfolio.taskflowww.controller;

import br.com.portfolio.taskflowww.domain.usuario.DadosDetalhamentoUsuario;
import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioAutenticadoController {

    @GetMapping("/me")
    public DadosDetalhamentoUsuario autenticado(
            @AuthenticationPrincipal Usuario usuario) {

        return new DadosDetalhamentoUsuario(usuario);
    }
}
