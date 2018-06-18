package ufes.pad.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.ImagemGeral;
import ufes.pad.model.LesaoGeral;
import ufes.pad.model.PacienteGeral;
import ufes.pad.repository.PacienteGeralRepository;

@ManagedBean
@ViewScoped
public class VisualizacaoGeralController {
	
	private PacienteGeral pac;
	
	private PacienteGeral pacSelecionado;
	 
	private LesaoGeral lesaoSelecionada;
	
	private List<PacienteGeral> todosPacs;
	
	private int numPacs;
	
	private int numLesoes;
	
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
		this.numLesoes = PacienteGeralController.totalLesoes(todosPacs); 
	}
	
	
	public List<String> pegaImagensLesao (LesaoGeral les){
		List<String> imgsPath = new ArrayList<String>();
		List<ImagemGeral> imgs = les.getImagens();
		
		for (ImagemGeral img : imgs) {
			imgsPath.add(img.getPath());
		}	
		
		return imgsPath;
	}	
	
	public void excluirPacienteGeral () { 
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println ("ENTROU PARA EXCLUIR"); 
		try {
			
			//for (LesaoGeral les : pacSelecionado.getLesoes()) {
			//	excluirImagensServer(les.getImagens());
			//}
			
			System.out.println("Excluindo paciente: " + pacSelecionado.getCartao_sus()); 
			
			//pac_rep.delete(pacSelecionado);
			
			context.addMessage(null, new FacesMessage("Paciente excluido com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir paciente. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}
	}
	
	static public void excluirImgServer (ImagemGeral img) {
		FacesContext context = FacesContext.getCurrentInstance();
		try{   		
    		
			File file = new File("src/main/webapp/dashboard/imgLesoesGeral/"+img.getPath());        	
    		file.delete();    	   
    	}catch(Exception e){    
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir a imagem do servidor. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
    		e.printStackTrace();    		
    	}		
	}	
	
	static public void excluirImagensServer (List<ImagemGeral> imgs) {
		
		for (ImagemGeral img : imgs) {		
			excluirImgServer(img);
		}
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


	public LesaoGeral getLesaoSelecionada() {
		return lesaoSelecionada;
	}


	public void setLesaoSelecionada(LesaoGeral lesaoSelecionada) {
		this.lesaoSelecionada = lesaoSelecionada;
	}


	public PacienteGeral getPacSelecionado() {
		return pacSelecionado;
	}


	public void setPacSelecionado(PacienteGeral pacSelecionado) {
		this.pacSelecionado = pacSelecionado;
	}


	public int getNumLesoes() {
		return numLesoes;
	}


	public void setNumLesoes(int numLesoes) {
		this.numLesoes = numLesoes;
	}

}
