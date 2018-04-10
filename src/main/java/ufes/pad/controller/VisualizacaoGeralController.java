package ufes.pad.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Imagem;
import ufes.pad.model.Lesao;
import ufes.pad.model.PacienteGeral;
import ufes.pad.repository.PacienteGeralRepository;

@ManagedBean
@ViewScoped
public class VisualizacaoGeralController {
	
	private PacienteGeral pac;
	
	private List<PacienteGeral> todosPacs;
	
	private int numPacs;
	
	@Autowired
	private PacienteGeralRepository pac_rep;
	
	
	public void pegaTodosPacGerais () {
		try {
			todosPacs = pac_rep.findAll();			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	@PostConstruct
	public void listarPacientesGerais () {		
		FacesContext context = FacesContext.getCurrentInstance();		
		try {
			todosPacs = pac_rep.findAll();			
			if (todosPacs.isEmpty()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Não existe nenhum paciente neste banco de dados.", "  "));				
			} 
		} catch (Exception e) {			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}	
		setNumPacs(todosPacs.size());		
	}
	
	public List<String> pegaImagensLesao (Lesao les){
		List<String> imgsPath = new ArrayList<String>();
		List<Imagem> imgs = les.getImagens();
		
		for (Imagem img : imgs) {
			imgsPath.add(img.getPath());
		}	
		
		return imgsPath;
	}	
	

	public PacienteGeral getPac() {
		return pac;
	}

	public void setPac(PacienteGeral pac) {
		this.pac = pac;
	}

	public List<PacienteGeral> getTodosPacs() {
		return todosPacs;
	}

	public void setTodosPacs(List<PacienteGeral> todosPac) {
		this.todosPacs = todosPac;
	}


	public int getNumPacs() {
		return numPacs;
	}


	public void setNumPacs(int numPacs) {
		this.numPacs = numPacs;
	}

}
