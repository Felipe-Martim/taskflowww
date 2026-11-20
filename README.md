# TaskFlow — API de Gerenciamento de Tarefas

API REST desenvolvida em Java e Spring Boot para gerenciamento de tarefas pessoais, com autenticação JWT, controle de acesso por usuário e persistência em PostgreSQL.

O projeto foi desenvolvido como parte do meu portfólio, com foco em boas práticas de desenvolvimento backend, organização em camadas, segurança e testes automatizados.

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Spring Data JPA / Hibernate**
- **PostgreSQL 17**
- **Flyway**
- **Maven**
- **Docker e Docker Compose**
- **JUnit e Mockito**
- **Swagger / OpenAPI**
- **Insomnia** para testes das requisições HTTP

## Funcionalidades

- Cadastro de usuários.
- Autenticação com e-mail e senha.
- Geração e validação de tokens JWT.
- Cadastro de tarefas.
- Listagem de tarefas com filtros e paginação.
- Consulta de tarefa por ID.
- Atualização de tarefas.
- Alteração do status de uma tarefa.
- Exclusão de tarefas.
- Controle de acesso: cada usuário acessa somente suas próprias tarefas.
- Validação dos dados recebidos pela API.
- Tratamento centralizado de exceções.

## Endpoints

### Usuários e autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/usuarios` | Cadastrar usuário |
| POST | `/login` | Autenticar e obter token JWT |

### Tarefas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/tarefas` | Criar tarefa |
| GET | `/tarefas` | Listar tarefas |
| GET | `/tarefas/{id}` | Consultar tarefa |
| PUT | `/tarefas/{id}` | Atualizar tarefa |
| PATCH | `/tarefas/{id}/status` | Atualizar status |
| DELETE | `/tarefas/{id}` | Excluir tarefa |

As rotas protegidas exigem o envio do token JWT no cabeçalho:

`Authorization: Bearer SEU_TOKEN`

## Modelo de tarefa

Exemplo de requisição para cadastrar uma tarefa:

```json
{
  "titulo": "Estudar Spring Boot",
  "descricao": "Finalizar a documentação da API TaskFlow",
  "prioridade": "ALTA",
  "dataPrazo": "2026-11-20T18:00:00"
}
```

### Prioridades disponíveis

- BAIXA
- MEDIA
- ALTA
- URGENTE

### Status disponíveis

- PENDENTE
- EM_ANDAMENTO
- CONCLUIDA
- CANCELADA

## Como executar o projeto

### Pré-requisitos

- JDK 21 ou superior.
- PostgreSQL 17.
- Docker e Docker Compose (opcionais, para executar o banco em container).
- Git.

### 1. Clonar o repositório

```bash
git clone URL_DO_REPOSITORIO
cd taskflowww
```

### 2. Configurar as variáveis de ambiente

Crie o arquivo `.env` a partir do exemplo:

```bash
cp .env.example .env
```

Configure os valores de acordo com seu ambiente:

```dotenv
POSTGRES_USER=taskflow_user
POSTGRES_PASSWORD=sua_senha_forte
POSTGRES_DB=taskflow
POSTGRES_PORT=5433
```

Também configure as variáveis `DB_PASSWORD` e `JWT_SECRET` no ambiente de execução da aplicação.

- `DB_PASSWORD`: senha do usuário do PostgreSQL.
- `JWT_SECRET`: chave secreta utilizada para assinar os tokens JWT.

**Importante:** nunca publique o arquivo `.env` ou suas credenciais no GitHub.

### 3. Iniciar o banco com Docker

```bash
docker compose up -d
```

O PostgreSQL ficará disponível na porta `5433` do computador, conforme o `compose.yaml`.

**Atenção:** a configuração atual do `application.properties` utiliza a porta `5432`. Caso utilize o PostgreSQL iniciado pelo Docker Compose deste projeto, ajuste a URL para `jdbc:postgresql://localhost:5433/taskflow`.

### 4. Executar a aplicação

No Linux ou WSL:

```bash
export DB_PASSWORD="sua_senha"
export JWT_SECRET="sua_chave_secreta"
./mvnw spring-boot:run
```

No Windows:

```powershell
$env:DB_PASSWORD="sua_senha"
$env:JWT_SECRET="sua_chave_secreta"
.\mvnw.cmd spring-boot:run
```

A aplicação será iniciada na porta padrão `8080`.

## Documentação da API

Com a aplicação em execução, acesse:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testes automatizados

Para executar os testes:

Linux / WSL:

```bash
./mvnw clean test
```

Windows:

```powershell
.\mvnw.cmd clean test
```

O projeto possui **29 testes automatizados**, que passaram com sucesso durante a validação do desenvolvimento.

## Segurança

A API utiliza Spring Security e autenticação JWT.

As senhas dos usuários são armazenadas com criptografia BCrypt. As rotas protegidas exigem um token válido, e as operações com tarefas respeitam o usuário autenticado.

As credenciais do banco de dados e a chave JWT são fornecidas por variáveis de ambiente.

## Autor

Desenvolvido por **Felipe Martim de Souza**.

Projeto de portfólio — Sistemas de Informação.

---

*Projeto desenvolvido para praticar e demonstrar conhecimentos em desenvolvimento backend com Java e Spring Boot.*
