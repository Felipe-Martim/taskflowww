package br.com.portfolio.taskflowww.controller;

import br.com.portfolio.taskflowww.domain.usuario.DadosAutenticacao;
import br.com.portfolio.taskflowww.domain.usuario.DadosTokenJWT;
import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import br.com.portfolio.taskflowww.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AuthController(
            AuthenticationManager manager,
            TokenService tokenService) {

        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(
            @RequestBody @Valid DadosAutenticacao dados) {

        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        dados.email(),
                        dados.senha()
                );

        var authentication = manager.authenticate(authenticationToken);

        var usuario = (Usuario) authentication.getPrincipal();

        var tokenJWT = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
