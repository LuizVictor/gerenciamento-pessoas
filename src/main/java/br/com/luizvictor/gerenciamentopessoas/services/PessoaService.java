package br.com.luizvictor.gerenciamentopessoas.services;

import br.com.luizvictor.gerenciamentopessoas.entities.pessoa.Pessoa;
import br.com.luizvictor.gerenciamentopessoas.repositories.PessoaRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final Logger logger = LoggerFactory.getLogger(PessoaService.class);

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public Pessoa salvar(Pessoa pessoa) {
        try {
            logger.info("Salvado pessoa: {}" , pessoa.getNome());
            return pessoaRepository.save(pessoa);
        } catch (DataAccessException exception) {
            logger.error("Nao foi possivel salvar pessoa: {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
}
