package ufes.pad.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.text.WordUtils;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ufes.pad.model.Usuario;
import ufes.pad.repository.UsuarioRepository;

@ManagedBean
@ViewScoped
public class UsuarioController {

	private Usuario user = new Usuario();
	
	@Autowired
	private UsuarioRepository userRep;
	
	private Usuario UsuarioLogado = new Usuario ();
	
	private Usuario UsuarioSelecionado = new Usuario ();
	
	private String senhaAtual;
	
	private String senhaNova;
	
	private String senhaNovaRepetida;
	
	private List<Usuario> todosUsuarios = new ArrayList<Usuario>();
	
	
	
	
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
	
	@PostConstruct
	public void settUsuarioLogado() {
	    Authentication auth =
	        SecurityContextHolder.getContext().getAuthentication();

	    FacesContext context = FacesContext.getCurrentInstance();
	    
	    try {
	    	String nome_usuario = auth.getName();	    
	    	UsuarioLogado = userRep.buscaPorNomeUsuario(nome_usuario);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível recuperar o usuário logado. Tente novamente. Se o problema persistir, entre em contato com o administrador do sistema.", "  "));
	    }
	}	
	
	public String alterarSenha () {		
		FacesContext context = FacesContext.getCurrentInstance();
		BCryptPasswordEncoder crip = new BCryptPasswordEncoder(); 
		boolean match = crip.matches(senhaAtual, UsuarioLogado.getSenha());
		System.out.println(match);
		
		if (!match) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Sua senha atual está incorreta.", "  "));
			return null;
		} else {
			System.out.println(UsuarioLogado.getSenha());
			UsuarioLogado.setSenha(crip.encode(senhaNova));
			System.out.println(UsuarioLogado.getSenha());
			try {
				//userRep.save(UsuarioLogado);
				System.out.println(senhaNova);
				boolean match2 = crip.matches(senhaNova, UsuarioLogado.getSenha());
				System.out.println(match2);
				context.addMessage(null, new FacesMessage("Sua senha foi alterada com sucesso."));
			} catch (Exception e) {
				e.printStackTrace();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao alterar a senha. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
			}
		}
		
		
		return "configuracoes.xhtml";
	}
	
	@PostConstruct
	public void buscaTodosUsuarios () {
		FacesContext context = FacesContext.getCurrentInstance();		
		try {
			todosUsuarios = userRep.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao tentar recuperar os usuários. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}		
	}
	
	public void excluirUsuario () {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			userRep.delete(UsuarioSelecionado);
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível excluir o usuário. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}
		context.addMessage(null, new FacesMessage("Usuário excluido com sucesso."));
		
	}
	
	
    public void onRowEdit(RowEditEvent event) {    	
    	FacesContext context = FacesContext.getCurrentInstance();
		try {			
			userRep.save(todosUsuarios);			
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível editar o usuário. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}
		context.addMessage(null, new FacesMessage("Usuário editado com sucesso."));    	
        
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edição cancelada", null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    	
//################################# GETTERS AND SETTERS ############################################	

	
	public Usuario getUser() {
		return user;
	}

  
	public void setUser(Usuario user) {
		this.user = user;
	}

	public Usuario getUsuarioLogado() {
		return UsuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		UsuarioLogado = usuarioLogado;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public String getSenhaNovaRepetida() {
		return senhaNovaRepetida;
	}

	public void setSenhaNovaRepetida(String senhaNovaRepetida) {
		this.senhaNovaRepetida = senhaNovaRepetida;
	}

	public List<Usuario> getTodosUsuarios() {
		return todosUsuarios;
	}

	public void setTodosUsuarios(List<Usuario> todosUsuarios) {
		this.todosUsuarios = todosUsuarios;
	}

	public Usuario getUsuarioSelecionado() {
		return UsuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		UsuarioSelecionado = usuarioSelecionado;
	}


	

}


