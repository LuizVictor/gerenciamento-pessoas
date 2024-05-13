package br.com.luizvictor.gerenciamentopessoas.entities.pessoa;

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
}