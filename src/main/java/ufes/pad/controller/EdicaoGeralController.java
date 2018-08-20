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

import ufes.pad.model.ImagemGeral;
import ufes.pad.model.LesaoGeral;
import ufes.pad.model.PacienteGeral;
import ufes.pad.repository.ImagemGeralRepository;
import ufes.pad.repository.LesaoGeralRepository;
import ufes.pad.repository.PacienteGeralRepository;

@ManagedBean
@ViewScoped
public class EdicaoGeralController {
	
	private PacienteGeral pacBuscado = new PacienteGeral();
	
	@Autowired 
	private PacienteGeralRepository pac_rep;
	
	@Autowired 
	private LesaoGeralRepository les_rep;
	
	@Autowired 
	private ImagemGeralRepository img_rep; 
	
	private LesaoGeral lesaoSelecionada;
	
	private ImagemGeral imgSelecionada;
	
	private List<LesaoGeral> pacLesoesDeletar = new ArrayList<LesaoGeral>();
	
	private List<ImagemGeral> pacImagensDeletar = new ArrayList<ImagemGeral>();	
  
    @PostConstruct
	public void pegaPacienteCartaoSus () {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
				
		String cartaoSus = request.getParameter("cartaosus");
		FacesContext context = FacesContext.getCurrentInstance();
						
		try {
			setPacBuscado(pac_rep.buscaPorCartaoSus(cartaoSus));			
			
			if (getPacBuscado()!=null) {
				context.addMessage(null, new FacesMessage("Paciente pronto para edição"));
				System.out.println("Paciente encontrado com sucesso para edição");
				
								
			} else {
				System.out.println("\n Paciente nao encontrado para edição\n");
				//context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Paciente não encontrado!", "  "));
			}
		} catch (Exception e) {			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}			
	}
    
    
    public String excluirPacienteGeral () {
    	FacesContext context = FacesContext.getCurrentInstance();
    	try {
    		for (LesaoGeral les : pacBuscado.getLesoes()) {
    			excluirImagensGeralServer(les.getImagens());
    		}
    		pac_rep.delete(pacBuscado);
    		context.addMessage(null, new FacesMessage("Paciente excluido com sucesso!"));
    	} catch (Exception e) {
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));
    		e.printStackTrace();
    		return null;
    	}
    	
    	return "/dashboard/visualizar_pacientes_gerais.xhtml?faces-redirect=true";
    }
    
    public void salvarAlteracoes () {
    	FacesContext context = FacesContext.getCurrentInstance();
    	try {
    		
			/*if (pacLesoes.isEmpty()) {
				pac_rep.save(pacBuscado);
			} else {								
				pacBuscado.setLesoes(pacLesoes);			
				pac_rep.save(pacBuscado);				
			}*/
    		
    		pac_rep.save(pacBuscado);			
			aplicarRemocoes();
    		
    		context.addMessage(null, new FacesMessage("Paciente salvo com sucesso!"));
    	} catch (Exception e) {
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));
    		e.printStackTrace();
    		
    	}    	
    	
    }
    
	static public void excluirImgGeralServer (ImagemGeral img) {
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			File file = new File("src/main/webapp/dashboard/imgLesoesGeral/"+img.getPath()); 
			System.out.println("Excluindo imagem: " + img.getPath());
    		file.delete();    	   
    	}catch(Exception e){    
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir a imagem do servidor. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
    		e.printStackTrace();    		
    	}		
	}   
	
	static public void excluirImagensGeralServer (List<ImagemGeral> imgs) {
		
		for (ImagemGeral img : imgs) {		
			excluirImgGeralServer(img);
		}
	}	
    
    public void excluirLesao () {
    	FacesContext context = FacesContext.getCurrentInstance();
    	try {
    		    		
    		if (!lesaoSelecionada.getImagens().isEmpty()) {    		
    			excluirImagensGeralServer(lesaoSelecionada.getImagens());
    		}
    		pacLesoesDeletar.add(lesaoSelecionada);
    		pacBuscado.getLesoes().remove(lesaoSelecionada);
    		context.addMessage(null, new FacesMessage("Lesao excluida com sucesso!"));
    	} catch (Exception e) {
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));
    		e.printStackTrace();    		
    	}
    	
    }
    
    public void excluirImgEdicao (LesaoGeral les) {
    	FacesContext context = FacesContext.getCurrentInstance(); 
    	try {
			pacImagensDeletar.add(imgSelecionada);
			les.getImagens().remove(imgSelecionada);
			
			System.out.println("Excluindo imagem da lesao: " + les.getRegiao() + " com path: " + imgSelecionada.getPath());
			
			context.addMessage(null, new FacesMessage("Imagem excluida com sucesso."));
    	} catch (Exception e) {
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));
    		e.printStackTrace();    		
    	}
    } 
    
    public void inserirNovaImagem (FileUploadEvent event) {
    	FacesContext context = FacesContext.getCurrentInstance();
    	  
    	try {
    		UploadedFile arq = event.getFile();	
    		InputStream in = new BufferedInputStream(arq.getInputstream());
			String pathImg = (pacBuscado.getCartao_sus() + "_" + new Date().getTime() + ".jpg");
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
    		les_rep.save(lesaoSelecionada);
    		
    	} catch (Exception e) { 
    		e.printStackTrace();
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema no envio da imagem", "  "));
		}
    
    	context.addMessage(null, new FacesMessage("Imagem adicionada com sucesso."));
    }
  
   
	private void aplicarRemocoes() {
		try {
			if (!pacLesoesDeletar.isEmpty()) {
				les_rep.delete(pacLesoesDeletar);
			}
			
			if (!pacImagensDeletar.isEmpty()) {
				img_rep.delete(pacImagensDeletar);
				excluirImagensGeralServer(pacImagensDeletar);				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
	public void atualizarImagens () {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Imagens atualizadas com sucesso!"));
	}
		
    

 // #############################################################################################################################################    
    
	public PacienteGeral getPacBuscado() {
		return pacBuscado;
	}

	public void setPacBuscado(PacienteGeral pacBuscado) {
		this.pacBuscado = pacBuscado;
	}


	public LesaoGeral getLesaoSelecionada() {
		return lesaoSelecionada;
	}


	public void setLesaoSelecionada(LesaoGeral lesaoSelecionada) {
		this.lesaoSelecionada = lesaoSelecionada;
	}


	public List<LesaoGeral> getPacLesoesDeletar() {
		return pacLesoesDeletar;
	}


	public void setPacLesoesDeletar(List<LesaoGeral> pacLesoesDeletar) {
		this.pacLesoesDeletar = pacLesoesDeletar;
	}


	public List<ImagemGeral> getPacImagensDeletar() {
		return pacImagensDeletar;
	}


	public void setPacImagensDeletar(List<ImagemGeral> pacImagensDeletar) {
		this.pacImagensDeletar = pacImagensDeletar;
	}


	public ImagemGeral getImgSelecionada() {
		return imgSelecionada;
	}


	public void setImgSelecionada(ImagemGeral imgSelecionada) {
		this.imgSelecionada = imgSelecionada;
	}   

}


