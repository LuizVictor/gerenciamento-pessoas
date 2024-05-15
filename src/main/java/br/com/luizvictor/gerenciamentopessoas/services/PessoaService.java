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
    public Pessoa editar(Pessoa pessoa) {
        try {
            Pessoa result = pessoaRepository.getReferenceById(pessoa.getId());
            result.editar(pessoa.getNome(), pessoa.getDataNascimento());
            return pessoaRepository.save(result);
        } catch (DataAccessException exception) {
            logger.error("Nao foi possivel editar pessoa: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Transactional
    public Long adicionarEndereco(Long id, Endereco endereco) {
        try {
            Pessoa pessoa = pessoaRepository.getReferenceById(id);
            pessoa.adicionarEndereco(endereco);

            Pessoa result = pessoaRepository.save(pessoa);
            Long enderecoId = result.getEnderecos().getLast().getId();

            logger.info("Adicionando endereco de ID {} a pessoa de ID {}", enderecoId, pessoa.getId());
            return enderecoId;
        } catch (DataAccessException exception) {
            logger.error("Nao foi possivel adicionar endereco: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public List<Endereco> buscarTodosEnderecos(Long pessoaId) {
        pessoaRepository.getReferenceById(pessoaId);
        List<Endereco> enderecos = enderecoRepository.findByPessoaId(pessoaId);

        if (enderecos.isEmpty()) {
            logger.error("Nenhuma endereco salvo");
            throw new EntityNotFoundException("Nenhuma endereco salvo");
        }

        logger.info("Retornando enderecos da pessoa de ID {}", pessoaId);
        return enderecos;
    }

    public Endereco buscarEnderecoPorId(Long pessoaId, Long enderecoId) {
        pessoaRepository.getReferenceById(pessoaId);
        logger.info("Buscando por endereco de ID {}", enderecoId);
        return enderecoRepository.findById(enderecoId).orElseThrow(
                () -> new EntityNotFoundException("Endereco nao encontrado")
        );
    }

    @Transactional
    public Endereco editarEndereco(Endereco endereco) {
        try {
            Endereco result = enderecoRepository.getReferenceById(endereco.getId());
            result.editar(
                    endereco.getLogradouro(),
                    endereco.getCep(),
                    endereco.getNumero(),
                    endereco.getCidade(),
                    endereco.getEstado()
            );

            logger.info("Endereco de ID {} foi editado", endereco.getId());
            return enderecoRepository.save(result);
        } catch (DataAccessException exception) {
            logger.error("Nao foi possivel editar endereco: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Transactional
    public void adicionarEnderecoPrincipal(Long idPessoa, Long idEndereco) {
        try {
            Pessoa pessoa = buscarPorId(idPessoa);
            pessoa.adicionaEnderecoPrincipal(idEndereco);

            pessoaRepository.save(pessoa);

            logger.info("Endereco de ID {} foi adicionado como principal de pessoa com ID {}", idEndereco, idPessoa);
        } catch (DataAccessException exception) {
            logger.error("Nao foi possivel adicionar endereco como principal: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
}
