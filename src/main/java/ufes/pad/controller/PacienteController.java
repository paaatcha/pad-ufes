package ufes.pad.controller;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Paciente;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class PacienteController {

	private Paciente pac = new Paciente();
	private List<Paciente> todosPacs;
	private Paciente pacBuscado;
	
	@Autowired
	private PacienteRepository pac_rep;
	
	private boolean visivel = false;
	private boolean visivelTodos = false;
	private String cartao_sus_busca;
	
	
	public String salvar () {		
		String ret = "/dashboard/home.xhtml";
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			Paciente p1 = pac_rep.buscaPorCartaoSus(this.pac.getCartao_sus());	
			
			System.out.println("ENTREI AQUI");
			
			if (p1 == null) {				
				pac_rep.save(this.pac);
				context.addMessage(null, new FacesMessage("Paciente cadastrado com sucesso. Utilize o celular para incluir as imagens e dados da lesão."));
			} else {
				ret = null;  
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Este cartão do SUS já está cadastrado! Visualize o paciente na página de visualização.", "  "));

			}			
						
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return ret;
	}	
	
	public List<Paciente> listarPacientes () {		
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			setTodosPacs(pac_rep.findAll());
			if (this.todosPacs.isEmpty()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Não existe nenhum paciente neste banco de dados.", "  "));
			} else {
				this.visivelTodos = true;				
			}
			return getTodosPacs();
		} catch (Exception e) {			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));
			return null;
		}		
	}	
	
	public void buscaUnicaCartaoSus () {
		try {
			pacBuscado =  pac_rep.buscaPorCartaoSus(cartao_sus_busca);			
			
			if (pacBuscado==null) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Nenhum paciente foi encontrado com este número de cartão. Tente visualizar todos para encontrá-lo, caso não encontre, ele não está registrado no banco.", "  "));
			} else {
				this.visivel=true;
			}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}		
	}
	
	
/* ###########################################  Getters and Setters ###################################################*/
	public Paciente getPac() {
		return pac;
	}

	public void setPac(Paciente pac) {
		this.pac = pac;
	}

	public PacienteRepository getPac_rep() {
		return this.pac_rep;
	}

	public void setUserRep(PacienteRepository pac_rep) {
		this.pac_rep = pac_rep;
	}

	public boolean isVisivel() {
		return visivel;
	}

	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	public Paciente getPacBuscado() {
		return pacBuscado;
	}

	public void setPacBuscado(Paciente pacBuscado) {
		this.pacBuscado = pacBuscado;
	}

	public String getCartao_sus_busca() {
		return cartao_sus_busca;
	}

	public void setCartao_sus_busca(String cartao_sus_busca) {
		this.cartao_sus_busca = cartao_sus_busca;
	}

	public List<Paciente> getTodosPacs() {
		return todosPacs;
	}

	public void setTodosPacs(List<Paciente> todosPacs) {
		this.todosPacs = todosPacs;
	}

	public boolean isVisivelTodos() {
		return visivelTodos;
	}

	public void setVisivelTodos(boolean visivelTodos) {
		this.visivelTodos = visivelTodos;
	}
	
	
	
	
}
