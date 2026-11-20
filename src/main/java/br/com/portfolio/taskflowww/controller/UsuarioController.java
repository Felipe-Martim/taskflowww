package br.com.portfolio.taskflowww.controller;

import br.com.portfolio.taskflowww.domain.usuario.DadosCadastroUsuario;
import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import br.com.portfolio.taskflowww.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder) {

        Usuario usuario = usuarioService.cadastrar(
                dados.nome(),
                dados.email(),
                dados.senha()
        );

        var uri = uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
