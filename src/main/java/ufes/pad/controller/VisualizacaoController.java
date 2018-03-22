package ufes.pad.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
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
public class VisualizacaoController {

	private List<Paciente> todosPacs;
	
	@Autowired 
	private PacienteRepository pac_rep;
	
	private Paciente pacSelecionadoLesao;
	
	private String cartao_sus_busca = "702-5083-6632-6434";

	private Paciente pacBuscado;
	
	private boolean liberarVisualizacao = false;
	
	public int calcIdadePac (Date dataNascimento) {
		LocalDate hoje = LocalDate.now();		
		LocalDate dNasc = dataNascimento.toLocalDate();		
		Period p = Period.between(dNasc, hoje);	
//		System.out.println(p.getYears());		
		return (p.getYears());				
	}	
	
	public void preencheIdadePacs (List<Paciente> pacientes) {
		for (int i=0; i < pacientes.size(); i++) {
			Date dNasc = (Date) pacientes.get(i).getData_nascimento();
			pacientes.get(i).setIdade(calcIdadePac(dNasc));
//			System.out.println(pacientes.get(i).getIdade());
//			System.out.println(pacientes.get(i).getData_nascimento().getClass());
		}
	}
	
	public void listarPacientes () {		
		FacesContext context = FacesContext.getCurrentInstance();		
		try {
			setTodosPacs(pac_rep.listarPacientes());
			if (this.todosPacs.isEmpty()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Não existe nenhum paciente neste banco de dados.", "  "));				
			} 
		} catch (Exception e) {			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}
		
		preencheIdadePacs (this.todosPacs);		
		
	}	
	
	public void buscaUnicaCartaoSus () {
		try {
			pacBuscado =  pac_rep.buscaPorCartaoSus(cartao_sus_busca);			
			
			if (pacBuscado==null) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Nenhum paciente foi encontrado com este número de cartão. Verifique se o número foi digitado corretamente. Tente visualizar todos para encontrá-lo, caso não encontre, ele não está registrado no banco.", "  "));
			} else {
				this.liberarVisualizacao=true;
				Date dNasc = (Date) pacBuscado.getData_nascimento();
				pacBuscado.setIdade(calcIdadePac(dNasc));
				System.out.println("Paciente encontrado com sucesso");;
			}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}		
	}
	
// ########################################################################################################
	
	public List<Paciente> getTodosPacs() {
		return todosPacs;
	}

	public void setTodosPacs(List<Paciente> todosPacs) {
		this.todosPacs = todosPacs;
	}

	public Paciente getPacSelecionadoLesao() {
		return pacSelecionadoLesao;
	}

	public void setPacSelecionadoLesao(Paciente pacSelecionadoLesao) {
		System.out.println("FUNCIONASDASD");
		this.pacSelecionadoLesao = pacSelecionadoLesao;
	}

	public String getCartao_sus_busca() { 
		return cartao_sus_busca;
	}

	public void setCartao_sus_busca(String cartao_sus_busca) {
		this.cartao_sus_busca = cartao_sus_busca;
	}

	public Paciente getPacBuscado() {
		return pacBuscado;
	}

	public void setPacBuscado(Paciente pacBuscado) {
		this.pacBuscado = pacBuscado;
	}

	public boolean isLiberarVisualizacao() {
		return liberarVisualizacao;
	}

	public void setLiberarVisualizacao(boolean liberarVisualizacao) {
		this.liberarVisualizacao = liberarVisualizacao;
	}	
	
}
