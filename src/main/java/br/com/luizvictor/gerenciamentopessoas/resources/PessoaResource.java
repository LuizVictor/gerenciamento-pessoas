package br.com.luizvictor.gerenciamentopessoas.resources;

import br.com.luizvictor.gerenciamentopessoas.dtos.PessoaDto;
import br.com.luizvictor.gerenciamentopessoas.entities.pessoa.Pessoa;
import br.com.luizvictor.gerenciamentopessoas.services.PessoaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaResource {
    private static final Logger log = LoggerFactory.getLogger(PessoaResource.class);
    private final PessoaService pessoaService;

    public PessoaResource(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<Pessoa> salvarPessoa(@RequestBody PessoaDto data) {
        try {
            Pessoa pessoa = new Pessoa(null, data.nome(), LocalDate.parse(data.dataNascimento()));
            Pessoa result = pessoaService.salvar(pessoa);
            return ResponseEntity.created(URI.create("/api/pessoa/" + result.getId())).body(result);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
