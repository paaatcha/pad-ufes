package ufes.pad.controller;



import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ufes.pad.model.Imagem;
import ufes.pad.model.ImagemGeral;
import ufes.pad.model.Lesao;
import ufes.pad.model.LesaoGeral;
import ufes.pad.model.Paciente;
import ufes.pad.model.PacienteGeral;
import ufes.pad.repository.PacienteGeralRepository;
import ufes.pad.repository.PacienteRepository;

@RestController
public class APIrequisicoesController {

	@Autowired
	private PacienteRepository pac_rep;
	
	@Autowired
	private PacienteGeralRepository pac_rep_geral;
	
	
	@GetMapping ("/APIrequisicoes/paciente/{cartao_sus}")
	public @ResponseBody MiniPaciente pegarMiniPaciente (@PathVariable String cartao_sus){
		
		try {			
			Paciente pac = (pac_rep.buscaPorCartaoSus(cartao_sus));
			System.out.println(pac);
			if (pac != null) {
				System.out.println("Paciente da requisicao foi encontrado com sucesso");
				MiniPaciente miniPac = new MiniPaciente(pac);
				return miniPac;
			} else {
				System.out.println("Este paciente não esta cadastrado");				
				return new MiniPaciente(new Paciente());
			}
		} catch (Exception e) {
			System.out.println("Falha na requisição para o banco de dados");
			e.printStackTrace();			
		}		
		return null;
	}
	
	@GetMapping ("/APIrequisicoes/ack")
	public @ResponseBody String ack (){		
		return "ACK";
	}	
	
	@PostMapping("/APIrequisicoes/paciente/cadastrarLesoes/{cartao_sus}")
	public void cadastrarLesao (@PathVariable String cartao_sus, @RequestParam("regiao") String regiao,
			@RequestParam("diaMaior") String diaMaior, @RequestParam("diaMenor") String diaMenor, 
			@RequestParam("diagnostico") String diagnostico, @RequestParam("procedimento") String procedimento,
			@RequestParam("obs") String obs,
			@RequestParam("imagem") MultipartFile[] imagem){
		
		try {			

			Paciente pac = (pac_rep.buscaPorCartaoSus(cartao_sus));
			
			if (pac != null) {			
				Lesao les = new Lesao ();
				List<Imagem> auxListImg = new ArrayList<Imagem>();
				
				// Setando os dados vindo da requisição
				les.setRegiao(regiao);
				les.setDiametro_maior(Float.parseFloat(diaMaior));
				les.setDiametro_menor(Float.parseFloat(diaMenor));
				les.setDiagnostico_clinico(diagnostico);
				les.setProcedimento(procedimento);
				les.setObs(obs);			
				
				// Incluindo as imagens
				for (MultipartFile img : imagem) {
					String path = cartao_sus + "_" + new Date().getTime() + ".jpg";
					File FilePath = new File("src/main/webapp/dashboard/imgLesoes/" + path);			
					byte imgBytes [] = img.getBytes();			
					FileUtils.writeByteArrayToFile(FilePath, imgBytes);
					
					// Inserindo a imagem
					Imagem imgLesao = new Imagem();
					imgLesao.setPath(path);
					auxListImg.add(imgLesao);				
				}
				
				les.setImagens(auxListImg);			
				Lesao.printLesao(les);
				
				
				if (pac.getLesoes() == null) {
					List<Lesao> lesPaclist = new ArrayList<Lesao>();
					lesPaclist.add(les);
					pac.setLesoes(lesPaclist);
					pac_rep.save(pac);
				} else {
					pac.getLesoes().add(les);
					pac_rep.save(pac);
				}				
			}			
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problemas na inserção de dados na requisição http");
		}	
		
	}
	
	@PostMapping("/APIrequisicoes/paciente/cadastrarLesoesGerais/{cartao_sus}")
	public void cadastrarLesaoGeral (@PathVariable String cartao_sus, @RequestParam("regiao") String regiao,			 
			@RequestParam("diagnostico") String diagnostico, @RequestParam("cresceu") String cresceu,
			@RequestParam("cocou") String cocou, @RequestParam("sangrou") String sangrou, 
			@RequestParam("doeu") String doeu, @RequestParam("mudou") String mudou,			
			@RequestParam("imagem") MultipartFile[] imagem){
		
		try {
			PacienteGeral pac = pac_rep_geral.buscaPorCartaoSus(cartao_sus);			
			if (pac == null) {
				pac = new PacienteGeral();	
				pac.setCartao_sus(cartao_sus);				
			}	
			
			LesaoGeral les = new LesaoGeral();
			List<ImagemGeral> imgList = new ArrayList<ImagemGeral>();			
			
			les.setRegiao(regiao);
			les.setDiagnostico(diagnostico);
			les.setCocou(cocou);
			les.setCresceu(cresceu);
			les.setDoeu(doeu);
			les.setMudou(mudou);
			les.setSangrou(sangrou);
			
			for (MultipartFile img : imagem) {
				String path = cartao_sus + "_" + new Date().getTime() + ".jpg";
				File FilePath = new File("src/main/webapp/dashboard/imgLesoesGeral/" + path);			
				byte imgBytes [] = img.getBytes();			
				FileUtils.writeByteArrayToFile(FilePath, imgBytes);
				
				// Inserindo a imagem
				ImagemGeral imgLesao = new ImagemGeral();				
				imgLesao.setPath(path);
				imgList.add(imgLesao);				
			}
			
			les.setImagens(imgList);
			
			if (pac.getLesoes() == null) {
				List<LesaoGeral> lesPaclist = new ArrayList<LesaoGeral>();
				lesPaclist.add(les);
				pac.setLesoes(lesPaclist);
				pac_rep_geral.save(pac);
			} else {
				pac.getLesoes().add(les);
				pac_rep_geral.save(pac);
			}	
			
			System.out.println("\nRecebendo novo paciente...\nCartao sus: " + cartao_sus);
			System.out.println("Numero de imgs: " + les.getImagens().size());
			System.out.println("Regiao: " + les.getRegiao());			
			System.out.println("Diag: " + les.getDiagnostico() + "\n----------------\n");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@PostMapping("/APIrequisicoes/novo_paciente")
	public void novoPaciente (@RequestBody Paciente pac) {
		
		// Setando os ids para NULL para não haver sobreposição
		pac.setId(null);
		for (Lesao les : pac.getLesoes()) {
			les.setId(null);
			for (Imagem img : les.getImagens()) {
				img.setId(null);
			}
		}
		
		try {
			
			if (pac_rep.buscaPorCartaoSus(pac.getCartao_sus()) == null) {
				pac_rep.save(pac);		
				System.out.println("Paciente: " + pac.getNome_completo() + " salvo no banco UFES com sucesso");
			} else {
				System.out.println("Paciente: " + pac.getNome_completo() + " cartão sus: " + pac.getCartao_sus() + " já está cadastrado no banco UFES");
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao salvar paciente " + pac.getNome_completo() + " banco da UFES");
		}	
		
	}
	
	@PostMapping("/APIrequisicoes/novo_paciente_geral")
	public void novoPacienteGeral (@RequestBody PacienteGeral pac) {		
			
		// Setando os ids para NULL para não haver sobreposição
		pac.setId(null);
		for (LesaoGeral les : pac.getLesoes()) {
			les.setId(null);
			for (ImagemGeral img : les.getImagens()) {
				img.setId(null);
			}
		}
		
		try {
			
			if (pac_rep_geral.buscaPorCartaoSus(pac.getCartao_sus()) == null) {
				pac_rep_geral.save(pac);		
				System.out.println("Paciente Geral: " + pac.getCartao_sus() + " salvo no banco UFES com sucesso");
			} else {
				System.out.println("Paciente Geral: " + pac.getCartao_sus() + " já está cadastrado no banco UFES");
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao salvar paciente geral" + pac.getCartao_sus() + " banco da UFES");
		}	
		
	}	
	
	

}

class MiniPaciente {
	private String nome, pront, alergia, diabetes, anticoagulante, has;
	private float pressao_dia, pressao_sis;
	private int nLesoes;
	
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
		
		if (pac.getLesoes() != null) {
			nLesoes = pac.getLesoes().size();
		} else {
			nLesoes = 0;
		}
		
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

	public int getnLesoes() {
		return nLesoes;
	}

	public void setnLesoes(int nLesoes) {
		this.nLesoes = nLesoes;
	}
	
	
}
















