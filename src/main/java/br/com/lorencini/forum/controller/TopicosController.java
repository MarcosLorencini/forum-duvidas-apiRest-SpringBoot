package br.com.lorencini.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.lorencini.forum.TopicoService;
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
	private TopicoService topicoService;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	@Cacheable(value = "listaDeTopico") //guarda o retorno do metodo em cach listaDeTopico= identificador único do cache.
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,  
			@PageableDefault(sort="id", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {//usa o dto para escolher quais atributos quer retornar e não precisa retornar tudo que esta em Topico.java
		
		//exemplo que cria na mão tem que passar todos os parametros na url
		//Pageable paginacao = PageRequest.of(pagina, qtd, Direction.ASC, ordenacao); 
		
		//Para usar o Pageable paginacao tem que colocar a anotação @EnableSpringDataWebSupport na classe
		//ForumApplication
		
		//caso não informe o sort e a direction na urle por padrao ele vai usar o @PageableDefault(sort="id", direction = Direction.DESC)
		
		if(nomeCurso == null) {						//parametro via url
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			//quando existir um atributo cursoNome dentro de Topico
			//findByCursoNome é um atributo dentro de Topico igual a cursoNome
			//findByCurso_Nome nome do atributo do relacionamento Curso com o Topico + o atributo nome dentro de Curso
			Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
		
		//o spring retorna a lista no forma json ele usa uma bliblioteca chamada jackson ele faz a conversão 
		//de objeto para json
	}
	
	//retorno void por padrão retorna 200
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopico", allEntries = true)//limpa o cache do método lista para não ter informação desatualizada
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
	
	
	  @GetMapping("/{id}")//tem que especificar pois tem outro GetMapping public
	  ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id)  {//@PathVariable o parametro não vem na ? e sim na /(parte da url) // o  getOne(id) ele considera que exiate um registro(id) no banco se não encontrar  não retorna nullo retorna um exception //Topico topico =
	  topicoRepository.getOne(id); //o findById(id) se não encotrar não retorna uma  exception devolve um Optional(opcional) pode ser que exista ou não o registro
	  Optional<Topico> topico = topicoRepository.findById(id);
	  if(topico.isPresent()) { return ResponseEntity.ok(new
	  DetalhesDoTopicoDto(topico.get()));//topico.get() chama o get() que está dentro do Optional 
	  }
	  
	  return ResponseEntity.notFound().build();//devolve um 404 nao devolve corpo  no body do postman 
	  }
	 
	
	@PutMapping("/{id}")
	@Transactional //comita a transacao depois que rodar o metodo
	@CacheEvict(value = "listaDeTopico", allEntries = true)//limpa o cache do método lista para não ter informação desatualizada
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
	@CacheEvict(value = "listaDeTopico", allEntries = true)//limpa o cache do método lista para não ter informação desatualizada
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();//nao tem corpo(construtor) de retorno é um delete, não devolve o corpo no body do postman
		}
		return ResponseEntity.notFound().build();//devolve um 404 nao devolve corpo no body do postman

	}
	
	/*
	 * @GetMapping("/{id}") public ResponseEntity<DetalhesDoTopicoDto>
	 * detalhar(@PathVariable Long id) throws Throwable { Optional<Topico> topico =
	 * topicoService.findById(id); if(topico.isPresent()) { return
	 * ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get())); }
	 * 
	 * return ResponseEntity.notFound().build(); }
	 */

}
