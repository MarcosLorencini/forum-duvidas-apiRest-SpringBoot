package br.com.lorencini.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lorencini.forum.config.security.TokenService;
import br.com.lorencini.forum.dto.TokenDto;
import br.com.lorencini.forum.form.LoginForm;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	
	//dispara o processo de autenticacao usuario/senha
	@Autowired
	private AuthenticationManager authManager;// classe do spring mas não consegue realizar a injeçao de dependencia 
	//temos que config na classe SecurityConfiguration através do metodo authenticationManager()
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();// tem que dar um new
		try {
			Authentication authenticate = authManager.authenticate(dadosLogin);//aqui vai chamar o metodo loadUserByUsername da classe AutenticacaoService
			String token = tokenService.gerarToken(authenticate);//eniva o usuario para gerar o token para o usuario logado
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
			
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	

}
