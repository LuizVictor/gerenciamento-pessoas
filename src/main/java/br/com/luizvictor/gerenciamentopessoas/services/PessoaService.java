package br.com.luizvictor.gerenciamentopessoas.services;

import br.com.luizvictor.gerenciamentopessoas.entities.endereco.Endereco;
import br.com.luizvictor.gerenciamentopessoas.entities.pessoa.Pessoa;
import br.com.luizvictor.gerenciamentopessoas.repositories.EnderecoRepository;
import br.com.luizvictor.gerenciamentopessoas.repositories.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final Logger logger = LoggerFactory.getLogger(PessoaService.class);

    public PessoaService(PessoaRepository pessoaRepository, EnderecoRepository enderecoRepository) {
        this.pessoaRepository = pessoaRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional
    public Pessoa salvar(Pessoa pessoa) {
        try {
            logger.info("Salvado pessoa: {}", pessoa.getNome());
            return pessoaRepository.save(pessoa);
        } catch (DataAccessException exception) {
            logger.error("Nao foi possivel salvar pessoa: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public List<Pessoa> buscarTodas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        if (pessoas.isEmpty()) {
            logger.error("Nenhuma pessoa salva");
            throw new EntityNotFoundException("Nenhuma pessoa salva");
        }

        return pessoas;
    }

    public Pessoa buscarPorId(Long id) {
        logger.info("Buscando pessoa de ID: {}", id);
        return pessoaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nenhuma pessoa encontrada")
        );
    }

    @Transactional
    public Pessoa editar(Long id, Pessoa pessoa) {
        try {
            Pessoa result = buscarPorId(id);

            result.editar(pessoa.getNome(), pessoa.getDataNascimento());
            return pessoaRepository.save(result);
        } catch (DataAccessException exception) {
            logger.error("Nao foi possivel editar pessoa: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Transactional
    public void adicionarEndereco(Long id, Endereco endereco) {
        try {
            Pessoa pessoa = buscarPorId(id);
            pessoa.adicionarEndereco(endereco);
            Pessoa result = pessoaRepository.save(pessoa);
            Long enderecoId = result.getEnderecos().getLast().getId();
            logger.info("Adicionando endereco de ID {} a pessoa de ID {}", enderecoId, pessoa.getId());
        } catch (DataAccessException exception) {
            logger.error("Nao foi adicionar enderco: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public List<Endereco> buscarTodosEnderecos(Long pessoaId) {
        buscarPorId(pessoaId);
        List<Endereco> enderecos = enderecoRepository.findByPessoaId(pessoaId);

        if (enderecos.isEmpty()) {
            logger.error("Nenhuma endereco salvo");
            throw new EntityNotFoundException("Nenhuma endereco salvo");
        }

        logger.info("Retornando enderecos da pessoa de ID {}", pessoaId);
        return enderecos;
    }
}
