package br.com.lorencini.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.lorencini.forum.modelo.Usuario;
import br.com.lorencini.forum.repository.UsuarioRepository;
//todas as req passam por aqui antes de bater nos endpoints
//não existe anotacao para este filtro o nosso filtro esta cadastrado na classe SecurityConfiguration
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter  { //fitro do spring que é chamado uma única x a cada requisicao
	
	//não conseguimos o usar o @Autowired tivemos que criar um construtor para receber o TokenService
	private TokenService tokenService;
	
	private UsuarioRepository usuarioRepository;
	
	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//pegar token do cabeçalho e valida lo
		
		String token = recuperarToken(request);
		boolean valido = tokenService.isTokenValido(token);
		if(valido) {
			autenticarCliente(token);
		}
		filterChain.doFilter(request, response);
		
	}
	
	//a autenticacao do usuario/senha + token já foi feito aqui só vai considerar que ele está autenticado
	private void autenticarCliente(String token) {
		Long idUsuario = tokenService.getIdUsuario(token);//pega o id do usuario que está dentro do token
		Usuario usuario= usuarioRepository.findById(idUsuario).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());//null é senha não precisa passar pq foi autenticado lá tras.  usuario.getAuthorities() perfis do usuario
		SecurityContextHolder.getContext().setAuthentication(authentication);//precisa passar os dados do ususaro para o spring considerar a autenticacao
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer "))
		return null;
		
		return token.substring(7, token.length());
	}

}
