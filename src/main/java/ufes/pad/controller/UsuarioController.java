package ufes.pad.controller;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ufes.pad.model.Usuario;
import ufes.pad.repository.UsuarioRepository;

@ManagedBean
@SessionScoped
public class UsuarioController {

	private Usuario user = new Usuario();
	
	@Autowired
	private UsuarioRepository userRep;
	
	private String mensagem;
	
	
// ########################## Methods ##################################
	public String salvar () {		
		String ret = "/index.xhtml";
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			Usuario u1 = userRep.buscaPorNomeUsuario(user.getNome_usuario());
			Usuario u2 = userRep.buscaPorEmail(user.getEmail());
			
			if (u1 == null && u2 == null) {
				user.setNome(user.getNome().toUpperCase());
				userRep.save(this.user);
				context.addMessage(null, new FacesMessage("Usuário cadastrado com sucesso. Aguarde liberação do administrador para acessar o sistema."));
				user = new Usuario ();
			} else {
				ret = null;  
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Este nome de usuário ou e-mail já está cadastrado. Insira um nome diferente.", "  "));

			}			
						
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return ret;
	}	

	public void checkEmail () {
		Usuario u2 = userRep.buscaPorEmail(user.getEmail());
		if (u2 != null) {
			FacesContext context = FacesContext.getCurrentInstance();  
	        context.addMessage(null, new FacesMessage("Este e-mail já está cadastrado. Insira um nome diferente."));
		}
	}
	
	public String getNomeUsuarioLogado() {
		    Authentication authentication =
		        SecurityContextHolder.getContext().getAuthentication();

		    String nome_usuario = authentication.getName();
		    Usuario u1 = userRep.buscaPorNomeUsuario(nome_usuario);		    
		    		    		    
		    
		    return WordUtils.capitalize(u1.getNome().toLowerCase());
		  }
	
	
	
	public Usuario getUser() {
		return user;
	}


	public void setUser(Usuario user) {
		this.user = user;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	

}

