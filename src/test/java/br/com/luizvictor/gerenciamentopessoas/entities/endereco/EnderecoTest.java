package br.com.luizvictor.gerenciamentopessoas.entities.endereco;

import br.com.luizvictor.gerenciamentopessoas.entities.pessoa.Pessoa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {
    @Test
    @DisplayName("Criando endereco")
    void deveCriarEndereco() {
        Endereco endereco = new Endereco(
                1L,
                "Rua A",
                "44000-000",
                10,
                "Feira de Santana",
                "Bahia"
        );

        assertEquals(1L, endereco.getId());
        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals("44000-000", endereco.getCep());
        assertEquals(10, endereco.getNumero());
        assertEquals("Feira de Santana", endereco.getCidade());
        assertEquals("Bahia", endereco.getEstado());
        assertNull(endereco.getPessoa());
    }

    @Test
    @DisplayName("Criando pessoa com construtor default")
    void deveUsarConstrutorDefault() {
        Endereco endereco = new Endereco();
        assertNotNull(endereco);
    }

    @Test
    @DisplayName("Adicionando pessoa")
    void deveAdicionarPessoa() {
        Endereco endereco = new Endereco(
                1L,
                "Rua A",
                "44000-000",
                10,
                "Feira de Santana",
                "Bahia"
        );

        Pessoa pessoa = new Pessoa(1L, "John Doe", LocalDate.of(1999, 5, 13));
        endereco.adicionarPessoa(pessoa);

        assertNotNull(endereco.getPessoa());
        assertEquals("John Doe", endereco.getPessoa().getNome());
        assertEquals("1999-05-13", endereco.getPessoa().getDataNascimento().toString());
    }
}