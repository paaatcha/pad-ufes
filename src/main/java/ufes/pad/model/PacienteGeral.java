package ufes.pad.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class PacienteGeral implements Serializable{

	/** Serialization id. */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private String cartao_sus;
	
	@OneToMany	(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn (name="pacienteId")
	private List<LesaoGeral> lesoes;

	public String getCartao_sus() {
		return cartao_sus;
	}

	public void setCartao_sus(String cartao_sus) {
		this.cartao_sus = cartao_sus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<LesaoGeral> getLesoes() {
		return lesoes;
	}

	public void setLesoes(List<LesaoGeral> lesoes) {
		this.lesoes = lesoes;
	}
	
}



