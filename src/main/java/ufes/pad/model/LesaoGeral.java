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
public class LesaoGeral implements Serializable{
	/** Serialization id. */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private String regiao;
	
	private String diagnostico; 
	
	private String cresceu;
	
	private String cocou;
	
	private String sangrou;
	
	private String doeu;
	
	private String mudou;	
	
	private String relevo;
	
	private int idade;
	
	private String obs;
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn (name="lesaoId")
	private List<ImagemGeral> imagens;
	
	
	public void print() {
		System.out.println("---- LESAO GERAL----");
		System.out.println("ID: " + id);
		System.out.println("Idade: " + idade);
		System.out.println("Regiao: " + regiao);
		System.out.println("Cresceu: " + cresceu);
		System.out.println("Coçou: " + cocou);
		System.out.println("Sangrou: " + sangrou);
		System.out.println("Doeu: " + doeu);
		System.out.println("Mudou: " + mudou);
		System.out.println("relevo: " + relevo);
		
		
		System.out.println("--- IMAGENS ---");
		if (!imagens.isEmpty()) {
			for (ImagemGeral img : imagens) {
				img.print();
			}
		}
		System.out.println("-------------------------------------------\n");
		 
	}

	public String getCresceu() {
		return cresceu;
	}

	public void setCresceu(String cresceu) {
		this.cresceu = cresceu.toUpperCase();
	}

	public String getCocou() {
		return cocou;
	}

	public void setCocou(String cocou) {
		this.cocou = cocou.toUpperCase();
	}

	public String getSangrou() {
		return sangrou;
	}

	public void setSangrou(String sangrou) {
		this.sangrou = sangrou.toUpperCase();
	}

	public String getDoeu() {
		return doeu;
	}

	public void setDoeu(String doeu) {
		this.doeu = doeu.toUpperCase();
	}

	public String getMudou() {
		return mudou;
	}

	public void setMudou(String mudou) {
		this.mudou = mudou.toUpperCase();
	}

	public String getDiagnostico() {
		return diagnostico;
	}
	
		

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico.toUpperCase();
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao.toUpperCase();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ImagemGeral> getImagens() {
		return imagens;
	}

	public void setImagens(List<ImagemGeral> imagens) {
		this.imagens = imagens;
	}

	public String getRelevo() {
		return relevo;
	}

	public void setRelevo(String relevo) {
		this.relevo = relevo.toUpperCase();
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}	
}

