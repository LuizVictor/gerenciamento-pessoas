package br.com.luizvictor.gerenciamentopessoas.services;

import br.com.luizvictor.gerenciamentopessoas.entities.endereco.Endereco;
import br.com.luizvictor.gerenciamentopessoas.entities.pessoa.Pessoa;
import br.com.luizvictor.gerenciamentopessoas.repositories.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PessoaServiceTest {
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private PessoaRepository pessoaRepository;

    @AfterEach
    void tearDown() {
        pessoaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar pessoa")
    void deveSalvarPessoa() {
        Pessoa pessoa = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa result = pessoaService.salvar(pessoa);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve retornar todas pessoas salvas")
    void deveRetornarTodasPessoaSalvas() {
        Pessoa pessoa1 = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa pessoa2 = new Pessoa(null, "Janet Doe", LocalDate.of(2000, 5, 13));
        pessoaRepository.saveAll(List.of(pessoa1, pessoa2));
        List<Pessoa> result = pessoaService.buscarTodas();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.getFirst().getNome());
        assertEquals("Janet Doe", result.getLast().getNome());
    }

    @Test
    @DisplayName("Deve lancar EntityNotFoundException em caso de lista vazia")
    void deveLancarExcecao() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> pessoaService.buscarTodas());

        String expected = "Nenhuma pessoa salva";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Deve buscar pessoa por id")
    void deveBuscarPessoaPorId() {
        Pessoa pessoa1 = new Pessoa(1L, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa pessoa2 = new Pessoa(null, "Janet Doe", LocalDate.of(2000, 5, 13));
        List<Pessoa> pessoas = pessoaRepository.saveAll(List.of(pessoa1, pessoa2));
        Pessoa result = pessoaService.buscarPorId(pessoas.getFirst().getId());

        assertNotNull(result);
        assertEquals("John Doe", result.getNome());
    }

    @Test
    @DisplayName("Deve lancar EntityNotFoundException em caso de id nao encontrado")
    void deveLancarExcecaoEmCasoDeIdNaoEncontrado() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> pessoaService.buscarPorId(1L));

        String expected = "Nenhuma pessoa encontrada";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Deve editar pessoa")
    void deveEditarPessoa() {
        Pessoa pessoa = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa result = pessoaRepository.save(pessoa);

        Pessoa pessoaEditada = new Pessoa(null, "Janet Doe", LocalDate.of(2000, 5, 13));
        Pessoa resultEditado = pessoaService.editar(result.getId(), pessoaEditada);

        assertEquals(pessoaEditada.getNome(), resultEditado.getNome());
        assertEquals(pessoaEditada.getDataNascimento(), resultEditado.getDataNascimento());
    }

    @Test
    @DisplayName("Deve adicionar endereco")
    void deveAdicionarEndereco() {
        Pessoa pessoa = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa result = pessoaRepository.save(pessoa);

        Endereco endereco = new Endereco(
                null,
                "Rua A",
                "44000-000",
                10,
                "Feira de Santana",
                "Bahia"
        );
        pessoaService.adicionarEndereco(result.getId(), endereco);
        pessoaService.adicionarEndereco(result.getId(), endereco);
        List<Endereco> enderecos = pessoaService.buscarPorId(result.getId()).getEnderecos();

        assertEquals(2, enderecos.size());
        assertEquals(result.getId(), enderecos.getFirst().getPessoa().getId());
        assertEquals(result.getId(), enderecos.getLast().getPessoa().getId());
    }

    @Test
    @DisplayName("Deve retornar todos enderecos de uma pessoa")
    void deveBuscarTodosEnderecos() {
        Pessoa pessoa = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa result = pessoaRepository.save(pessoa);

        Endereco endereco = new Endereco(
                null,
                "Rua A",
                "44000-000",
                10,
                "Feira de Santana",
                "Bahia"
        );
        pessoaService.adicionarEndereco(result.getId(), endereco);
        pessoaService.adicionarEndereco(result.getId(), endereco);
        List<Endereco> enderecos = pessoaService.buscarTodosEnderecos(result.getId());

        assertEquals(2, enderecos.size());
        assertEquals(result.getId(), enderecos.getFirst().getPessoa().getId());
        assertEquals(result.getId(), enderecos.getLast().getPessoa().getId());
    }

    @Test
    @DisplayName("Deve lancar EntityNotFoundException em caso de enderecos vazio")
    void deveLancarExcecaoEmCasoDeEnderecosVazio() {
        Pessoa pessoa = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa result = pessoaRepository.save(pessoa);
        Exception exception = assertThrows(EntityNotFoundException.class, () -> pessoaService.buscarTodosEnderecos(result.getId()));

        String expected = "Nenhuma endereco salvo";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }
}