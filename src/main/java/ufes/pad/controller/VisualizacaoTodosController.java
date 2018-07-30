package ufes.pad.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.Paciente;
import ufes.pad.repository.LesaoRepository;
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class VisualizacaoTodosController {
	
	private List<Paciente> todosPacs;
	
	@Autowired 
	private PacienteRepository pac_rep;	
	
	@Autowired 
	private LesaoRepository les_rep;
	
	private int numPac;
	
	private Long numCirurgias;
	
	private boolean exibirTabelaPacientes = false;
	
	// Filtros
	
	private String filtroCidade = "";
	
	private String filtroNome = "";
	
	private String filtroDiag = "";	
		
	private boolean filtroPacSemLesao = false;	
		
	private boolean filtroPacSemImg = false;
	
	private boolean filtroCirurgias = false;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	// *****************************
	
	public List<String> completarCidades (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] cidades = new String[] {"ITAGUAÇU", "AFONSO CLÁUDIO", "SANTA MARIA DE JETIBA", "VILA VALERIO", "PANCAS", "BAIXO GUANDU", "SÃO GABRIEL DA PALHA", "DOMINGOS MARTINS"};
		
		for (String s : cidades){
			 if (s.startsWith(query)){
				 result.add(s);
			 }
		}		
		return result;		
	}	
	
	
	
	public void listarFiltragem() {
		FacesContext context = FacesContext.getCurrentInstance();	
		boolean comDatas = dataInicio != null && dataFim != null;	
		
		try {
			// Pegando os dados de onde é necessário para realizar os filtros
			if (comDatas) {
				System.out.println("Filtrando pacientes por intervalo de datas");
				setTodosPacs(pac_rep.filtrarPacientesComData(filtroCidade, filtroNome, dataInicio, dataFim, filtroDiag));
				
				// Se quiser o numero de cirurgias também, tem que rodar outra query:
				if (filtroCirurgias && filtroNome.equals("")) {					
					numCirurgias = les_rep.filtraLesoesPorLocalData(filtroCidade, dataInicio, dataFim, filtroDiag);
					System.out.println("Numero de cirurgias: " + numCirurgias);
				} else if (filtroCirurgias && !filtroNome.equals("")) {
					// Neste caso, as cirurgias para o paciente com nome, não tem como pegar
					context.addMessage(null, new FacesMessage("Atenção, o número de cirurgias só é exibido para uma cidade e/ou um intervalo de datas. Caso procure um nome, essa funcionalidade não estará ativa."));
					this.filtroCirurgias = false;					
				}
				
			} else if (!filtroCidade.equals("") || !filtroNome.equals("") || !filtroDiag.equals("")) {
				System.out.println("Filtrando pacientes por cidade ou nome");
				setTodosPacs(pac_rep.filtrarPacientes(filtroCidade, filtroNome, filtroDiag));
				
				// Se quiser o numero de cirurgias também, tem que rodar outra query:
				if (filtroCirurgias && filtroNome.equals("")) {
					numCirurgias = les_rep.filtraLesoesPorLocal(filtroCidade, filtroDiag);
					System.out.println("Numero de cirurgias: " + numCirurgias);
				} else if (filtroCirurgias && filtroNome.equals("")){
					// Neste caso, as cirurgias para o paciente com nome, não tem como pegar
					context.addMessage(null, new FacesMessage("Atenção, o número de cirurgias só é exibido para uma cidade e/ou um intervalo de datas. Caso procure um nome, essa funcionalidade não estará ativa."));
					this.filtroCirurgias = false;					
				}
			
			// Filtrar sem lesao e sem imagem, tem que ser na lista toda (infelizmente).
			} else if (filtroPacSemLesao || filtroPacSemImg) {
				System.out.println("Filtrando pacientes sem lesão ou imagem");
				setTodosPacs(pac_rep.listarPacientes());
				
				if (filtroCirurgias) {
					context.addMessage(null, new FacesMessage("Atenção, o número de cirurgias só é exibido para uma cidade e/ou um intervalo de datas. Caso procure um nome, essa funcionalidade não estará ativa."));
					this.filtroCirurgias = false;					
				}
				
				if (filtroPacSemLesao || (filtroPacSemImg && filtroPacSemLesao)) {
					List<Paciente> newTodosPacs = new ArrayList<Paciente>();
					for (Paciente pac : todosPacs) {
						if (!pac.possuiLesao()) {
							newTodosPacs.add(pac);
						}
					}						
					todosPacs = newTodosPacs;
				} else if (filtroPacSemImg) {
					List<Paciente> newTodosPacs = new ArrayList<Paciente>();
					for (Paciente pac : todosPacs) {
						if (!pac.possuiImagem()) {
							newTodosPacs.add(pac);
						}
					}						
					todosPacs = newTodosPacs;
				}				
				
			} else {
				numPac = 0;
				exibirTabelaPacientes = false;
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Não existe nenhum paciente para essa filtragem. Verifique se não existe algum erro de digitação.", "  "));
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));
		}		
		
		// Verificando se pode exibir os pacientes
		if (!todosPacs.isEmpty()) {					
			preencheIdadePacs (this.getTodosPacs());
			exibirTabelaPacientes = true;
			numPac = todosPacs.size();			
		} else {
			exibirTabelaPacientes = false; 
			numPac = 0;
		}
		context.addMessage(null, new FacesMessage(numPac + " pacientes foram encontrados!"));
				
	}
	
	public void listarPacientes () {		
		FacesContext context = FacesContext.getCurrentInstance();		
		try {
			setTodosPacs(pac_rep.listarPacientes());
			if (this.getTodosPacs().isEmpty()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Não existe nenhum paciente neste banco de dados.", "  "));				
			} else {
				preencheIdadePacs (this.getTodosPacs());
				exibirTabelaPacientes = true;
				numPac = todosPacs.size();	
				numCirurgias = new Long(PacienteController.totalLesoes(todosPacs));
				context.addMessage(null, new FacesMessage(numPac + " pacientes foram encontrados!"));
				this.filtroCirurgias = true;
			}
		} catch (Exception e) {			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
		}
		
	}		
	
	public void preencheIdadePacs (List<Paciente> pacientes) {
		for (int i=0; i < pacientes.size(); i++) {
			java.sql.Date dNasc = (java.sql.Date) pacientes.get(i).getData_nascimento();
			pacientes.get(i).setIdade(VisualizacaoController.calcIdadePac(dNasc));
//			System.out.println(pacientes.get(i).getIdade());
//			System.out.println(pacientes.get(i).getData_nascimento().getClass());			
		}
	}

	public List<Paciente> getTodosPacs() {
		return todosPacs;
	}

	public void setTodosPacs(List<Paciente> todosPacs) {
		this.todosPacs = todosPacs;
	}

	public int getNumPac() {
		return numPac;
	}

	public void setNumPac(int numPac) {
		this.numPac = numPac;
	}

	public String getFiltroCidade() {
		return filtroCidade;
	}

	public void setFiltroCidade(String filtroCidade) {
		this.filtroCidade = filtroCidade;
	}

	public boolean isExibirTabelaPacientes() {
		return exibirTabelaPacientes;
	}

	public void setExibirTabelaPacientes(boolean exibirTabelaPacientes) {
		this.exibirTabelaPacientes = exibirTabelaPacientes;
	}

	public String getFiltroNome() {
		return filtroNome;
	}

	public void setFiltroNome(String filtroNome) {
		this.filtroNome = filtroNome;
	}

	public boolean isFiltroPacSemLesao() {
		return filtroPacSemLesao;
	}

	public void setFiltroPacSemLesao(boolean filtroPacSemLesao) {
		this.filtroPacSemLesao = filtroPacSemLesao;
	}

	public boolean isFiltroPacSemImg() {
		return filtroPacSemImg;
	}

	public void setFiltroPacSemImg(boolean filtroPacSemImg) {
		this.filtroPacSemImg = filtroPacSemImg;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Long getNumCirurgias() {
		return numCirurgias;
	}

	public void setNumCirurgias(Long numCirurgias) {
		this.numCirurgias = numCirurgias;
	}

	public String getFiltroDiag() {
		return filtroDiag;
	}

	public void setFiltroDiag(String filtroDiag) {
		this.filtroDiag = filtroDiag;
	}

	public boolean isFiltroCirurgias() {
		return filtroCirurgias;
	}

	public void setFiltroCirurgias(boolean filtroCirurgias) {
		this.filtroCirurgias = filtroCirurgias;
	}

	

}
