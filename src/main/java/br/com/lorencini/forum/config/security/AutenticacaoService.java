package br.com.lorencini.forum.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.lorencini.forum.modelo.Usuario;
import br.com.lorencini.forum.repository.UsuarioRepository;

//quando clicar no formulario de login o Spring vai chamar este metodo

@Service
public class AutenticacaoService implements UserDetailsService { //informa para o Spring que esta classe tem a logica autenticacao
	
	@Autowired
	private UsuarioRepository repository;

	//recebe como parametro o email(usuario) que foi digitado no form e busca a senha no banco para ser validada
	//o spring é que vai chamar este método 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if(usuario.isPresent()) {
			return usuario.get();
			
		}
		throw new UsernameNotFoundException("Dados inválidos");
	}

}
