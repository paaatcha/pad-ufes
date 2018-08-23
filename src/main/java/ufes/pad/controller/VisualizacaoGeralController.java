package ufes.pad.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.ImagemGeral;
import ufes.pad.model.LesaoGeral;
import ufes.pad.model.PacienteGeral;
import ufes.pad.repository.LesaoGeralRepository;
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
	
	private int numLesoesEspecifico;
	
	private boolean somenteNaoAuditados = false;
	
	private boolean exibirTabela = false;
	
	private String cartao_sus = "";
	
	private String filtroLesao = "";
	
	private boolean somenteObs = false;
	 
	@Autowired
	private PacienteGeralRepository pac_rep;
	
	@Autowired
	private LesaoGeralRepository les_rep;
	
	
	 
	public void pegaTodosPacGerais () {
		try {
			todosPacs = pac_rep.findAll();			
		} catch (Exception e) { 
			// TODO: handle exception
		}		
	}
	
	
	public void listarPacientesGerais () {		
		FacesContext context = FacesContext.getCurrentInstance();
		
		try {		
			if (!cartao_sus.equals("")) {
				PacienteGeral pacsus = new PacienteGeral(); 
				pacsus = pac_rep.buscaPorCartaoSus(getCartao_sus());
				todosPacs = new ArrayList<PacienteGeral>();
				
				if (pacsus != null) {				
					todosPacs.add(pacsus);
				}
				
			} else if (isSomenteNaoAuditados()) {
				todosPacs = pac_rep.findByAuditadoFalse();
			} else if (isSomenteObs()) {
				todosPacs = pac_rep.filtraPorObs("");
			} else if (!filtroLesao.equals("")) {
				todosPacs = pac_rep.filtraPorLesao(filtroLesao);
				setNumLesoesEspecifico(les_rep.countByDiagnosticoLike(filtroLesao));
			} else {
				todosPacs = pac_rep.findAll();
			}
			
			if (todosPacs.isEmpty()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Não existe nenhum paciente neste banco de dados.", "  "));				
			} else {
				setNumPacs(todosPacs.size());	
				this.numLesoes = PacienteGeralController.totalLesoes(todosPacs); 
				context.addMessage(null, new FacesMessage("Pacientes encontrados com sucesso"));
				exibirTabela = true;
			}
		} catch (Exception e) {			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));	
			e.printStackTrace();
		}	
		
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

	public boolean isExibirTabela() {
		return exibirTabela;
	}


	public void setExibirTabela(boolean exibirTabela) {
		this.exibirTabela = exibirTabela;
	}


	public String getCartao_sus() {
		return cartao_sus;
	}


	public void setCartao_sus(String cartao_sus) {
		this.cartao_sus = cartao_sus;
	}


	public boolean isSomenteNaoAuditados() {
		return somenteNaoAuditados;
	}


	public void setSomenteNaoAuditados(boolean somenteNaoAuditados) {
		this.somenteNaoAuditados = somenteNaoAuditados;
	}


	public String getFiltroLesao() {
		return filtroLesao;
	}


	public void setFiltroLesao(String filtroLesao) {
		this.filtroLesao = filtroLesao;
	}


	public int getNumLesoesEspecifico() {
		return numLesoesEspecifico;
	}


	public void setNumLesoesEspecifico(int numLesoesEspecifico) {
		this.numLesoesEspecifico = numLesoesEspecifico;
	}


	public boolean isSomenteObs() {
		return somenteObs;
	}


	public void setSomenteObs(boolean somenteObs) {
		this.somenteObs = somenteObs;
	}



}

