package br.com.luizvictor.gerenciamentopessoas.repositories;

import br.com.luizvictor.gerenciamentopessoas.entities.endereco.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByPessoaId(Long id);
}