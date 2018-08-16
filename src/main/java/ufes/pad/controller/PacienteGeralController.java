package ufes.pad.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

import ufes.pad.model.ImagemGeral;
import ufes.pad.model.LesaoGeral;
import ufes.pad.model.PacienteGeral;
import ufes.pad.repository.PacienteGeralRepository;

@ManagedBean
@ViewScoped
public class PacienteGeralController {

	@Autowired 
	private PacienteGeralRepository pac_rep;	
	
	private PacienteGeral pac = new PacienteGeral();
	
	private List<LesaoGeral> lesoes = new ArrayList<LesaoGeral>();
	
	private List<ImagemGeral> imagens;
	
	private LesaoGeral lesao;
	
	private LesaoGeral lesaoSelecionada;
	
	private ImagemGeral imgSelecionada;
	
	private boolean instanciado = false; 
	
	
	
	public void instanciaNovaLesao () {
		FacesContext context = FacesContext.getCurrentInstance();
		if (pac.getCartao_sus().equals("")) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Você precisa prencher o cartão do SUS primeiro", "  "));
		} else {
			lesao = new LesaoGeral();
			imagens = new ArrayList<ImagemGeral>();
			lesao.setImagens(imagens);
			lesoes.add(lesao);
			pac.setLesoes(lesoes);
			instanciado = true;
			context.addMessage(null, new FacesMessage("Lesão pronta para ser adicionada", ""));
		}
	}
	
	public void excluirLesao () {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			
			if (lesaoSelecionada.getImagens() != null) {
				for (ImagemGeral img : lesaoSelecionada.getImagens()) {
					// Excluindo imagem do servidor
					File file = new File("src/main/webapp/dashboard/imgLesoesGeral/" + img.getPath());
					file.delete();
					System.out.println("Excluindo imagem da lesao: " + img.getPath());
				}	
			}
			pac.getLesoes().remove(lesaoSelecionada);
			System.out.println("Lesão excluida");
			
			if (pac.getLesoes().isEmpty()) {
				instanciado = false;
			}
			
			context.addMessage(null, new FacesMessage("Lesão excluida com sucesso."));
			
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na exclusão da lesão", "  "));
    		e.printStackTrace();    		
		}
	}
		
	public void inserirNovaImagem (FileUploadEvent event) {
    	FacesContext context = FacesContext.getCurrentInstance();
  	  
    	try {
    		UploadedFile arq = event.getFile();	
    		InputStream in = new BufferedInputStream(arq.getInputstream());
			String pathImg = (pac.getCartao_sus() + "_" + new Date().getTime() + ".jpg");
			File file = new File("src/main/webapp/dashboard/imgLesoesGeral/" + pathImg);
    		FileOutputStream fout = new FileOutputStream(file);
    		
    		while (in.available() != 0) {
    			fout.write(in.read());
    		}
    		 
    		fout.close();
    		
    		lesaoSelecionada = (LesaoGeral) event.getComponent().getAttributes().get("lesaoParaNovaImg");
    		ImagemGeral novaImg = new ImagemGeral();
    		novaImg.setPath(pathImg);
    		lesaoSelecionada.getImagens().add(novaImg);    		
    		
    	} catch (Exception e) { 
    		e.printStackTrace();
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema no envio da imagem", "  "));
		}
    
    	context.addMessage(null, new FacesMessage("Imagem adicionada com sucesso."));
    }
	
    public void excluirImg (LesaoGeral les) {
    	FacesContext context = FacesContext.getCurrentInstance(); 
    	try {
    		
    		// Excluindo imagem do servidor
    		File file = new File("src/main/webapp/dashboard/imgLesoesGeral/" + imgSelecionada.getPath());
    		file.delete();  
    		
			les.getImagens().remove(getImgSelecionada());
			System.out.println("Excluindo imagem da lesao: " + les.getRegiao() + " com path: " + getImgSelecionada().getPath());
			
			context.addMessage(null, new FacesMessage("Imagem excluida com sucesso."));
    	} catch (Exception e) {
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));
    		e.printStackTrace();    		
    	}
    } 	
    
    public String salvar () {
    	FacesContext context = FacesContext.getCurrentInstance(); 
    	try {
    		pac.setAuditado(true);
    		pac_rep.save(pac);
    		context.addMessage(null, new FacesMessage("Paciente salvo no banco com sucesso."));
    	} catch (Exception e) {
    		e.printStackTrace();
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));
    		return null;
    	}
    	
    	return "novo_paciente_geral.xhtml?faces-redirect=true";
    }
    
	public void configuraUnirest () { 
		Unirest.setObjectMapper(new ObjectMapper() {
		    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		                = new com.fasterxml.jackson.databind.ObjectMapper();
	
		    public <T> T readValue(String value, Class<T> valueType) {
		        try {
		            return jacksonObjectMapper.readValue(value, valueType);
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
		    }
	
		    public String writeValue(Object value) {
		        try {
		            return jacksonObjectMapper.writeValueAsString(value);
		        } catch (JsonProcessingException e) {
		            throw new RuntimeException(e);
		        }
		    }
		});	
	}
	
	static public int totalLesoes (List<PacienteGeral> pacs) {
		int numLesoes = 0;
		for (PacienteGeral pac : pacs) {
			numLesoes += pac.numeroLesoes();
		}
		return numLesoes;
	}	
	
	
	public String enviarPacientesGeralBancoUfes () {		 
		configuraUnirest ();
		int i = 0;
		FacesContext context = FacesContext.getCurrentInstance();
		
		System.out.println("\n---- Iniciando envio pacientes gerais ----\n");
		
		try {
			List <PacienteGeral> pacs = pac_rep.findAll();			
			
			if (pacs == null) {
				return null;
			}
			
			for (PacienteGeral pac : pacs) {	
				i++;
				HttpResponse<JsonNode> postResponse = Unirest.post("http://labcin1.ufes.br/APIrequisicoes/novo_paciente_geral")
//				HttpResponse<JsonNode> postResponse = Unirest.post("http://localhost:8080/APIrequisicoes/novo_paciente_geral")
				.header("accept", "application/json")
				.header("Content-Type", "application/json")
				.body(pac)
				.asJson();
				
				if (postResponse.getStatus() == 200) {
					System.out.println(i + " Paciente geral: " + pac.getCartao_sus() + " enviado com sucesso...");					
				} else {
					System.out.println(" Paciente geral: " + pac.getCartao_sus() + " problema na requisicao...");
				}
				
			}
			
			context.addMessage(null, new FacesMessage(i + " pacientes gerais foram enviados com sucesso para o servidor da UFES.", ""));
			
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar pacientes gerais para o banco da UFES.", "  "));
		}
		
		return "Sucesso";
	}

	public PacienteGeral getPac() {
		return pac;
	}

	public void setPac(PacienteGeral pac) {
		this.pac = pac;
	}

	public List<LesaoGeral> getLesoes() {
		return lesoes;
	}

	public void setLesoes(List<LesaoGeral> lesoes) {
		this.lesoes = lesoes;
	}

	public LesaoGeral getLesao() {
		return lesao;
	}

	public void setLesao(LesaoGeral lesao) {
		this.lesao = lesao;
	}

	public List<ImagemGeral> getImagens() {
		return imagens;
	}

	public void setImagens(List<ImagemGeral> imagens) {
		this.imagens = imagens;
	}

	public boolean isInstanciado() {
		return instanciado;
	}

	public void setInstanciado(boolean instanciado) {
		this.instanciado = instanciado;
	}

	public LesaoGeral getLesaoSelecionada() {
		return lesaoSelecionada;
	}

	public void setLesaoSelecionada(LesaoGeral lesaoSelecionada) {
		this.lesaoSelecionada = lesaoSelecionada;
	}

	public ImagemGeral getImgSelecionada() {
		return imgSelecionada;
	}

	public void setImgSelecionada(ImagemGeral imgSelecionada) {
		this.imgSelecionada = imgSelecionada;
	}
	
	
/* ###########################################  Getters and Setters ###################################################*/



}
