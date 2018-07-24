package ufes.pad.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Imagem;
import ufes.pad.model.Lesao;
import ufes.pad.model.Paciente;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class VisualizacaoController {

		
	@Autowired 
	private PacienteRepository pac_rep;
	
	private Paciente pacSelecionadoLesao;
	
	private String cartao_sus_busca;

	private Paciente pacBuscado;	
	
	private boolean liberarVisualizacao = false;
	
	static public int calcIdadePac (Date dataNascimento) {
		LocalDate hoje = LocalDate.now();		
		LocalDate dNasc = dataNascimento.toLocalDate();		
		Period p = Period.between(dNasc, hoje);	
//		System.out.println(p.getYears());		
		return (p.getYears());				
	}
	
	public void buscaUnicaCartaoSus () {
		try {
			pacBuscado =  pac_rep.buscaPorCartaoSus(cartao_sus_busca);			
			
			if (pacBuscado==null) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Nenhum paciente foi encontrado com este número de cartão. Verifique se o número foi digitado corretamente. Tente visualizar todos para encontrá-lo, caso não encontre, ele não está registrado no banco.", "  "));
			} else {
				this.liberarVisualizacao=true;
				Date dNasc = (Date) pacBuscado.getData_nascimento();
				pacBuscado.setIdade(calcIdadePac(dNasc));
				System.out.println("Paciente encontrado com sucesso");;
			}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}		
	}
	
	public List<String> pegaImagensLesao (Lesao les){
		List<String> imgsPath = new ArrayList<String>();
		List<Imagem> imgs = les.getImagens();
		
		for (Imagem img : imgs) {
			imgsPath.add(img.getPath());
		}	
		
		return imgsPath;
	}		

	public String redirecionaParaEdicao () {
		return "home.xhtml?" + pacBuscado.getCartao_sus();
	}
	
// ############################### FUNCOES PARA VISUALIZAÇÕES ##############################################	

	public String printData (Date d) {
		d = (Date) d;
		SimpleDateFormat brasil = new SimpleDateFormat("dd/MM/yyyy");
		
		return brasil.format(d).toString();
	}
	
	public String printEstadoCivil() {
		switch (pacBuscado.getEstado_civil()) {
			case 'C':
				return "CASADO";
			case 'S':
				return "SOLTEIRO";
			case 'V':
				return "VIÚVO";
			case 'D':
				return "DIVORCIADO";
			default:
				return "DESCONHECIDO";
		}
		
	}
	
	public String printDestrofia() {
		switch (pacBuscado.getDestrofia_solar()) {
			case 'L':
				return "LEVE";
			case 'M':
				return "MODERADA";
			case 'F':
				return "FORTE";
			case 'A':
				return "AUSENTE";
			case 'P':
				return "PRÉ-DISTRÓFICA";
			default:
				return "DESCONHECIDO";
		}
		
	}
	
	public String printExposicaoSol() {
		switch (pacBuscado.getHora_exp_sol()) {
			case 'M':
				return "PARTE DA MANHÃ";
			case 'T':
				return "PARTE DA TARDE";
			case 'A':
				return "TANTO A TARDE QUANTO DE MANHÃ";			
			default:
				return "DESCONHECIDO";
		}
		
	}   
		
	public String printRenda() {
		if (pacBuscado.getRenda().equals("A1")) {
			return "MENOR OU IGUAL A 1";
		} else if (pacBuscado.getRenda().equals("1-5")) {
			return "MAIOR QUE 1 E MENOR OU IGUAL A 5";			
		} else if (pacBuscado.getRenda().equals("5-10")) {
			return "MAIS QUE 5 E MENOR OU IGUAL A 10";
		} else {
			return "MAIS DO QUE 10";
		}
		
	}	
	
	public String printNumComodos() {
		if (pacBuscado.getNum_comodos().equals("1-3")) {
			return "DE 1 A 3";
		} else if (pacBuscado.getNum_comodos().equals("4-5")) {
			return "DE 4 A 5";			
		} else {
			return "MAIS DE 5";
		}
		
	}	
	
	public String printBebida() {
		if (pacBuscado.getBebida().equals("DIA")) {
			return "DIARIAMENTE";
		} else if (pacBuscado.getBebida().equals("FDS")) {
			return "FIM DE SEMANA";			
		} else {
			return "NÃO BEBE";
		}
		
	}
	
	public String printCigarro() {
		if (pacBuscado.getFumo().equals("ATE5")) {
			return "MENOS DE 5 CIGARROS POR DIA";
		} else if (pacBuscado.getFumo().equals("5A10")) {
			return "DE 5 A 10 CIGARROS POR DIA";			
		} else if (pacBuscado.getFumo().equals("M10")) {
			return "MAIS DE 10 CIGARROS POR DIA";
		} else {
			return "NÃO FUMA";
		}
		
	}	
	
	public String printSimNao (char tipo) {
		if (tipo == 'S') {
			return "SIM";
		} else {
			return "NÃO";
		}
	}
	
	public String printSexo () {
		if (pacBuscado.getSexo() == 'F') {
			return "FEMININO";
		} else {
			return "MASCULINO";
		}
	}
	
	
	public String excluirPaciente () {
		Paciente.printPaciente(pacBuscado);
		
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			pac_rep.delete(pacBuscado);
			
			for (Lesao les : pacBuscado.getLesoes()) {
				EdicaoController.excluirImagensServer (les.getImagens());
			}			
			context.addMessage(null, new FacesMessage("Paciente excluido com sucesso."));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir paciente. Tente novamente. Se o problema persistir, entre em contato com o administrador do sistema.", "  "));
			return null;
		}
		
		liberarVisualizacao = false;
		
		return "visualizar_paciente.xhtml";
	}	
	
	public String printDataProcedimento (Date d) {		
		return (d.toString().substring(8, 10) + "/" + d.toString().substring(5, 7) + "/" + d.toString().substring(0, 4));		
	}
	
	
	
	
	
// ############################### GETTERS AND SETTERS #####################################################

	public Paciente getPacSelecionadoLesao() {
		return pacSelecionadoLesao;
	}

	public void setPacSelecionadoLesao(Paciente pacSelecionadoLesao) {
		System.out.println("FUNCIONASDASD");
		this.pacSelecionadoLesao = pacSelecionadoLesao;
	}

	public String getCartao_sus_busca() { 
		return cartao_sus_busca;
	}

	public void setCartao_sus_busca(String cartao_sus_busca) {
		this.cartao_sus_busca = cartao_sus_busca;
	}

	public Paciente getPacBuscado() {
		return pacBuscado;
	}

	public void setPacBuscado(Paciente pacBuscado) {
		this.pacBuscado = pacBuscado;
	}

	public boolean isLiberarVisualizacao() {
		return liberarVisualizacao;
	}

	public void setLiberarVisualizacao(boolean liberarVisualizacao) {
		this.liberarVisualizacao = liberarVisualizacao;
	}
	
}
