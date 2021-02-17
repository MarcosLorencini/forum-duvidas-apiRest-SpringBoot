package br.com.lorencini.forum.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.lorencini.forum.dto.TopicoDto;
import br.com.lorencini.forum.form.TopicoForm;
import br.com.lorencini.forum.modelo.Topico;
import br.com.lorencini.forum.repository.CursoRepository;
import br.com.lorencini.forum.repository.TopicoRespository;

//@Controller tem que colocar o @ResponseBody acima do métodos
//@RestController //não precisa usar o @ResponseBody acima dos metodos já assume que o @ResponseBody é o comportamento padrã do metodo
@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRespository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {//usa o dto para escolher quais atributos quer retornar e não precisa retornar tudo que esta em Topico.java
		if(nomeCurso == null) {						//parametro via url
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			//quando existir um atributo cursoNome dentro de Topico
			//findByCursoNome é um atributo dentro de Topico igual a cursoNome
			//findByCurso_Nome nome do atributo do relacionamento Curso com o Topico + o atributo nome dentro de Curso
			List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
		
		//o spring retorna a lista no forma json ele usa uma bliblioteca chamada jackson ele faz a conversão 
		//de objeto para json
	}
	
	//retorno void por padrão retorna 200
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		//@ResponseBody: spring é para pegar topico do corpo da requisão e não como parametro da url igual em GET
		//tem que converter form para Topico que o parametro de save está esperando
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();//topico.getId() substitui {id}
		//quando cria um recurso:devolve um created(201)+um cabeçalho http chamado location com uri deste novo recurso que foi criado+no corpo da resposta
		//a respresetação do recurso que foi criado que é o TopicoDto
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
		
	}

}