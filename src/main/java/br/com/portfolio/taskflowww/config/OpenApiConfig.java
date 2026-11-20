package br.com.portfolio.taskflowww.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TaskFlow API",
                version = "1.0.0",
                description = """
                        API REST para gerenciamento de tarefas.
                        
                        Permite cadastrar usuários, autenticar com JWT,
                        criar tarefas, listar, atualizar e excluir tarefas.
                        """
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Informe o token JWT para autenticação."
)
public class OpenApiConfig {

}

