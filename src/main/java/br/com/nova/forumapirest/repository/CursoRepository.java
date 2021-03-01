package br.com.nova.forumapirest.repository;

import br.com.nova.forumapirest.modelo.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Curso findByNome(String nome);
}
