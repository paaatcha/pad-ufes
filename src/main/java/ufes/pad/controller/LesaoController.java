package ufes.pad.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
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

import ufes.pad.model.Imagem;
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
	
	private List<Imagem> imagens = new ArrayList<Imagem>();
	private Imagem img = new Imagem();
	
	public String concluir () {
		imagens.clear();
		visivelLesao = false;
		idPac = 0L;
		idLesaoBanco = 0L;
		cartaoSusLesao = "N";
		return "/dashboard/home.xhtml";
	}

	public void msgAddId () {
		FacesMessage msg = new FacesMessage("O ID: " + idLesaoBanco + " foi adicionado.");
		FacesContext.getCurrentInstance().addMessage("msgUpdate", msg);		
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
			imagens.add(img);
			img = new Imagem ();
			System.out.println("Adicionando " + pathImg);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema no envio da imagem. Tente novamente. Caso não consiga, entre em contato com o administrador do sistema.", "  "));
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

	public List<Imagem> getImagens() {
		return imagens;
	}

	public void setImagens(List<Imagem> imagens) {
		this.imagens = imagens;
	}


	public Long getIdLesaoBanco() {
		return idLesaoBanco;
	}


	public void setIdLesaoBanco(Long idLesaoBanco) {
		this.idLesaoBanco = idLesaoBanco;
	}


	

	
	
}
