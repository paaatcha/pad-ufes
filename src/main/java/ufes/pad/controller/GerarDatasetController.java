package ufes.pad.controller;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.repository.PacienteGeralRepository;
import ufes.pad.repository.PacienteRepository;

public class GerarDatasetController {

	@Autowired
	private PacienteRepository pac_rep;
	
	@Autowired
	private PacienteGeralRepository pac_geral_rep;
	
	private boolean dsCompleto = false;
	
	private boolean dsCirurgia = false;
	
	private boolean dsApp = false;
 
	public boolean isDsCompleto() {
		return dsCompleto;
	}

	public void setDsCompleto(boolean dsCompleto) {
		this.dsCompleto = dsCompleto;
	}

	public boolean isDsCirurgia() {
		return dsCirurgia;
	}

	public void setDsCirurgia(boolean dsCirurgia) {
		this.dsCirurgia = dsCirurgia;
	}

	public boolean isDsApp() {
		return dsApp;
	}

	public void setDsApp(boolean dsApp) {
		this.dsApp = dsApp;
	}
	
	
	
	
}
