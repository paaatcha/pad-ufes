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
import ufes.pad.repository.PacienteRepository;

@ManagedBean
@ViewScoped
public class VisualizacaoTodosController {
	
	private List<Paciente> todosPacs;
	
	@Autowired 
	private PacienteRepository pac_rep;	
	
	private int numPac;
	
	private int numCirurgias;
	
	private boolean exibirTabelaPacientes = false;
	
	// Filtros
	
	private String filtroCidade = "";
	
	private String filtroNome = "";
	
	private boolean filtroPacComLesao = false;
	
	private boolean filtroPacSemLesao = false;
	
	private boolean filtroPacComImg = false;
	
	private boolean filtroPacSemImg = false;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	// *****************************
	
	public List<String> completarCidades (String query){
		List<String> result = new ArrayList<String>();
		query = query.toUpperCase();
		String[] cidades = new String[] {"ITAGUAÇU", "AFONSO CLÁUDIO", "SANTA MARIA DE JETIBA"};
		
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
		
		
		if (filtroCidade.equals("") && filtroNome.equals("") && !filtroPacComLesao && !filtroPacComImg && !filtroPacSemLesao && !filtroPacSemImg && !comDatas) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Todos os filtros estão vazios. Adicione pelo menos um filtro para utilizar essa funcionalidade.", "  "));
		} else {		
			try {
				
				if (comDatas) {
					System.out.println("Filtrando por intervalo de data");
					
//					Calendar calInicio = Calendar.getInstance();
//					calInicio.setTime(dataInicio);
//					
//					Calendar calFim = Calendar.getInstance();
//					calFim.setTime(dataFim);					
//					String dataInicioStr, dataFimStr;
//					
//					dataInicioStr = String.valueOf(calInicio.get(Calendar.YEAR)) + "-" + String.valueOf(calInicio.get(Calendar.MONTH)+1) + "-" + String.valueOf(calInicio.get(Calendar.DAY_OF_MONTH));
//					dataFimStr = String.valueOf(calFim.get(Calendar.YEAR)) + "-" + String.valueOf(calFim.get(Calendar.MONTH)+1) + "-" + String.valueOf(calFim.get(Calendar.DAY_OF_MONTH));
					
					setTodosPacs(pac_rep.filtrarPacientesComData(filtroCidade, filtroNome, dataInicio, dataFim));
				} else {
					setTodosPacs(pac_rep.filtrarPacientes(filtroCidade, filtroNome));					
				}
				
				if (this.getTodosPacs().isEmpty()) {
					numPac = 0;
					exibirTabelaPacientes = false;
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Não existe nenhum paciente para essa filtragem. Verifique se não existe algum erro de digitação.", "  "));				
				} else {

					
					// REALIZANDO FILTROS DE LESÃO PARA O PACIENTE					
					if (filtroPacComLesao && !filtroPacSemLesao) {
						List<Paciente> newTodosPacs = new ArrayList<Paciente>();
						System.out.println("Filtrando pacientes com lesao: ");
						
						for (Paciente pac : todosPacs) {
							//System.out.println(pac.pacientePossuiLesao());							
							if (pac.possuiLesao()) {
								//todosPacs.remove(pac);
								//Paciente.printPaciente(pac);
								newTodosPacs.add(pac);
							}
						}						
						todosPacs = newTodosPacs;						
					}
					
					if (filtroPacSemLesao && !filtroPacComLesao) {
						List<Paciente> newTodosPacs = new ArrayList<Paciente>();
						System.out.println("Filtrando pacientes sem lesao: ");
						
						for (Paciente pac : todosPacs) {
							//System.out.println(pac.pacientePossuiLesao());							
							if (!pac.possuiLesao()) {
								//todosPacs.remove(pac);
								//Paciente.printPaciente(pac);
								newTodosPacs.add(pac);
							}
						}						
						todosPacs = newTodosPacs;						
					}
					
					// *********** FIM **************
					
					
					// REALIZANDO FILTROS DE IMAGEM PARA O PACIENTE	
					if (filtroPacComImg && !filtroPacSemImg) {
						List<Paciente> newTodosPacs = new ArrayList<Paciente>();
						System.out.println("Filtrando pacientes com Imagem: ");
						
						for (Paciente pac : todosPacs) {
							//System.out.println(pac.pacientePossuiLesao());							
							if (pac.possuiImagem()) {
								//todosPacs.remove(pac);
								//Paciente.printPaciente(pac);
								newTodosPacs.add(pac);
							}
						}						
						todosPacs = newTodosPacs;						
					}
					
					if (filtroPacSemImg && !filtroPacComImg) {
						List<Paciente> newTodosPacs = new ArrayList<Paciente>();
						System.out.println("Filtrando pacientes sem Imagem: ");
						
						for (Paciente pac : todosPacs) {
							//System.out.println(pac.pacientePossuiLesao());							
							if (!pac.possuiImagem()) {
								//todosPacs.remove(pac);
								//Paciente.printPaciente(pac);
								newTodosPacs.add(pac);
							}
						}						
						todosPacs = newTodosPacs;						
					}
					
					// *********** FIM **************					
					
					
					if (!todosPacs.isEmpty()) {					
						preencheIdadePacs (this.getTodosPacs());
						exibirTabelaPacientes = true;
						numPac = todosPacs.size();	
						numCirurgias = PacienteController.totalLesoes(todosPacs);
					} else {
						exibirTabelaPacientes = false;
						numPac = 0;
					}
					context.addMessage(null, new FacesMessage(numPac + " pacientes foram encontrados!"));
				}
			} catch (Exception e) {			
				e.printStackTrace();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! Problema de conexão com o banco de dados.", "  "));			
			}
		}
		
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
				numCirurgias = PacienteController.totalLesoes(todosPacs);
				context.addMessage(null, new FacesMessage(numPac + " pacientes foram encontrados!"));
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

	public boolean isFiltroPacComLesao() {
		return filtroPacComLesao;
	}

	public void setFiltroPacComLesao(boolean filtroPacComLesao) {
		this.filtroPacComLesao = filtroPacComLesao;
	}

	public boolean isFiltroPacComImg() {
		return filtroPacComImg;
	}

	public void setFiltroPacComImg(boolean filtroPacComImg) {
		this.filtroPacComImg = filtroPacComImg;
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

	public int getNumCirurgias() {
		return numCirurgias;
	}

	public void setNumCirurgias(int numCirurgias) {
		this.numCirurgias = numCirurgias;
	}

	

}
