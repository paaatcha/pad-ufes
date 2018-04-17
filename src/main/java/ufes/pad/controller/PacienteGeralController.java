package ufes.pad.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

import ufes.pad.model.PacienteGeral;
import ufes.pad.repository.PacienteGeralRepository;

@ManagedBean
@ViewScoped
public class PacienteGeralController {

	@Autowired 
	private PacienteGeralRepository pac_rep;	
		
	public void configuraUnirest () { 
		Unirest.setObjectMapper(new ObjectMapper() {
		    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		                = new com.fasterxml.jackson.databind.ObjectMapper();
	
		    public <T> T readValue(String value, Class<T> valueType) {
		        try {
		            return jacksonObjectMapper.readValue(value, valueType);
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
		    }
	
		    public String writeValue(Object value) {
		        try {
		            return jacksonObjectMapper.writeValueAsString(value);
		        } catch (JsonProcessingException e) {
		            throw new RuntimeException(e);
		        }
		    }
		});	
	}
	
	
	public String enviarPacientesGeralBancoUfes () {		 
		configuraUnirest ();
		int i = 0;
		FacesContext context = FacesContext.getCurrentInstance();
		
		System.out.println("\n---- Iniciando envio pacientes gerais ----\n");
		
		try {
			List <PacienteGeral> pacs = pac_rep.findAll();			
			
			if (pacs == null) {
				return null;
			}
			
			for (PacienteGeral pac : pacs) {	
				i++;
				HttpResponse<JsonNode> postResponse = Unirest.post("http://labcin1.ufes.br/APIrequisicoes/novo_paciente_geral")
//				HttpResponse<JsonNode> postResponse = Unirest.post("http://localhost:8080/APIrequisicoes/novo_paciente_geral")
				.header("accept", "application/json")
				.header("Content-Type", "application/json")
				.body(pac)
				.asJson();
				
				if (postResponse.getStatus() == 200) {
					System.out.println(i + " Paciente geral: " + pac.getCartao_sus() + " enviado com sucesso...");					
				} else {
					System.out.println(" Paciente geral: " + pac.getCartao_sus() + " problema na requisicao...");
				}
				
			}
			
			context.addMessage(null, new FacesMessage(i + " pacientes gerais foram enviados com sucesso para o servidor da UFES.", ""));
			
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao enviar pacientes gerais para o banco da UFES.", "  "));
		}
		
		return "Sucesso";
	}
	
	
/* ###########################################  Getters and Setters ###################################################*/



}
