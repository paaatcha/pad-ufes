package ufes.pad.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Imagem;
import ufes.pad.model.Lesao;
import ufes.pad.model.Paciente;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
public class LesaoController {
	
	@Autowired
	private PacienteRepository pac_rep;
	
	private Paciente pacLesao;
	private Long idLesao = 0L;
	private String cartaoSusLesao = "N";
	private boolean visivelLesao = false;
	
	private List<Lesao> lesoes = new ArrayList<Lesao>();
	
	private List<Imagem> imagens = new ArrayList<Imagem>();
	
	private Lesao lesaoDada = new Lesao();
	
	public void adicionarLesaoPaciente () {		
		lesaoDada.setImagens(this.imagens);
		lesoes.add(lesaoDada);
		
		System.out.println("Inserindo lesão "+lesaoDada.getRegiao());
		
		imagens.clear();
		lesaoDada = new Lesao();
	}
	
	public String finalizar () {
		lesoes.clear();
		imagens.clear();
				
		return "dashboard/home.xhtml";
	}
	
	public void adicionar() {		
		this.pacLesao.setLesoes(lesoes);
		
	}
	
	public void processaImg(FileUploadEvent event) {
		try {
			UploadedFile arq = event.getFile();
			InputStream in = new BufferedInputStream(arq.getInputstream());
			File file = new File("src/main/webapp/dashboard/imgLesoes/" + arq.getFileName());
    		FileOutputStream fout = new FileOutputStream(file);

		while (in.available() != 0) {
			fout.write(in.read());
		}
			fout.close();
			FacesMessage msg = new FacesMessage("O Arquivo ", file.getName()+ " salvo.");
			FacesContext.getCurrentInstance().addMessage("msgUpdate", msg);
			
			Imagem img = new Imagem();
			img.setPath("imgLesoes/" + arq.getFileName());
			imagens.add(img);		
		
			System.out.println(img.getPath());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	public void buscaPacienteParaLesao() {
		try {
			if (getIdLesao() != 0L) {
				System.out.println(getIdLesao().getClass());
				setPacLesao(pac_rep.buscaPorId(getIdLesao()));
			} else if (getCartaoSusLesao().equals("N")) {
				setPacLesao(pac_rep.buscaPorCartaoSus(getCartaoSusLesao()));
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Você deve informar o ID ou cartão do SUS", "  "));
			}								
			
			if (getPacLesao()==null) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Nenhum paciente foi encontrado com este número de cartão. Tente visualizar todos para encontrá-lo, caso não encontre, ele não está registrado no banco.", "  "));
				this.setVisivelLesao(false);
			} else {
				this.setVisivelLesao(true);
			}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}			
	}

	public Paciente getPacLesao() {
		return pacLesao;
	}

	public void setPacLesao(Paciente pacLesao) {
		this.pacLesao = pacLesao;
	}

	public Long getIdLesao() {
		return idLesao;
	}

	public void setIdLesao(Long idLesao) {
		this.idLesao = idLesao;
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

	public List<Lesao> getLesoes() {
		return lesoes;
	}

	public void setLesoes(List<Lesao> lesoes) {
		this.lesoes = lesoes;
	}

	public Lesao getLesaoDada() {
		return lesaoDada;
	}

	public void setLesaoDada(Lesao lesaoDada) {
		this.lesaoDada = lesaoDada;
	}

	public List<Imagem> getImagens() {
		return imagens;
	}

	public void setImagens(List<Imagem> imagens) {
		this.imagens = imagens;
	}


	
	
}
