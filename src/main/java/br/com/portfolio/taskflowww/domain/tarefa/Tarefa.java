package br.com.portfolio.taskflowww.domain.tarefa;

import br.com.portfolio.taskflowww.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTarefa status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadeTarefa prioridade;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "data_prazo")
    private LocalDateTime dataPrazo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Tarefa() {
    }

    public Tarefa(
            String titulo,
            String descricao,
            PrioridadeTarefa prioridade,
            LocalDateTime dataPrazo,
            Usuario usuario) {

        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.dataPrazo = dataPrazo;
        this.usuario = usuario;
        this.status = StatusTarefa.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public PrioridadeTarefa getPrioridade() {
        return prioridade;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public LocalDateTime getDataPrazo() {
        return dataPrazo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void atualizar(
            String titulo,
            String descricao,
            PrioridadeTarefa prioridade,
            LocalDateTime dataPrazo) {

        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.dataPrazo = dataPrazo;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void atualizarStatus(StatusTarefa novoStatus) {
        this.status = novoStatus;
        this.dataAtualizacao = LocalDateTime.now();

        if (novoStatus == StatusTarefa.CONCLUIDA) {
            this.dataConclusao = LocalDateTime.now();
        } else {
            this.dataConclusao = null;
        }
    }
}
