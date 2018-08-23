package ufes.pad.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Imagem;
import ufes.pad.model.ImagemGeral;
import ufes.pad.model.Lesao;
import ufes.pad.model.LesaoGeral;
import ufes.pad.model.Paciente;
import ufes.pad.model.PacienteGeral;
import ufes.pad.repository.PacienteGeralRepository;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class GerarDatasetController {

	@Autowired
	private PacienteRepository pac_rep;
	
	@Autowired
	private PacienteGeralRepository pac_geral_rep;
	
	private boolean dsCompleto = false;
	
	private boolean dsCirurgia = false;
	
	private boolean dsApp = false;
	
	private boolean dsGerado = false;

	 
	public void gerar() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (this.dsCompleto) {
			System.out.println("Gerando dataset completo...");
			if (this.escreveDatasetCompleto ()) {
				this.dsGerado = true;
			}
			
		} else if (this.dsCirurgia) {
			System.out.println("Gerando dataset cirurgia...");
			if (this.escreveDatasetCirurgia()) {
				this.dsGerado = true;
			}
			
		} else if (this.dsApp) {
			System.out.println("Gerando dataset aplicativo...");
			if (this.escreveDatasetApp()) {
				this.dsGerado = true;
			}			
			
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Se você não definir um dataset fica difícil funcionar --'", "  "));
			
		}
		
		 
	}
	
	private boolean escreveDatasetCompleto () {
		
		FacesContext context = FacesContext.getCurrentInstance();
		String diag;
		
		// Pegando todos os pacientes da cirurgia e do aplicativo
		List<Paciente> pacs = pac_rep.findAll();
		List<PacienteGeral> pacsGerais = pac_geral_rep.findAll();
		
		try {
			PrintWriter pw = new PrintWriter(new File("src/main/webapp/dashboard/dataset/dataset.csv"));
	        StringBuilder sb = new StringBuilder();
	        sb.append("Diagnostico");
	        sb.append(',');
	        sb.append("Path");
	        sb.append('\n');
			
		// Inserindo pacientes de cirurgia
		for (Paciente p : pacs) {
			for (Lesao les : p.getLesoes()) {
				diag = les.getDiagnostico_clinico();
				for (Imagem img : les.getImagens()) {
					sb.append(diag);
					sb.append(',');
					sb.append(img.getPath());
					sb.append('\n');
				}				
				
			}
		}
		
		// Inserindo pacientes do aplicativo
		for (PacienteGeral p : pacsGerais) {
			for (LesaoGeral les : p.getLesoes()) {
				diag = les.getDiagnostico();
				for (ImagemGeral img : les.getImagens()) {
					sb.append(diag);
					sb.append(',');
					sb.append(img.getPath());
					sb.append('\n');
				}				
				
			}
		}		
		
		// Finalizando o arquivo
		pw.write(sb.toString());
        pw.close();
        System.out.println("Dataset completo salvo com sucesso!");
        context.addMessage(null, new FacesMessage("Dataset completo salvo com sucesso!"));
		
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro na geração do dataset. Leia o log no terminal.", "  "));
			return false;
		}		
		
		return true;
	}
	
	public boolean escreveDatasetApp () {
		FacesContext context = FacesContext.getCurrentInstance();
		
		// Pegando todos os pacientes do aplicativo
		List<PacienteGeral> pacsGerais = pac_geral_rep.findAll();
		
		try {
			PrintWriter pw = new PrintWriter(new File("src/main/webapp/dashboard/dataset/dataset.csv"));
	        StringBuilder sb = new StringBuilder();	        
	        sb.append("Diagnostico");
	        sb.append(',');
	        sb.append("Regiao");
	        sb.append(',');	        
	        sb.append("Cocou");
	        sb.append(',');	
	        sb.append("Cresceu");
	        sb.append(',');
	        sb.append("Doeu");
	        sb.append(',');
	        sb.append("Mudou");
	        sb.append(',');
	        sb.append("Sangrou");
	        sb.append(',');
	        sb.append("Relevo");
	        sb.append(',');	
	        sb.append("Idade");
	        sb.append(',');	                
	        sb.append("Path");
	        sb.append('\n');			
	        
		// Inserindo pacientes do aplicativo
		for (PacienteGeral p : pacsGerais) {
			for (LesaoGeral les : p.getLesoes()) {				
				for (ImagemGeral img : les.getImagens()) {					
					sb.append(les.getDiagnostico().replaceAll(",", "-"));
			        sb.append(',');
			        sb.append(les.getRegiao().replaceAll(",", "-"));
			        sb.append(',');	        
			        sb.append(les.getCocou());
			        sb.append(',');	
			        sb.append(les.getCresceu());
			        sb.append(',');
			        sb.append(les.getDoeu());
			        sb.append(',');
			        sb.append(les.getMudou());
			        sb.append(',');
			        sb.append(les.getSangrou());
			        sb.append(',');
			        sb.append(les.getRelevo());
			        sb.append(',');	 
			        sb.append(les.getIdade());
			        sb.append(',');			               
			        sb.append(img.getPath());
			        sb.append('\n');
				}
			}
		}		
		
		// Finalizando o arquivo
		pw.write(sb.toString());
        pw.close();
        System.out.println("Dataset aplicativo salvo com sucesso!");
        context.addMessage(null, new FacesMessage("Dataset aplicativo salvo com sucesso!"));
		
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro na geração do dataset. Leia o log no terminal.", "  "));
			return false;
		}		
		
		return true;
	}
	
	public boolean escreveDatasetCirurgia () {
		FacesContext context = FacesContext.getCurrentInstance();
		
		// Pegando todos os pacientes do aplicativo
		List<Paciente> pacs = pac_rep.findAll();
		
		try {
			PrintWriter pw = new PrintWriter(new File("src/main/webapp/dashboard/dataset/dataset.csv"));
	        StringBuilder sb = new StringBuilder();	        
	        sb.append("Diagnostico");
	        sb.append(',');
	        sb.append("Profissao");
	        sb.append(',');	        
	        sb.append("IdadeComecouTrabalhar");
	        sb.append(',');	
	        sb.append("Rede esgoto");
	        sb.append(',');
	        sb.append("Agua encanada");
	        sb.append(',');
	        sb.append("Cigarro");
	        sb.append(',');
	        sb.append("Alcool");
	        sb.append(',');
	        sb.append("Agrotoxico");
	        sb.append(',');
	        sb.append("TipoPele");
	        sb.append(',');
	        sb.append("Distrofia");
	        sb.append(',');	  
	        sb.append("ExposicaoSol");
	        sb.append(',');	  
	        sb.append("HoraExposicao");
	        sb.append(',');	  
	        sb.append("Chapeu");
	        sb.append(',');	 
	        sb.append("MangaComprida");
	        sb.append(',');	  
	        sb.append("CalcaComprida");
	        sb.append(',');	  
	        sb.append("FiltroSolar");
	        sb.append(',');	  
	        sb.append("HistoricoCancerPele");
	        sb.append(',');	  
	        sb.append("HistoricoCancer");
	        sb.append(',');	  
	        sb.append("HiperTensao");
	        sb.append(',');	  
	        sb.append("Diabetes");
	        sb.append(',');	  
	        sb.append("Path");
	        sb.append('\n');			
	        
		// Inserindo pacientes do aplicativo
		for (Paciente p : pacs) {
			for (Lesao les : p.getLesoes()) {				
				for (Imagem img : les.getImagens()) {					
					sb.append(les.getDiagnostico_clinico());
			        sb.append(',');
			        sb.append(p.getAtv_principal());
			        sb.append(',');	        
			        sb.append(p.getIdade_inicio_atv());
			        sb.append(',');	
			        sb.append(p.getRede_esgoto());
			        sb.append(',');
			        sb.append(p.getAgua_encanada());
			        sb.append(',');
			        sb.append(p.getFumo());
			        sb.append(',');
			        sb.append(p.getBebida());
			        sb.append(',');
			        sb.append(p.getAgrotoxico().replaceAll(",", "-"));
			        sb.append(',');
			        sb.append(p.getTipo_pele());
			        sb.append(',');
			        sb.append(p.getDestrofia_solar());
			        sb.append(',');	  
			        sb.append(p.getExp_sol());
			        sb.append(',');	  
			        sb.append(p.getHora_exp_sol());
			        sb.append(',');	  
			        sb.append(p.getChapeu());
			        sb.append(',');	 
			        sb.append(p.getManga_cumprida());
			        sb.append(',');	  
			        sb.append(p.getCalca_cumprida());
			        sb.append(',');	  
			        sb.append(p.getFiltro_solar());
			        sb.append(',');	  
			        sb.append(p.getHist_cancer_pele());
			        sb.append(',');	  
			        sb.append(p.getHist_cancer());
			        sb.append(',');	  
			        sb.append(p.getHAS());
			        sb.append(',');	  
			        sb.append(p.getDiabetes());
			        sb.append(',');	  
			        sb.append(img.getPath());
			        sb.append('\n');
				}
			}
		}		
		
		// Finalizando o arquivo
		pw.write(sb.toString());
        pw.close();
        System.out.println("Dataset aplicativo salvo com sucesso!");
        context.addMessage(null, new FacesMessage("Dataset aplicativo salvo com sucesso!"));
		
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro na geração do dataset. Leia o log no terminal.", "  "));
			return false;
		}		
		
		return true;
	}
	

	public boolean isDsCirurgia() {
		return dsCirurgia;
	}

	public void setDsCirurgia(boolean dsCirurgia) {
		this.dsCirurgia = dsCirurgia;
	}

	public boolean isDsApp() {
		return dsApp;
	}

	public void setDsApp(boolean dsApp) {
		this.dsApp = dsApp;
	}

	public boolean isDsCompleto() {
		return dsCompleto;
	}

	public void setDsCompleto(boolean dsCompleto) {
		this.dsCompleto = dsCompleto;
	}

	public boolean isDsGerado() {
		return dsGerado;
	}

	public void setDsGerado(boolean dsGerado) {
		this.dsGerado = dsGerado;
	}
	
	
}
