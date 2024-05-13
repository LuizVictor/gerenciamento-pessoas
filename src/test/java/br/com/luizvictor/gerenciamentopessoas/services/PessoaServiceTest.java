package br.com.luizvictor.gerenciamentopessoas.services;

import br.com.luizvictor.gerenciamentopessoas.entities.pessoa.Pessoa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PessoaServiceTest {
    @Autowired
    private PessoaService pessoaService;

    @Test
    @DisplayName("Deve salvar pessoa")
    void deveSalvarPessoa() {
        Pessoa pessoa = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa result = pessoaService.salvar(pessoa);

        assertNotNull(result);
    }
}