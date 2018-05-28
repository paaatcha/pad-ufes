package ufes.pad.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Lesao implements Serializable {

	/** Serialization id. */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	@Column(length=100)
	private String diagnostico_clinico;
	
	@Column(length=100)
	private String diagnostico_histo = "SEM RESULTADO";
	
	@Column(length=100, nullable = false)
	private String regiao;
	
	@Column(length=150, nullable = false)
	private String procedimento;
	
	@Column(length=500)
	private String obs = "NENHUMA";
	
	private float diametro_maior;
	private float diametro_menor;
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn (name="lesaoId")
	private List<Imagem> imagens;	
	
	
	static public void printLesao (Lesao les) {
		System.out.println("---- LESAO ----\n");
		System.out.println("ID: " + les.id);
		System.out.println("Regiao: " + les.regiao);
		System.out.println("Diagnostico Clinico: " + les.diagnostico_clinico);
		System.out.println("Diagnostico Histopatologico: " + les.diagnostico_histo);
		System.out.println("Diametro maior: " + les.diametro_maior);
		System.out.println("Diametro menor: " + les.diametro_menor);
		System.out.println("Procedimento: " + les.procedimento + "\n");
		
		
		System.out.println("--- IMAGENS ---\n");
		if (!les.getImagens().isEmpty()) {
			for (Imagem img : les.getImagens()) {
				Imagem.printImagem(img);
			}
		}
		 
	}	
	
	public boolean possuiImagem() {
		return !this.imagens.isEmpty();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDiagnostico_clinico() {
		return diagnostico_clinico;
	}

	public void setDiagnostico_clinico(String diagnostico_clinico) {
		this.diagnostico_clinico = diagnostico_clinico.toUpperCase();
	}

	public String getDiagnostico_histo() {
		return diagnostico_histo;
	}

	public void setDiagnostico_histo(String diagnostico_histo) {
		this.diagnostico_histo = diagnostico_histo.toUpperCase();
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao.toUpperCase();
	}

	public float getDiametro_maior() {
		return diametro_maior;
	}

	public void setDiametro_maior(float diametro_maior) {
		this.diametro_maior = diametro_maior;
	}

	public float getDiametro_menor() {
		return diametro_menor;
	}

	public void setDiametro_menor(float diametro_menor) {
		this.diametro_menor = diametro_menor;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Imagem> getImagens() {
		return imagens;
	}

	public void setImagens(List<Imagem> imagens) {
		this.imagens = imagens;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs.toUpperCase();
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento.toUpperCase();
	}	
	
}


