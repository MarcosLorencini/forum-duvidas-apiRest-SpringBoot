package br.com.lorencini.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.lorencini.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authenticate) {
		Usuario logado = (Usuario)authenticate.getPrincipal();//pega o usuario logado
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));//milisengudos da variavel data + os milisegundos da expiration = 1dia de expiracao do token 
		
		return Jwts.builder()
				.setIssuer("API do Forúm")//quem é app que esta gerando o token
				.setSubject(logado.getId().toString())//quem é o usuario autenticado
				.setIssuedAt(hoje)//data geracao de token
				.setExpiration(dataExpiracao)// 1 dia de expiracao
				.signWith(SignatureAlgorithm.HS256, secret)// pega a senha da aplicacao que gera o hash da assinatura do token e cripotografa com o SignatureAlgorithm.HS256 e gera o token
				.compact();
	}

}
