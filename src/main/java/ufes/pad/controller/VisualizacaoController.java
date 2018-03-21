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
	
	private int idadePac;
	
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
			setTodosPacs(pac_rep.findAll());
			if (this.todosPacs.isEmpty()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Não existe nenhum paciente neste banco de dados.", "  "));				
			} 
		} catch (Exception e) {			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}
		
		preencheIdadePacs (this.todosPacs);		
		
	}	
	
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

	public int getIdadePac() {
		return idadePac;
	}

	public void setIdadePac(int idadePac) {
		this.idadePac = idadePac;
	}	
	
}
