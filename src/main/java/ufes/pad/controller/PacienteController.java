package ufes.pad.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
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
				"CHINA", "JAPÃO", "CORÉIA", "HUNGRIA", "DINAMARCA", "COLÔMBIA", "LATINA", "AFRICA", "PARAGUAI", "VENEZUELA", "HOLANDA"};
		
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
		String[] diag = new String[] {"ACNE VULGAR L70.0", "CARCINOMA BASO CELULAR C80", "CARCINOMA ESPINO CELULAR C44.9", "CELULITE NÃO ESPECIFICADA L03.9", "CERATOACANTOMA D23", "CERATOSE ACTÍNICA L57.0",
				"CERATOSE SEBORREICA L82", "CICATRIZ HIPERTRÓFICA L91.8", "CISTO EPIDÉRMICO L72.0", "CISTO MUCOSO K09.9", "CISTO PILONIDAL COM ABSCESSO L05.0",	"CISTO PILONIDAL SEM ABSCESSO L05.9",
				"CISTO SEBÁCEO L72.1", "CORNO CUTÂNEO L75.8", "DERMATITE ATÓPICA L20.9", "DERMATITE DE CONTATO L25", "DERMATITE SEBORREICA L20", "DOENÇA DE BOWEN", "ERITEMA MULTIFORME L51", "FARMACODERMIA L27.0",
				"FIBROMA (ACROCORDON) D21.9", "HANSENÍASE A30.9", "HEMANGIOMA D18.0", "HERPES SIMPLES B00.1", "HERPES ZOOSTER B02.9", "IMPETIGO L01.0", "LENTIGO MALIGNO D03.9", "LESÃO NÃO ESPECIFICADA L99",
				"LIPOMA D17", "LÍQUEN PLANO L66.1", "MELANOMA C43.9", "MELANOSE SOLAR L81.1", "MICOSE NÃO ESPECIFICADA B49", "MILIÁRIA NÃO ESPECIFICADA L74.3", "NEVO MELANOCÍTICO D22.9", "PITIRÍASE ALBA L130.5",
				"PITIRÍASE LIQUINOIDE CRÔNICA L41.1", "PITIRÍASE VERSICOLOR B36.0", "PSORÍASE VULGAR L40.0", "ROSÁCEA NÃO ESPECIFICADA L71.9", "SERINGOMA D23.9", "TINEA CORPORIS B35.4", "ÚLCERA CRÔNICA DE PELE NÃO ESPECIFICADA L98.4",
				"VARICELA B01.9", "VERRUGA B07", "VITILIGO L80"};
		
		for (String s : diag){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}
	
	public List<String> completarRegiaoLesao (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] diag = new String[] {"FRONTAL", "PARIENTAL", "OCCIPTAL", "TEMPORAL", "AURICULAR", "MASTÓIDEA", "ORBITÁRIA", "NASAL", "ZIGOMÁTICA", "BUCAL", "MASSETERINA", "MENTONIANA",
				"SUBMANDIBULAR", "TARINGEA", "TIREÓIDEA", "TRAQUEAL", "ESTERNOCLEIDOMASTÓIDEA", "TRIGONO-LATERAL DO PESCOÇO", "SACRAL", "GLÚTEA", "ANAL", "PERINEAL", "UROGENITAL", "PUDENDA",
				"VENTRAL DO PESCOÇO", "LATERAL DO PESCOÇO", "MAMÁRIA", "ESTERNAL", "TORÁCICA LATERAL", "ABDOMINAL CRANIAL", "UMBILICAL", "ABDOMINAL CAUDAL", "INGUINAL", "DELTÓIDEA", "FOSSA AXILAR",
				"BRANQUIAL", "CUBITAL", "ANTEBRANQUIAL", "PALMAR", "DIGITOPALMAR", "TRIGONO-FEMORAL", "FEMORAL ANTERIOR", "JOELHO", "PATELAR", "DORSAL DO PÉ", 
				"DIGITAL DO PÉ", "VERTEBRAL", "ESCAPULAR", "INFRA-ESCAPULAR", "LOMBAR", "DELTÓIDEA", "BRANQUIAL POSTERIOR", "ANTEBRANQUIAL POSTERIOR", "DORSAL DA MÃO",
				"DORSAL DOS DEDOS", "UNGUEAL", "FEMORAL DORSAL", "JOELHO POSTERIOR", "FOSSA POPLITEA", "CALCÂNEA", "PLANTA", "MALEOLAR", "SOLAR", "CRURAL POSTERIOR"};
		
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
				"CAMINHONEIRO", "PEDREIRO", "FUNCIONARIO PUBLICO"};
		
		for (String s : diag){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;
	}	
	
	public void checarPacCadastrado() {
		Paciente p1 = pac_rep.buscaPorCartaoSus(this.pac.getCartao_sus());
		
		if (p1 != null) {
			try {
				FacesContext fContext = FacesContext.getCurrentInstance();				
				ExternalContext extContext = fContext.getExternalContext();
				extContext.redirect(extContext.getRequestContextPath() + "/dashboard/editar_paciente.xhtml?cartaosus=" + pac.getCartao_sus() + "&recadastro=sim");
				
				//FacesContext context = FacesContext.getCurrentInstance();
				//context.addMessage(null, new FacesMessage("Este paciente já está cadastrado no banco. Por favor, cheque se o nome do mesmo é " + p1.getNome_completo() + "\nSe sim, atualize apenas os campos necessários."));
				
			} catch (Exception ex) {
				System.out.println("Ocorreu algum erro na verificação se o paciente ja esta cadastrado no banco. Erro no ajax do checarPacCadastrado");
			}
			
			
		}
	}
	

	
	public void inserirLesao () {		
		System.out.println(lesao.getDiagnostico_clinico() +" "+lesao.getRegiao()+" "+lesao.getDiametro_maior()+" "+lesao.getDiametro_menor());
		FacesContext context = FacesContext.getCurrentInstance();		
		
		if (lesao.getRegiao() != null && lesao.getDiagnostico_clinico() != null && lesao.getProcedimento() != null) {
			lesao.setImagens(pacImagens);		
			lesao.setData_atendimento(this.pac.getData_atendimento());
			lesao.setLocal_atendimento(this.pac.getLocal_atendimento());
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
	
	static public int totalLesoes (List<Paciente> pacs) {
		int numLesoes = 0;
		for (Paciente pac : pacs) {
			numLesoes += pac.numeroLesoes();
		}
		return numLesoes;
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
		boolean isOk = true;
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
				
				isOk = enviaImagensLesoes (pac);
				
				if (postResponse.getStatus() == 200 && isOk) {
					System.out.println(i + " Paciente " + pac.getNome_completo() + " enviado com sucesso...\n");					
				} else {
					System.out.println(" Paciente " + pac.getNome_completo() + " problema na requisicao...\n");
				}
				
			}
			
			context.addMessage(null, new FacesMessage(i + " pacientes foram enviados com sucesso para o servidor da UFES.", ""));
			
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar pacientes para o banco da UFES.", "  "));
		}
		
		return "Sucesso";
	}	
	
	private boolean enviaImagensLesoes (Paciente pac) {
		
		
		try {
			for (Lesao L : pac.getLesoes()) {
			
				for (Imagem img : L.getImagens()) {
					
					String imgPath = "src/main/webapp/dashboard/imgLesoes/" + img.getPath();
					
					HttpResponse<JsonNode> postImgResponse = Unirest.post("http://labcin1.ufes.br/APIrequisicoes/novo_imagem_lesao")
//					HttpResponse<JsonNode> postImgResponse = Unirest.post("http://localhost:8080/APIrequisicoes/novo_imagem_lesao")							
					.header("accept", "application/json")
					.field("path", imgPath)
					.field("imagem", new File(imgPath))
					.asJson();
					
					if (postImgResponse.getStatus() != 200) {
						System.out.println("Problema no envio das imagens do paciente: " + pac.getNome_completo());
						return false;					
					} else {
						System.out.println("Imagem adicionada...");
					}
					
				}			
				
			}
		} catch (Exception e) {
			
			FacesContext context = FacesContext.getCurrentInstance();
			
			if (e instanceof FileNotFoundException) {
				System.out.println("Imagem não foi encontrada no path informada");context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A imagem do paciente nao foi encontrada.", "  "));
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar imagens dos pacientes para o banco da UFES.", "  "));
				System.out.println("Problema estrutural no envio das imagens do paciente: " + pac.getNome_completo());
			}
			
			e.printStackTrace();	
			
			
			return false;
		}
		
		return true;
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
