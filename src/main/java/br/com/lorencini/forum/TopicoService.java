package br.com.lorencini.forum;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.lorencini.forum.modelo.Topico;
import br.com.lorencini.forum.repository.TopicoRespository;

@Service
public class TopicoService {
	

	@Autowired
	private TopicoRespository topicoRepository;
	
	public Optional<Topico> findById(Long id) throws Throwable {
		return Optional.ofNullable(topicoRepository.findById(id).orElseThrow(() -> new Throwable()));
		
	}

}
