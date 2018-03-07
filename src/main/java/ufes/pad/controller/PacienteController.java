package ufes.pad.controller;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Paciente;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@SessionScoped
public class PacienteController {

	private Paciente pac = new Paciente();
	
	@Autowired
	private PacienteRepository userRep;
	
	
	
	

	public Paciente getPac() {
		return pac;
	}

	public void setPac(Paciente pac) {
		this.pac = pac;
	}

	public PacienteRepository getUserRep() {
		return userRep;
	}

	public void setUserRep(PacienteRepository userRep) {
		this.userRep = userRep;
	}
	
	
	
	
}
