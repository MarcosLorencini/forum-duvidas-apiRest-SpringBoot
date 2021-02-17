package br.com.lorencini.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lorencini.forum.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	Curso findByNome(String nomeCurso);

}
