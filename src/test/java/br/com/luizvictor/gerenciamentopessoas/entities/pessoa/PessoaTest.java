package br.com.luizvictor.gerenciamentopessoas.entities.pessoa;

import br.com.luizvictor.gerenciamentopessoas.entities.endereco.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PessoaTest {
    @Test
    @DisplayName("Deve criar uma pessoa")
    void deveCriarUmaPessoa() {
        Pessoa pessoa = new Pessoa(1L, "John Doe", LocalDate.of(1999, 5, 13));

        assertEquals(1L, pessoa.getId());
        assertEquals("John Doe", pessoa.getNome());
        assertEquals("1999-05-13", pessoa.getDataNascimento().toString());
    }

    @Test
    void testandoConstrutorDefault() {
        Pessoa pessoa = new Pessoa();
        assertNotNull(pessoa);
    }

    @Test
    @DisplayName("Deve editar dados de uma pessoa")
    void deveEditarPessoa() {
        Pessoa pessoa = new Pessoa(1L, "John Doe", LocalDate.of(1999, 5, 13));
        pessoa.editar("Jannet Doe", LocalDate.of(2000, 5, 13));

        assertEquals(1L, pessoa.getId());
        assertEquals("Jannet Doe", pessoa.getNome());
        assertEquals("2000-05-13", pessoa.getDataNascimento().toString());
    }

    @Test
    @DisplayName("Deve adicionar enderco")
    void deveAdicionarEndereco() {
        Pessoa pessoa = new Pessoa(1L, "John Doe", LocalDate.of(1999, 5, 13));
        Endereco endereco = new Endereco(
                1L,
                "Rua A",
                "44000-000",
                10,
                "Feira de Santana",
                "Bahia"
        );

        pessoa.adicionarEndereco(endereco);

        assertEquals(1, pessoa.getEnderecos().size());
    }
}