package ufes.pad.controller;

import javax.annotation.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import ufes.pad.model.Paciente;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
public class APIrequisicoesController {

	@Autowired
	private PacienteRepository pac_rep;	
		
	ObjectMapper mapper = new ObjectMapper();
	private String nomePacienteJson;
	
//    @PostConstruct
	public void pegaPacienteCartaoSus () {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
				
		String cartaoSus = request.getParameter("cartaosus");
		
		try {
			Paciente pac = (pac_rep.buscaPorCartaoSus(cartaoSus));			 
			
			if (pac!=null) {				
				System.out.println("Paciente encontrado com sucesso");
				MiniPaciente pacJson = new MiniPaciente(pac);
				nomePacienteJson = mapper.writeValueAsString(pacJson);
			} else {
				// Retornando um NULL
				nomePacienteJson = mapper.writeValueAsString(new MiniPaciente( new Paciente ()));
			}
		} catch (Exception e) {
				
		}
	}

	public String getNomePacienteJson() {
		return nomePacienteJson;
	}

	public void setNomePacienteJson(String nomePacienteJson) {
		this.nomePacienteJson = nomePacienteJson;
	}
}

class MiniPaciente {
	private String nome, pront, alergia, diabetes, anticoagulante, has;
	private float pressao_dia, pressao_sis;
	
	public MiniPaciente(Paciente pac) {
		nome = pac.getNome_completo();
		pront = pac.getProntuario();
		alergia = pac.getAlergia();		
		
		if (pac.getDiabetes()=='S') {
			diabetes = "SIM";
		} else {
			diabetes = "NÃO";
		}
		
		if (pac.getAnticoagulante()=='S') {
			anticoagulante = "SIM";
		} else {
			anticoagulante = "NÃO";
		}
		
		if (pac.getHAS()=='S') {
			has = "SIM";
		} else {
			has = "NÃO";
		}		
		
		pressao_dia = pac.getPres_art_diastolica();
		pressao_sis = pac.getPres_art_sistolica();
		
	}	
	
	public float getPressao_sis() {
		return pressao_sis;
	}
	public void setPressao_sis(float pressao_sis) {
		this.pressao_sis = pressao_sis;
	}
	public float getPressao_dis() {
		return pressao_dia;
	}
	public void setPressao_dis(float pressao_dis) {
		this.pressao_dia = pressao_dis;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPront() {
		return pront;
	}
	public void setPront(String pront) {
		this.pront = pront;
	}
	public String getAlergia() {
		return alergia;
	}
	public void setAlergia(String alergia) {
		this.alergia = alergia;
	}
	public String getDiabetes() {
		return diabetes;
	}
	public void setDiabetes(String diabetes) {
		this.diabetes = diabetes;
	}
	public String getAnticoagulante() {
		return anticoagulante;
	}
	public void setAnticoagulante(String anticoagulante) {
		this.anticoagulante = anticoagulante;
	}
	public String getHas() {
		return has;
	}
	public void setHas(String has) {
		this.has = has;
	}
	
	
}
















