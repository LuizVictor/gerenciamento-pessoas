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
            Pessoa result = pessoaRepository.save(pessoa);
            logger.info("Pessoa salva com sucesso: ID = {}, Nome = {}", result.getId(), result.getNome());
            return result;
        } catch (DataAccessException exception) {
            logger.error("Erro ao salvar pessoa: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public List<Pessoa> buscarTodas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        if (pessoas.isEmpty()) {
            logger.error("Nenhuma pessoa encontrada");
            throw new EntityNotFoundException("Nenhuma pessoa encontrada");
        }

        logger.info("Encontradas {} pessoas", pessoas.size());
        return pessoas;
    }

    public Pessoa buscarPorId(Long id) {
        logger.info("Buscando pessoa com ID: {}", id);
        return pessoaRepository.findById(id).orElseThrow(() -> {
            logger.error("Nenhuma pessoa encontrada com ID: {}", id);
            return new EntityNotFoundException("Nenhuma pessoa encontrada");
        });
    }

    @Transactional
    public Pessoa editar(Pessoa pessoa) {
        try {
            Pessoa result = pessoaRepository.getReferenceById(pessoa.getId());
            result.editar(pessoa.getNome(), pessoa.getDataNascimento());

            Pessoa editada = pessoaRepository.save(result);

            logger.info("Pessoa editada com sucesso: ID = {}", editada.getId());
            return editada;
        } catch (DataAccessException exception) {
            logger.error("Erro ao editar pessoa: {}", exception.getMessage());
            throw new RuntimeException("Erro ao editar pessoa");
        }
    }

    @Transactional
    public Long adicionarEndereco(Long id, Endereco endereco) {
        try {
            Pessoa pessoa = pessoaRepository.getReferenceById(id);
            pessoa.adicionarEndereco(endereco);

            Pessoa result = pessoaRepository.save(pessoa);
            Long idEndereco = result.getEnderecos().getLast().getId();

            logger.info("Endereço adicionado com sucesso: PessoaID = {}, EndereçoID = {}", pessoa.getId(), idEndereco);
            return idEndereco;
        } catch (DataAccessException exception) {
            logger.error("Erro ao adicionar endereço: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public List<Endereco> buscarTodosEnderecos(Long pessoaId) {
        pessoaRepository.getReferenceById(pessoaId);
        List<Endereco> enderecos = enderecoRepository.findByPessoaId(pessoaId);

        if (enderecos.isEmpty()) {
            logger.error("Nenhum endereço encontrado");
            throw new EntityNotFoundException("Nenhum endereço encontrado");
        }

        logger.info("Encontrados {} endereços para pessoa com ID: {}", enderecos.size(), pessoaId);
        return enderecos;
    }

    public Endereco buscarEnderecoPorId(Long pessoaId, Long enderecoId) {
        pessoaRepository.getReferenceById(pessoaId);
        logger.info("Buscando por endereço com ID: {}", enderecoId);
        return enderecoRepository.findById(enderecoId).orElseThrow( () -> {
            logger.error("Nenhum endereço encontrado com ID: {}", enderecoId);
            return new EntityNotFoundException("Nenhum endereço encontrado");
        });
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

            logger.info("Endereço editado com sucesso: ID = {}", endereco.getId());
            return enderecoRepository.save(result);
        } catch (DataAccessException exception) {
            logger.error("Erro ao editar endereço: {}", exception.getMessage());
            throw new RuntimeException("Erro ao editar endereço");
        }
    }

    @Transactional
    public void adicionarEnderecoPrincipal(Long idPessoa, Long idEndereco) {
        try {
            Pessoa pessoa = pessoaRepository.getReferenceById(idPessoa);
            pessoa.adicionaEnderecoPrincipal(idEndereco);

            pessoaRepository.save(pessoa);

            logger.info("Endereço principal adicionado: PessoaID = {}, EndereçoID = {}", idEndereco, idPessoa);
        } catch (DataAccessException exception) {
            logger.error("Erro adicionar endereço como principal: {}", exception.getMessage());
            throw new RuntimeException("Erro ao adicionar endereço com principal");
        }
    }
}
