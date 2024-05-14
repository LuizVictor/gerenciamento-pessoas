package br.com.luizvictor.gerenciamentopessoas.entities.pessoa;

import br.com.luizvictor.gerenciamentopessoas.entities.endereco.Endereco;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pessoas")
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    @OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Endereco> enderecos = new ArrayList<>();
    private Long enderecoPrincipal;

    public Pessoa() { }

    public Pessoa(Long id, String nome, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public Long getEnderecoPrincipal() {
        return enderecoPrincipal;
    }

    public void editar(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public void adicionarEndereco(Endereco endereco) {
        endereco.adicionarPessoa(this);
        this.enderecos.add(endereco);
    }

    public void adicionaEnderecoPrincipal(Long enderecoId) {
        this.enderecoPrincipal = enderecoId;
    }
}
