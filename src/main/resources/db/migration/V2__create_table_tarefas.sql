CREATE TABLE tarefas (
    id BIGSERIAL PRIMARY KEY,

    titulo VARCHAR(150) NOT NULL,

    descricao TEXT NOT NULL,

    status VARCHAR(20) NOT NULL,

    prioridade VARCHAR(20) NOT NULL,

    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    data_atualizacao TIMESTAMP,

    data_conclusao TIMESTAMP,

    data_prazo TIMESTAMP,

    usuario_id BIGINT NOT NULL,

    CONSTRAINT fk_tarefas_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

CREATE INDEX idx_tarefas_usuario
    ON tarefas(usuario_id);

CREATE INDEX idx_tarefas_status
    ON tarefas(status);

CREATE INDEX idx_tarefas_prazo
    ON tarefas(data_prazo);
