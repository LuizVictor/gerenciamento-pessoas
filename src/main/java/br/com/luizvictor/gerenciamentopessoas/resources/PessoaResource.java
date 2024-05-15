package br.com.luizvictor.gerenciamentopessoas.resources;

import br.com.luizvictor.gerenciamentopessoas.dtos.EnderecoDto;
import br.com.luizvictor.gerenciamentopessoas.dtos.PessoaDto;
import br.com.luizvictor.gerenciamentopessoas.entities.endereco.Endereco;
import br.com.luizvictor.gerenciamentopessoas.entities.pessoa.Pessoa;
import br.com.luizvictor.gerenciamentopessoas.services.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Pessoa>> getPessoas() {
        try {
            List<Pessoa> pessoas = pessoaService.buscarTodas();
            return ResponseEntity.ok(pessoas);
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> getPessoaById(@PathVariable Long id) {
        try {
            Pessoa pessoa = pessoaService.buscarPorId(id);
            return ResponseEntity.ok(pessoa);
        } catch (EntityNotFoundException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> editarPessoa(@PathVariable Long id, @RequestBody PessoaDto data) {
        try {
            Pessoa pessoa = new Pessoa(id, data.nome(), LocalDate.parse(data.dataNascimento()));
            Pessoa result = pessoaService.editar(pessoa);
            return ResponseEntity.ok(result);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{id}/adicionar-endereco")
    public ResponseEntity adicionarEndereco(@PathVariable Long id, @RequestBody EnderecoDto data) {
        try {
            Endereco endereco = new Endereco(
                    null,
                    data.logradouro(),
                    data.cep(),
                    data.numero(),
                    data.cidade(),
                    data.estado()
            );

            pessoaService.adicionarEndereco(id, endereco);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/{id}/enderecos")
    public ResponseEntity<List<Endereco>> buscarEnderecos(@PathVariable Long id) {
        try {
            List<Endereco> enderecos = pessoaService.buscarTodosEnderecos(id);
            return ResponseEntity.ok(enderecos);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{idPessoa}/enderecos/{idEndereco}")
    public ResponseEntity<Endereco> buscarEnderecoPorId(@PathVariable Long idPessoa, @PathVariable Long idEndereco) {
        try {
            Endereco endereco = pessoaService.buscarEnderecoPorId(idPessoa, idEndereco);
            return ResponseEntity.ok(endereco);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/enderecos/{id}")
    public ResponseEntity<Endereco> editarEndereco(@PathVariable Long id, @RequestBody EnderecoDto data) {
        try {
            Endereco endereco = new Endereco(
                    id,
                    data.logradouro(),
                    data.cep(),
                    data.numero(),
                    data.cidade(),
                    data.estado()
            );

            Endereco result = pessoaService.editarEndereco(endereco);
            return ResponseEntity.ok(result);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{idPessoa}/adicionar-principal/{idEndereco}")
    public ResponseEntity adicionarPrincipal(@PathVariable Long idPessoa, @PathVariable Long idEndereco) {
        try {
            pessoaService.adicionarEnderecoPrincipal(idPessoa, idEndereco);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
