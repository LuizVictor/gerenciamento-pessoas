package br.com.luizvictor.gerenciamentopessoas.resources;

import br.com.luizvictor.gerenciamentopessoas.dtos.PessoaDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
}