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

import ufes.pad.model.Imagem;
import ufes.pad.model.Lesao;
import ufes.pad.model.Paciente;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class PacienteController {

	private Paciente pac = new Paciente();
	private List<Paciente> todosPacs;
	
	
	@Autowired 
	private PacienteRepository pac_rep;	
		
	private Lesao lesaoSelecionada;	
	
	private List<Lesao> pacLesoes = new ArrayList<Lesao>();
	private List<Imagem> pacImagens = new ArrayList<Imagem>();	
	private Lesao lesao = new Lesao ();	
	private Imagem img = new Imagem (); 
	private List<String> imgsPath; 
	
	
	
	public List<String> completarEstados (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] estados = new String[] {"ACRE","ALAGOAS","AMAPÁ","AMAZONAS","BAHIA","CEARÁ","DISTRITO FEDERAL","ESPÍRITO SANTO","GOIÁS","MARANHÃO",
				"MATO GROSSO","MATO GROSSO DO SUL","MINAS GERAIS","PARÁ","PARAÍBA","PARANÁ","PERNAMBUCO","PIAUÍ","RIO DE JANEIRO",
				"RIO GRANDE DO NORTE","RIO GRANDE DO SUL","RONDÔNIA","RORAIMA","SANTA CATARINA","SÃO PAULO","SERGIPE","TOCANTINS"};
		
		for (String s : estados){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}  
	
	public List<String> completarOrigemFamiliar (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] estados = new String[] {"ALEMANHA", "POMERANO", "PORTUGAL", "ITÁLIA", "BRASIL", "ESTADOS UNIDOS", "ESPANHA", "MÉXICO", "NORUEGA", "FRANÇA",
				"OUTRO", "NÃO SABE", "NAO SABE", "INGLATERRA", "POLÔNIA", "CANADÁ", "ARGENTINA", "URUGUAI", "CHILE", "EQUADOR", "ÁRABE", "EUROPA", "ASIA",
				"CHINA", "JAPÃO", "CORÉIA", "HUNGRIA", "DINAMARCA", "COLÔMBIA", "LATINA", "AFRICA", "PARAGUAI", "VENEZUELA"};
		
		for (String s : estados){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}
	
	public List<String> completarDiagnosticoLesao (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] diag = new String[] {"CARCINOMA BASOCELULAR", "CARCINOMA ESPINOCELULAR", "DOENÇA DE BOWN", "CERATOSE ACTÍNICA", "LENTIGO", "MICOSE CUTANEA",
				"NEVO", "NEVO MELANOCÍTICO DISPLÁSICO", "HANSENIASE", "CERATOACANTOMA", "DERMATOFIBROMA", "CROMOBLASTOMICOSE",
				"MELANOMA", "DERMATO FIBROSSARCOMA", "CORNO CUTÂNEO", "PSORIASE", "CROMOMICOSE", "TRICOEPITELIOMA"};
		
		for (String s : diag){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}
	
	public List<String> completarProfissao (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] diag = new String[] {"APOSENTADO", "LAVRADOR", "ESTUDANTE", "PROFESSOR", "DONA DE CASA", "COSTUREIRA", "COMERCIANTE", "DOMESTICA",
				"CAMINHONEIRO"};
		
		for (String s : diag){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}	
	
	public void inserirLesao () {		
		System.out.println(lesao.getDiagnostico_clinico() +" "+lesao.getRegiao()+" "+lesao.getDiametro_maior()+" "+lesao.getDiametro_menor());
		FacesContext context = FacesContext.getCurrentInstance();		
		
		if (lesao.getRegiao() != null && lesao.getDiagnostico_clinico() != null && lesao.getProcedimento() != null) {
			lesao.setImagens(pacImagens);						
			pacLesoes.add(lesao);			
			lesao = new Lesao ();		
			pacImagens = new ArrayList<Imagem>();			
			context.addMessage(null, new FacesMessage("Lesão adicionada com sucesso."));			
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Você não preencheu todos os campos obrigatorios de uma lesão. Verifique se esqueceu algum campo e tente novamente!", "  "));
		}
		lesao = null;
		lesao = new Lesao();
	}
	
	static public void printImgs (List<Imagem> imgs) {
		System.out.println("Imprimindo imagens");
		for (Imagem img: imgs) {
			System.out.println(img.getPath());
		}
	}
	
	static public void printLesoes (List<Lesao> lesoes) {
		System.out.println("Imprimindo lesoes");
		for (Lesao les: lesoes) {
			System.out.println("Regiao: " + les.getRegiao() + " - " + "Diagnostico: " + les.getDiagnostico_clinico());
			printImgs(les.getImagens());
		}
	}
	
	public void inserirImagemLista (FileUploadEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			UploadedFile arq = event.getFile();		
			InputStream in = new BufferedInputStream(arq.getInputstream());
			String pathImg = (new Date().getTime())+ "_" + arq.getFileName();
			File file = new File("src/main/webapp/dashboard/imgLesoes/" + pathImg);
    		FileOutputStream fout = new FileOutputStream(file);

		while (in.available() != 0) {
			fout.write(in.read());
		}
			fout.close();			
			context.addMessage(null, new FacesMessage("Imagem adicionada com sucesso."));
			
			img.setPath(pathImg);
			pacImagens.add(img);
			img = new Imagem ();
			System.out.println("Adicionando " + pathImg);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema no envio da imagem. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}		
	}	

	public void excluirImagensServer (List<Imagem> imgs) {
		
		for (Imagem img : imgs) {		
			try{
	    		
	    		File file = new File("src/main/webapp/dashboard/imgLesoes/"+img.getPath());
	        	
	    		if(file.delete()){
	    			System.out.println(file.getName() + " foi deletado");
	    		}else{
	    			System.out.println("Problema para deletar o arquivo");
	    		}
	    	   
	    	}catch(Exception e){ 
	    		
	    		e.printStackTrace();
	    		
	    	}		
		}
	}
	
	public void excluirLesao () {
		excluirImagensServer (lesaoSelecionada.getImagens());
		System.out.println("Excluindo lesão "+lesaoSelecionada.getRegiao());
		this.pacLesoes.remove(this.lesaoSelecionada);
	}
	
	public String salvarPaciente () {		
		String ret = "/dashboard/cadastar_pacientes.xhtml";
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			Paciente p1 = pac_rep.buscaPorCartaoSus(this.pac.getCartao_sus());	
			
			if (p1 == null) {				
				pac_rep.save(this.pac);
				context.addMessage(null, new FacesMessage("Paciente cadastrado com sucesso. \nID do Paciente: " + pac.getId()));				
				pac = new Paciente();
				
			} else {
				ret = null;  
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Este cartão do SUS já está cadastrado! Visualize o paciente na página de visualização.", "  "));
			}						
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Problema na comunicação com banco de dados. Tente novamente. Se o problema persistir, entre em contato com o administrador do sistema.", "  "));
			ret = null;
		}		
		return ret;
	}
	
	
	public String salvarPacienteAntigo () {		
		String ret = "/dashboard/cadastar_pacientes_antigos.xhtml";
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			Paciente p1 = pac_rep.buscaPorCartaoSus(this.pac.getCartao_sus());	
			
			if (pacLesoes.isEmpty()) {
				ret =  null;
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Você está cadastrando um paciente sem lesões adicionadas! Essa operação não é permitida.", "  "));
			} else if (p1 == null) {		
				printLesoes(pacLesoes);
				this.pac.setLesoes(pacLesoes);
				pac_rep.save(this.pac);
				context.addMessage(null, new FacesMessage("Paciente cadastrado com sucesso. \nID do Paciente: " + pac.getId()));
				pacLesoes.clear();
				pac = new Paciente();
				lesao = new Lesao();
			} else {
				ret = null;  
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Este cartão do SUS já está cadastrado! Visualize o paciente na página de visualização.", "  "));

			}			
						
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Problema na comunicação com banco de dados. Tente novamente. Se o problema persistir, entre em contato com o administrador do sistema.", "  "));
			ret = null;
		}		
		return ret;
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
	
	
	public String enviarPacientesBancoUfes () {		 
		configuraUnirest ();
		int i = 0;
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println("\n---- Iniciando envio pacientes ----\n");
		try {
			List <Paciente> pacs = pac_rep.findAll();			
			
			if (pacs == null) {
				return null;
			}
			
			for (Paciente pac : pacs) {	
				i++;
				HttpResponse<JsonNode> postResponse = Unirest.post("http://labcin1.ufes.br/APIrequisicoes/novo_paciente")
//				HttpResponse<JsonNode> postResponse = Unirest.post("http://localhost:8080/APIrequisicoes/novo_paciente")
				.header("accept", "application/json")
				.header("Content-Type", "application/json")
				.body(pac)
				.asJson();
				
				if (postResponse.getStatus() == 200) {
					System.out.println(i + " Paciente " + pac.getNome_completo() + " enviado com sucesso...");					
				} else {
					System.out.println(" Paciente " + pac.getNome_completo() + " problema na requisicao...");
				}
				
			}
			
			context.addMessage(null, new FacesMessage(i + " pacientes foram enviados com sucesso para o servidor da UFES.", ""));
			
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar pacientes para o banco da UFES.", "  "));
		}
		
		return "Sucesso";
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

	public List<Paciente> getTodosPacs() {
		return todosPacs;
	}

	public void setTodosPacs(List<Paciente> todosPacs) {
		this.todosPacs = todosPacs;
	}

	public Lesao getLesao() {
		return lesao;
	}

	public void setLesao(Lesao lesao) {
		this.lesao = lesao;
	}

	public List<Lesao> getPacLesoes() {
		return pacLesoes;
	}

	public void setPacLesoes(List<Lesao> pacLesoes) {
		this.pacLesoes = pacLesoes;
	}

	public Lesao getlesaoSelecionada() {
		return lesaoSelecionada;
	}

	public void setlesaoSelecionada(Lesao lesaoSelecionada) {
		this.lesaoSelecionada = lesaoSelecionada;
	}

	public List<String> getImgsPath() {
		return imgsPath;
	}

	public void setImgsPath(List<String> imgsPath) {
		this.imgsPath = imgsPath;
	}


}
