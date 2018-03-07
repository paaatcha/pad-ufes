package ufes.pad.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Lesao implements Serializable {

	/** Serialization id. */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	@Column(length=40, nullable = false)
	private String diagnostico_clinico;
	
	@Column(length=40, nullable = false)
	private String diagnostico_histo;
	
	@Column(length=40, nullable = false)
	private String topografia;
	
	private float diametro_maior;
	private float diametro_menor;
	
	@ManyToMany	(fetch = FetchType.EAGER)
	private List<Imagem> imagens;

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
		this.diagnostico_clinico = diagnostico_clinico;
	}

	public String getDiagnostico_histo() {
		return diagnostico_histo;
	}

	public void setDiagnostico_histo(String diagnostico_histo) {
		this.diagnostico_histo = diagnostico_histo;
	}

	public String getTopografia() {
		return topografia;
	}

	public void setTopografia(String topografia) {
		this.topografia = topografia;
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
	
}
