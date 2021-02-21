package br.com.lorencini.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.lorencini.forum.dto.DetalhesDoTopicoDto;
import br.com.lorencini.forum.dto.TopicoDto;
import br.com.lorencini.forum.form.AtualizacaoTopicoForm;
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
	@Transactional
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
	
	@GetMapping("/{id}")//tem que especificar pois tem outro GetMapping
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {//@PathVariable o parametro não vem na ? e sim na /(parte da url)
		// o getOne(id) ele considera que exiate um registro(id) no banco se não encontrar não retorna nullo retorna um exception
		//Topico topico = topicoRepository.getOne(id);
		//o findById(id) se não encotrar não retorna uma exception devolve um Optional(opcional) pode ser que exista ou não o registro
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));//topico.get() chama o get() que está dentro do Optional
		} 
		
		return ResponseEntity.notFound().build();//devolve um 404 nao devolve corpo no body do postman
	}
	
	@PutMapping("/{id}")
	@Transactional //comita a transacao depois que rodar o metodo
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);

		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			//aqui nao precisa chamar o update, pois a jpa já realiza a atulização dos dados quando terminar a transaçao
			return ResponseEntity.ok(new TopicoDto(topico));		
		}
		
		return ResponseEntity.notFound().build();//devolve um 404 nao devolve corpo no body do postman
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();//nao tem corpo(construtor) de retorno é um delete, não devolve o corpo no body do postman
		}
		return ResponseEntity.notFound().build();//devolve um 404 nao devolve corpo no body do postman

	}

}
