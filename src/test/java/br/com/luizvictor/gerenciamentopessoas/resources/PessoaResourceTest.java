package br.com.luizvictor.gerenciamentopessoas.resources;

import br.com.luizvictor.gerenciamentopessoas.dtos.PessoaDto;
import br.com.luizvictor.gerenciamentopessoas.entities.pessoa.Pessoa;
import br.com.luizvictor.gerenciamentopessoas.repositories.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class PessoaResourceTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PessoaRepository pessoaRepository;

    @AfterEach
    void tearDown() {
        pessoaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve registar pessoa")
    void deveRegistraPessoa() throws Exception {
        PessoaDto dto = new PessoaDto("John Doe", "1999-05-14");
        mvc.perform(post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(dto)))
                .andExpect(status().isCreated());

        assertEquals(1, pessoaRepository.count());
    }

    @Test
    @DisplayName("Nao deve registrar pessoa")
    void naoDeveRegistraPessoa() throws Exception {
        PessoaDto dto = new PessoaDto("John Doe", "1999-5-14");
        mvc.perform(post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(0, pessoaRepository.count());
    }

    @Test
    @DisplayName("Deve retornar todas pessoas")
    void deveRetornarTodasPessoa() throws Exception {
        Pessoa pessoa1 = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa pessoa2 = new Pessoa(null, "Janet Doe", LocalDate.of(2000, 5, 13));
        pessoaRepository.saveAll(List.of(pessoa1, pessoa2));

        mvc.perform(get("/api/pessoas").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertEquals(2, pessoaRepository.count());
    }

    @Test
    @DisplayName("Nao deve retornar pessoas")
    void naoDeveRetornarTodasPessoa() throws Exception {
        mvc.perform(get("/api/pessoas").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        assertEquals(0, pessoaRepository.count());
    }

    @Test
    @DisplayName("Deve buscar pessoa por id")
    void deveBuscarPessoaPorId() throws Exception {
        Pessoa pessoa1 = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa pessoa2 = new Pessoa(null, "Janet Doe", LocalDate.of(2000, 5, 13));
        List<Pessoa> pessoas = pessoaRepository.saveAll(List.of(pessoa1, pessoa2));
        Pessoa pessoa = pessoas.getFirst();

        mvc.perform(get("/api/pessoas/{id}", pessoa.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pessoa.getId()))
                .andExpect(jsonPath("$.nome").value(pessoa.getNome()));

        assertEquals(2, pessoaRepository.count());
    }

    @Test
    @DisplayName("Nao deve buscar pessoa por id invalido")
    void naoDeveBuscarPessoaPorIdInvalido() throws Exception {
        mvc.perform(get("/api/pessoas/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        assertEquals(0, pessoaRepository.count());
    }

    @Test
    @DisplayName("Deve editar pessoa")
    void deveEditarPessoa() throws Exception {
        Pessoa pessoa = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa result = pessoaRepository.save(pessoa);
        PessoaDto dto = new PessoaDto("Janet Doe", "2000-05-13");

        mvc.perform(put("/api/pessoas/{id}", result.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(result.getId()))
                .andExpect(jsonPath("$.nome").value(dto.nome()))
                .andExpect(jsonPath("$.dataNascimento").value(dto.dataNascimento()));

        assertEquals(1, pessoaRepository.count());
    }

    @Test
    @DisplayName("Nao deve editar pessoa")
    void naoDveEditarPessoa() throws Exception {
        Pessoa pessoa = new Pessoa(null, "John Doe", LocalDate.of(1999, 5, 13));
        Pessoa result = pessoaRepository.save(pessoa);
        PessoaDto dto = new PessoaDto("Janet Doe", "2000-5-13");

        mvc.perform(put("/api/pessoas/{id}", result.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(dto)))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(1, pessoaRepository.count());
    }
}