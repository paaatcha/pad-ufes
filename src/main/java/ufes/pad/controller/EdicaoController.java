package ufes.pad.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Imagem;
import ufes.pad.model.Lesao;
import ufes.pad.model.Paciente;
import ufes.pad.repository.ImagemRepository;
import ufes.pad.repository.LesaoRepository;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class EdicaoController {
	
	private Paciente pacBuscado = new Paciente();
	
	@Autowired 
	private PacienteRepository pac_rep;
	
	@Autowired
	private LesaoRepository les_rep;
	
	@Autowired
	private ImagemRepository img_rep;
	
	private boolean lesaoVazia;
	
	private boolean lesaoCompleta;
	
	private List<Lesao> pacLesoes = new ArrayList<Lesao>();
	
	private List<Imagem> pacImagens = new ArrayList<Imagem>();
	
	private List<Lesao> pacLesoesDeletar = new ArrayList<Lesao>();
	
	private List<Imagem> pacImagensDeletar = new ArrayList<Imagem>();	
	
	private Lesao lesao = new Lesao ();	
	
	private Imagem img = new Imagem ();	
	
	private Lesao lesaoSelecionada;	 
	
	private Imagem imgSelecionada; 
	
	private boolean pacRecadastro = false; 
    
    @PostConstruct
	public void pegaPacienteCartaoSus () {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
				
		String cartaoSus = request.getParameter("cartaosus");		
		String recadastro = request.getParameter("recadastro");
		
		try {
			setPacBuscado(pac_rep.buscaPorCartaoSus(cartaoSus));			
			
			if (getPacBuscado()!=null) {							
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Paciente pronto para edição"));
				System.out.println("Paciente encontrado com sucesso para edição");
				
				
				if (pacBuscado.getLesoes().isEmpty()) {
					this.lesaoVazia = true;
					this.lesaoCompleta = false; 
				} else {
					this.lesaoVazia = false;
					this.lesaoCompleta = true;
					pacLesoes = pacBuscado.getLesoes();					
				}
				
				if (recadastro.equals("sim")) {
					this.setPacRecadastro(true);					
					pacBuscado.setData_atendimento(new Date());
										
				}
				
			} else {
				System.out.println("\n Paciente nao encontrado para edição\n");
			}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}			
	}	
    
	public String editarPaciente () {		
		String ret = "/dashboard/visualizar_paciente.xhtml";
		FacesContext context = FacesContext.getCurrentInstance();
		try {
						
			if (pacLesoes.isEmpty()) {
				pac_rep.save(pacBuscado);
			} else {								
				pacBuscado.setLesoes(pacLesoes);			
				pac_rep.save(pacBuscado);				
			}
			
			aplicarRemocoes();
			
			context.addMessage(null, new FacesMessage("Paciente editado com sucesso."));
									
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Problema na comunicação com banco de dados. Tente novamente. Se o problema persistir, entre em contato com o administrador do sistema.", "  "));
			ret = null;
		}		
		return ret;
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
			
			getImg().setPath(pathImg);
			getPacImagens().add(getImg());
			setImg(new Imagem ());
			System.out.println("Adicionando " + pathImg);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema no envio da imagem. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}		
	}
	
	public void inserirImagemListaCompleta (FileUploadEvent event) {
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
			
			Lesao les = (Lesao) event.getComponent().getAttributes().get("lesEdicaoImg"); 
			img.setPath(pathImg);
			
					
			
			
			for (Lesao lesLista : pacLesoes) {
				lesLista.print();
				if (lesLista.getId() == les.getId()) {
					lesLista.getImagens().add(img);
					
					
				}
			}			
			img = new Imagem ();						
		}
		catch (Exception ex) {
			ex.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema no envio da imagem. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}	
		// Se der tudo certo...
		context.addMessage(null, new FacesMessage("Imagem adicionada com sucesso."));
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
	
	static public void excluirImgServer (Imagem img) {
		FacesContext context = FacesContext.getCurrentInstance();
		try{   		
    		
			File file = new File("src/main/webapp/dashboard/imgLesoes/"+img.getPath());        	
    		file.delete();    	   
    	}catch(Exception e){    
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir a imagem do servidor. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
    		e.printStackTrace();    		
    	}		
	}
	
	static public void excluirImagensServer (List<Imagem> imgs) {
		
		for (Imagem img : imgs) {		
			excluirImgServer(img);
		}
	}
	
	public void excluirImgEdicao (Lesao les) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {					
			pacImagensDeletar.add(imgSelecionada);
			les.getImagens().remove(imgSelecionada);
//			excluirImgServer(imgSelecionada);			
//			img_rep.delete(imgSelecionada);			
			context.addMessage(null, new FacesMessage("Imagem excluida com sucesso."));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir a imagem do servidor. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
		}
	}
	
	public void excluirLesao () {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			excluirImagensServer (lesaoSelecionada.getImagens());
			pacLesoesDeletar.add(lesaoSelecionada);
			pacLesoes.remove(lesaoSelecionada);
//			les_rep.delete(lesaoSelecionada);			
			context.addMessage(null, new FacesMessage("Lesão excluida com sucesso."));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir a lesão. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));			
		}
	}
	
	private void aplicarRemocoes() {
		try {
			if (!pacLesoesDeletar.isEmpty()) {
				les_rep.delete(pacLesoesDeletar);
			}
			
			if (!pacImagensDeletar.isEmpty()) {
				img_rep.delete(pacImagensDeletar);
				excluirImagensServer(pacImagensDeletar);				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void adicionarNovaLesao () {
		lesao = new Lesao();
		lesao.setImagens(new ArrayList<Imagem>());
		pacLesoes.add(lesao);		
	}
	

    

// #############################################################################################################################################
	
	public Paciente getPacBuscado() {
		return pacBuscado;
	}

	public void setPacBuscado(Paciente pacBuscado) {
		this.pacBuscado = pacBuscado;
	}

	public boolean isLesaoVazia() {
		return lesaoVazia;
	}

	public void setLesaoVazia(boolean lesaoVazia) {
		this.lesaoVazia = lesaoVazia;
	}

	public boolean isLesaoCompleta() {
		return lesaoCompleta;
	}

	public void setLesaoCompleta(boolean lesaoCompleta) {
		this.lesaoCompleta = lesaoCompleta;
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

	public List<Imagem> getPacImagens() {
		return pacImagens;
	}

	public void setPacImagens(List<Imagem> pacImagens) {
		this.pacImagens = pacImagens;
	}

	public Imagem getImg() {
		return img;
	}

	public void setImg(Imagem img) {
		this.img = img;
	}

	public Lesao getLesaoSelecionada() {
		return lesaoSelecionada;
	}

	public void setLesaoSelecionada(Lesao lesaoSelecionada) {		
		this.lesaoSelecionada = lesaoSelecionada;
	}

	public Imagem getImgSelecionada() {
		return imgSelecionada;
	}

	public void setImgSelecionada(Imagem imgSelecionada) {
		this.imgSelecionada = imgSelecionada;
	}

	public boolean isPacRecadastro() {
		return pacRecadastro;
	}

	public void setPacRecadastro(boolean pacRecadastro) {
		this.pacRecadastro = pacRecadastro;
	}

}

