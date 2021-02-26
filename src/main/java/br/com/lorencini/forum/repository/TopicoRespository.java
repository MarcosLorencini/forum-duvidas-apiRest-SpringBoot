package br.com.lorencini.forum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lorencini.forum.modelo.Topico;

public interface TopicoRespository extends JpaRepository<Topico, Long> {
	//padrão de nomeclatura do spring data
	Page<Topico> findByCurso_Nome(String nomeCurso, Pageable paginacao);
	//caso queira criar um nome do método que faça a mesma coisa, tem que gerar uma query
	@Query("SELECT t FROM Topico t where t.curso.nome = :nomeCurso")
	List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);

}
