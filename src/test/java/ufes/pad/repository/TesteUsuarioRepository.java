package ufes.pad.repository;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import ufes.pad.model.Usuario;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TesteUsuarioRepository {

	@Autowired
	private UsuarioRepository usuarioRep;
	
	public void salvar() {
		Usuario user = new Usuario();
		user.setNome("Andre");
		user.setEmail("pacheco.comp@gmail.com");
		user.setNome_usuario("apacheco");
		user.setSenha("1234");
		
		Usuario userSalvo = usuarioRep.save(user);
		Assert.assertNotNull(userSalvo.getId());
		
	}
	
}
