package br.com.luizvictor.gerenciamentopessoas.entities.pessoa;

import br.com.luizvictor.gerenciamentopessoas.entities.endereco.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {
    private Pessoa pessoa;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        pessoa = new Pessoa(1L, "John Doe", LocalDate.of(1999, 5, 13));
        endereco = new Endereco(
                1L,
                "Rua A",
                "44000-000",
                10,
                "Feira de Santana",
                "Bahia"
        );
    }

    @Test
    @DisplayName("Deve criar uma pessoa")
    void deveCriarUmaPessoa() {
        assertEquals(1L, pessoa.getId());
        assertEquals("John Doe", pessoa.getNome());
        assertEquals("1999-05-13", pessoa.getDataNascimento().toString());
    }

    @Test
    void testandoConstrutorDefault() {
        Pessoa pessoaDefault = new Pessoa();
        assertNotNull(pessoaDefault);
    }

    @Test
    @DisplayName("Deve editar dados de uma pessoa")
    void deveEditarPessoa() {
        pessoa.editar("Jannet Doe", LocalDate.of(2000, 5, 13));

        assertEquals(1L, pessoa.getId());
        assertEquals("Jannet Doe", pessoa.getNome());
        assertEquals("2000-05-13", pessoa.getDataNascimento().toString());
    }

    @Test
    @DisplayName("Deve adicionar endereço")
    void deveAdicionarEndereco() {
        Pessoa pessoa = new Pessoa(1L, "John Doe", LocalDate.of(1999, 5, 13));

        pessoa.adicionarEndereco(endereco);

        assertEquals(1, pessoa.getEnderecos().size());
    }

    @Test
    @DisplayName("Deve adicionar endereço como principal")
    void deveAdicionarEnderecoPrincipal() {
        Pessoa pessoa = new Pessoa(1L, "John Doe", LocalDate.of(1999, 5, 13));

        pessoa.adicionarEndereco(endereco);
        pessoa.adicionaEnderecoPrincipal(endereco.getId());

        assertEquals(1, pessoa.getEnderecos().size());
        assertEquals(pessoa.getEnderecoPrincipal(), endereco.getId());
    }
}