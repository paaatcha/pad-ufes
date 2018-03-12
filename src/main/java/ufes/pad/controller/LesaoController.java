package ufes.pad.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Paciente;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class LesaoController {
	
	@Autowired
	private PacienteRepository pac_rep;	
	private Paciente pacLesao;
	private Long idPac = 0L;
	private String cartaoSusLesao = "N";
	private boolean visivelLesao = false; 
	
	private Long idLesaoBanco = 0L;
	
	private List<String> imagens = new ArrayList<String>();
	
	public String concluir () {
		imagens.clear();
		visivelLesao = false;
		idPac = 0L;
		idLesaoBanco = 0L;
		cartaoSusLesao = "N";
		return "/dashboard/home.xhtml";
	}

	public void msgAddId () {
		FacesMessage msg = new FacesMessage("O ID adicionado.");
		FacesContext.getCurrentInstance().addMessage("msgUpdate", msg);		
	}
	
	public void processaImg(FileUploadEvent event) {
		
		try {
			UploadedFile arq = event.getFile();			
			InputStream in = new BufferedInputStream(arq.getInputstream());
			String nomeImg = "Pac_"+idPac+"_lesao_"+idLesaoBanco+"_"+arq.getFileName();
			File file = new File("src/main/webapp/dashboard/imgLesoes/" + nomeImg);
    		FileOutputStream fout = new FileOutputStream(file);

		while (in.available() != 0) {
			fout.write(in.read());
		}
			fout.close();
			FacesMessage msg = new FacesMessage("A imagem", file.getName()+ " foi salva com sucesso.");
			FacesContext.getCurrentInstance().addMessage("msgUpdate", msg);
			

			imagens.add("imgLesoes/" + nomeImg);
			System.out.println("Adicionando imagems...");

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	public void buscaPacienteParaLesao() {
		try {
			
			if (getIdPac() == 0L && getCartaoSusLesao().equals("N")) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Você deve informar o ID ou cartão do SUS", "  "));				
			} else {
				this.setVisivelLesao(true);
				this.pacLesao = (pac_rep.buscaPorId(getIdPac()));				
				if (pacLesao == null) {
					this.pacLesao = (pac_rep.buscaPorCartaoSus(getCartaoSusLesao()));
					if (pacLesao == null) {
						FacesContext context = FacesContext.getCurrentInstance();
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Nenhum paciente foi encontrado com este número de cartão. Tente visualizar todos para encontrá-lo, caso não encontre, ele não está registrado no banco.", "  "));
						this.setVisivelLesao(false);						
					}
				}				
			}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}			
	}

/* ###########################################  Getters and Setters ###################################################*/	
	
	public Paciente getPacLesao() {
		return pacLesao;
	}

	public void setPacLesao(Paciente pacLesao) {
		this.pacLesao = pacLesao;
	}

	public Long getIdPac() {
		return idPac;
	}

	public void setIdPac(Long idPac) {
		this.idPac = idPac;
	}

	public String getCartaoSusLesao() {
		return cartaoSusLesao;
	}

	public void setCartaoSusLesao(String cartaoSusLesao) {
		this.cartaoSusLesao = cartaoSusLesao;
	}

	public boolean isVisivelLesao() {
		return visivelLesao;
	}

	public void setVisivelLesao(boolean visivelLesao) {
		this.visivelLesao = visivelLesao;
	}

	public List<String> getImagens() {
		return imagens;
	}

	public void setImagens(List<String> imagens) {
		this.imagens = imagens;
	}


	public Long getIdLesaoBanco() {
		return idLesaoBanco;
	}


	public void setIdLesaoBanco(Long idLesaoBanco) {
		this.idLesaoBanco = idLesaoBanco;
	}


	

	
	
}
