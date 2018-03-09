package ufes.pad.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import ufes.pad.model.Usuario;
import ufes.pad.repository.UsuarioRepository;

@Repository
public class ImplementsUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository ur;
	
	@Override
	public UserDetails loadUserByUsername(String nome_usuario) throws UsernameNotFoundException {
		Usuario user = ur.buscaPorNomeUsuario(nome_usuario);
		
		if (user == null) {
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}else if (user.isApto() == false) {
			throw new UsernameNotFoundException("Este usuário não possui permissão para acessar o sistema!");
		}
		
		return user;
	}

}
