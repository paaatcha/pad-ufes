package ufes.pad.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Paciente implements Serializable {

	/** Serialization id. */
	private static final long serialVersionUID = 1L;	
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	@Column(length=100, nullable = false)
	private String local_atendimento;	
	
	@Temporal(TemporalType.DATE)
	private Date data_atendimento = new Date();
	
	@Column(length=15, nullable = false)
	private String prontuario;
	
	@Column(length=150, nullable = false)
	private String nome_completo;
	
	@Column(length=100, nullable = false)
	private String local_nascimento;
	
	@Column(length=40, nullable = false)
	private String estado_nascimento = "ESPÍRITO SANTO";
	
	@Temporal(TemporalType.DATE)
	private Date data_nascimento;

	@Column(length=150, nullable = false)
	private String nome_mae;
	
	@Column(length=250, nullable = false)	
	private String endereco;
	
	@Column(length=100, nullable = false)
	private String atv_principal;
	
	@Column(length=5, nullable = false) 
	private String escolaridade;
	
	@Column(length=10, nullable = false)
	private String fumo;
	
	@Column(length=10, nullable = false)
	private String bebida;
	
	@Column(length=10, nullable = false)
	private String renda;
	
	@Column(length=30, nullable = false)
	private String origem_familiar_mae = "NÃO SABE";	
	
	@Column(length=30, nullable = false)
	private String origem_familiar_pai= "NÃO SABE";
	
	@Column(length=40, nullable = false)
	private String agrotoxico = "NENHUM";	
		
	@Column(length=20, nullable = false)
	private String cartao_sus;	
	
	@OneToMany	(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn (name="pacienteId")
	private List<Lesao> lesoes;	
	
	private char diabetes;
	
	private char anticoagulante;
	
	@Column(length=100, nullable = false)
	private String alergia = "NÃO";	
	
	private String obs = "NENHUMA";
	
	private float pres_art_sistolica;
	
	private float pres_art_diastolica;
	
	@Column(length=5, nullable = false)
	private String num_comodos;
	
	private int num_pessoas_casa;
	
	private char sexo;
	
	private char agua_encanada;
	
	private char rede_esgoto;
	
	private int idade_inicio_atv;
	
	private int tempo_endereco;
	
	private char estado_civil;
	
	private char destrofia_solar;
	
	private int exp_sol;
	
	private char hora_exp_sol;
	
	private char chapeu;
	
	private char manga_cumprida;
	
	private char calca_cumprida;
	
	private char filtro_solar;
	
	private char hist_cancer_pele;
	
	private char hist_cancer;
	
	private int num_vezes_atendido;
	
	private char tipo_pele;
	
	private char HAS;
	
	// Atributos que não vão para o banco
	@Transient
	private int idade;
	
	static public void printPaciente (Paciente pac) {
		System.out.println("--- PACIENTE ---\n");
		System.out.println("ID: " + pac.id);
		System.out.println("Nome: " + pac.nome_completo);
		System.out.println("Cartao SUS: " + pac.cartao_sus + "\n");
		
		System.out.println("--- LESOES ---\n");
		for (Lesao les : pac.lesoes) {
			Lesao.printLesao(les);
		}
	}
/* ###########################################  Getters and Setters ###################################################*/	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocal_atendimento() {
		return local_atendimento;
	}

	public void setLocal_atendimento(String local_atendimento) {
		this.local_atendimento = local_atendimento.toUpperCase();
	}

	public Date getData_atendimento() {
		return data_atendimento;
	}

	public void setData_atendimento(Date data_atendimento) {
		this.data_atendimento = data_atendimento;
	}

	public String getNome_completo() {
		return nome_completo;
	}

	public void setNome_completo(String nome_completo) {
		this.nome_completo = nome_completo.toUpperCase();
	}

	public Date getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(Date data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNome_mae() {
		return nome_mae;
	}

	public void setNome_mae(String nome_mae) {
		this.nome_mae = nome_mae.toUpperCase();
	}

	public String getLocal_nascimento() {
		return local_nascimento;
	}

	public void setLocal_nascimento(String local_nascimento) {
		this.local_nascimento = local_nascimento.toUpperCase();
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco.toUpperCase();
	}

	public char getSexo() {
		return sexo;
	}

	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	public int getTempo_endereco() {
		return tempo_endereco;
	}

	public void setTempo_endereco(int tempo_endereco) {
		this.tempo_endereco = tempo_endereco;
	}

	public char getEstado_civil() {
		return estado_civil;
	}

	public void setEstado_civil(char estado_civil) {
		this.estado_civil = estado_civil;
	}


	public String getEstado_nascimento() {
		return estado_nascimento;
	}


	public void setEstado_nascimento(String estado_nascimento) {
		this.estado_nascimento = estado_nascimento.toUpperCase();
	}


	public String getProntuario() {
		return prontuario;
	}


	public void setProntuario(String prontuario) {
		this.prontuario = prontuario;
	}


	public String getAtv_principal() {
		return atv_principal;
	}


	public void setAtv_principal(String atv_principal) {
		this.atv_principal = atv_principal.toUpperCase();
	}


	public int getIdade_inicio_atv() {
		return idade_inicio_atv;
	}


	public void setIdade_inicio_atv(int idade_inicio_atv) {
		this.idade_inicio_atv = idade_inicio_atv;
	}


	public String getEscolaridade() {
		return escolaridade;
	}


	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}


	public int getNum_pessoas_casa() {
		return num_pessoas_casa;
	}


	public void setNum_pessoas_casa(int num_pessoas_casa) {
		this.num_pessoas_casa = num_pessoas_casa;
	}


	public String getNum_comodos() {
		return num_comodos;
	}


	public void setNum_comodos(String num_comodos) {
		this.num_comodos = num_comodos;
	}


	public char getAgua_encanada() {
		return agua_encanada;
	}


	public void setAgua_encanada(char agua_encanada) {
		this.agua_encanada = agua_encanada;
	}


	public char getRede_esgoto() {
		return rede_esgoto;
	}


	public void setRede_esgoto(char rede_esgoto) {
		this.rede_esgoto = rede_esgoto;
	}


	public String getFumo() {
		return fumo;
	}


	public void setFumo(String fumo) {
		this.fumo = fumo;
	}


	public String getBebida() {
		return bebida;
	}


	public void setBebida(String bebida) {
		this.bebida = bebida;
	}


	public String getAgrotoxico() {
		return agrotoxico;
	}


	public void setAgrotoxico(String agrotoxico) {
		this.agrotoxico = agrotoxico;
	}


	public char getDestrofia_solar() {
		return destrofia_solar;
	}


	public void setDestrofia_solar(char destrofia_solar) {
		this.destrofia_solar = destrofia_solar;
	}


	public int getExp_sol() {
		return exp_sol;
	}


	public void setExp_sol(int exp_sol) {
		this.exp_sol = exp_sol;
	}


	public char getHora_exp_sol() {
		return hora_exp_sol;
	}


	public void setHora_exp_sol(char hora_exp_sol) {
		this.hora_exp_sol = hora_exp_sol;
	}


	public char getChapeu() {
		return chapeu;
	}


	public void setChapeu(char chapeu) {
		this.chapeu = chapeu;
	}


	public char getManga_cumprida() {
		return manga_cumprida;
	}


	public void setManga_cumprida(char manga_cumprida) {
		this.manga_cumprida = manga_cumprida;
	}


	public char getCalca_cumprida() {
		return calca_cumprida;
	}


	public void setCalca_cumprida(char calca_cumprida) {
		this.calca_cumprida = calca_cumprida;
	}


	public char getFiltro_solar() {
		return filtro_solar;
	}


	public void setFiltro_solar(char filtro_solar) {
		this.filtro_solar = filtro_solar;
	}


	public String getRenda() {
		return renda;
	}


	public void setRenda(String renda) {
		this.renda = renda;
	}

	public String getOrigem_familiar_mae() {
		return origem_familiar_mae;
	}

	public void setOrigem_familiar_mae(String origem_familiar_mae) {
		this.origem_familiar_mae = origem_familiar_mae;
	}

	public String getOrigem_familiar_pai() {
		return origem_familiar_pai;
	}

	public void setOrigem_familiar_pai(String origem_familiar_pai) {
		this.origem_familiar_pai = origem_familiar_pai;
	}

	public char getHist_cancer_pele() {
		return hist_cancer_pele;
	}

	public void setHist_cancer_pele(char hist_cancer_pele) {
		this.hist_cancer_pele = hist_cancer_pele;
	}

	public char getHist_cancer() {
		return hist_cancer;
	}

	public void setHist_cancer(char hist_cancer) {
		this.hist_cancer = hist_cancer;
	}

	public int getNum_vezes_atendido() {
		return num_vezes_atendido;
	}

	public void setNum_vezes_atendido(int num_vezes_atendido) {
		this.num_vezes_atendido = num_vezes_atendido;
	}

	public char getTipo_pele() {
		return tipo_pele;
	}

	public void setTipo_pele(char tipo_pele) {
		this.tipo_pele = tipo_pele;
	}

	public String getCartao_sus() {
		return cartao_sus;
	}

	public void setCartao_sus(String cartao_sus) {
		this.cartao_sus = cartao_sus;
	}

	public List<Lesao> getLesoes() {
		return lesoes;
	}

	public void setLesoes(List<Lesao> lesoes) {
		this.lesoes = lesoes;
	}

	public float getPres_art_sistolica() {
		return pres_art_sistolica;
	}

	public void setPres_art_sistolica(float pres_art_sistolica) {
		this.pres_art_sistolica = pres_art_sistolica;
	}

	public float getPres_art_diastolica() {
		return pres_art_diastolica;
	}

	public void setPres_art_diastolica(float pres_art_diastolica) {
		this.pres_art_diastolica = pres_art_diastolica;
	}

	public char getDiabetes() {
		return diabetes;
	}

	public void setDiabetes(char diabetes) {
		this.diabetes = diabetes;
	}

	public char getAnticoagulante() {
		return anticoagulante;
	}

	public void setAnticoagulante(char anticoagulante) {
		this.anticoagulante = anticoagulante;
	}

	public String getAlergia() {
		return alergia;
	}

	public void setAlergia(String alergia) {
		this.alergia = alergia;
	}

	public String getObs() {
		return obs.toUpperCase();
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public char getHAS() {
		return HAS;
	}

	public void setHAS(char hAS) {
		HAS = hAS;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}	
	
	
	
}
