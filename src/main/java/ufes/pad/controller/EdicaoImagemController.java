package ufes.pad.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.CroppedImage;
import org.springframework.beans.factory.annotation.Autowired;

import ufes.pad.model.ImagemGeral;
import ufes.pad.model.LesaoGeral;
import ufes.pad.repository.LesaoGeralRepository;

@ManagedBean
@ViewScoped
public class EdicaoImagemController {

	private CroppedImage croppedImage;
	
	private String imgPath;
	
	private String imgPathNovo = "";
	
	private LesaoGeral lesao;
	
	private List<ImagemGeral> imagensCropadas = new ArrayList<ImagemGeral>();
	
	@Autowired
	private LesaoGeralRepository les_rep;
	
	private ImagemGeral imgSelecionada;
	
	@PostConstruct
	public void pegaImgURL () {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
				
		setImgPath(request.getParameter("imgpath"));
		Long lesaoId = Long.parseLong(request.getParameter("lesaoid"));
		
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			lesao = les_rep.findById(lesaoId);		
			lesao.print();
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lesão não encontrada!", "  "));
		}
		
				
	}	
    
	public void cropaImagem () {
		
		if (getCroppedImage() == null) {
			return;
		}
		
		/*String filename = "src/main/webapp" + getCroppedImage().getOriginalFilename();		
		int width = 0;
		int height = 0;
		
		try {
			BufferedImage imgOriginal = ImageIO.read(new File(filename));
			width = imgOriginal.getWidth();
			height = imgOriginal.getHeight();
		
		} catch (Exception e) {
			// TODO
		}*/
		
		FacesContext context = FacesContext.getCurrentInstance();
        setImgPathNovo("src/main/webapp/dashboard/imgLesoesGeral/cropped_" + imgPath);
        
        
         
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(getImgPathNovo()));
            imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
            imageOutput.close();
        } catch (Exception e) {
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Problema na edição da imagem.", "  "));
            return;
        }
        
		System.out.println("Tamanho final da imagem: ");		
		System.out.println(getCroppedImage().getLeft());
		System.out.println(getCroppedImage().getTop());
		System.out.println(getCroppedImage().getHeight());
		System.out.println(getCroppedImage().getWidth());
		context.addMessage(null, new FacesMessage("Imagem cropada com sucesso!"));
	}
	
	public void salvarCrop () {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			if (!imgPathNovo.equals("")) {
				ImagemGeral img = new ImagemGeral();
				
				// Renomeando a imagem cropada de cropped para o novo timestamp
				String cropPath = imgPath.substring(0, 18) + "_" + (new Date().getTime()) + ".jpg";
				File cropada = new File (imgPathNovo);
				cropada.renameTo( new File("src/main/webapp/dashboard/imgLesoesGeral/" + cropPath) );
				
				// Inserindo a imagem cropada na lista de imagens cropadas
				img.setPath(cropPath);			
				getImagensCropadas().add(img);	
				imgPathNovo = "";
				
				System.out.println("Imagem salva no path: " + cropPath);	
			}
			context.addMessage(null, new FacesMessage("Imagem cropada com sucesso!"));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao salvar este crop", "  "));
		}
	}
	
	
	public String salvarTudo () {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
		
			if (!getImagensCropadas().isEmpty()) {
				List<ImagemGeral> imagensLesoesBanco = lesao.getImagens();
				int i = 0;
				
				// Atualizando os dados das imagens das lesoes que estao no banco
				for (i = 0; i < imagensLesoesBanco.size(); i ++) {
					if (imagensLesoesBanco.get(i).getPath().equals(imgPath)) {
						// Salvando um backup da imagem antiga
						String imgFullPath = "src/main/webapp/dashboard/imgLesoesGeral/" + imgPath;
						File antiga = new File (imgFullPath);
						antiga.renameTo( new File("src/main/webapp/dashboard/imgLesoesGeral/antigas/"+imgPath) );						
						break;
					}
				}
				
				// Removendo da lista de lesoes a imagem que foi cropada
				imagensLesoesBanco.remove(i);
				
				for (ImagemGeral imgCropada : imagensCropadas) { 
					imagensLesoesBanco.add(imgCropada);
				}
				
				les_rep.save(lesao);
				context.addMessage(null, new FacesMessage("Imagens salvas com sucesso!"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao finalizar o salvamento dos crops", "  "));
		}
		
		return "/dashboard/visualizar_pacientes_gerais.xhtml?faces-redirect=true";
	}
	
	public void excluirCropGrid () {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			imagensCropadas.remove(imgSelecionada);
			File imgFile = new File ("src/main/webapp/dashboard/imgLesoesGeral/" + imgSelecionada.getPath());
			imgFile.delete();
			
			context.addMessage(null, new FacesMessage("Imagem excluida com sucesso!"));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao excluir imagem", "  "));
		}
	}
	
	/*
	public void salvar () {
		
		FacesContext context = FacesContext.getCurrentInstance();
		if (!imgPathNovo.equals("")) {
			
			String imgFullPath = "src/main/webapp/dashboard/imgLesoesGeral/" + imgPath;
			File antiga = new File (imgFullPath);
			File cropada = new File (imgPathNovo);
			
			try {
				antiga.renameTo( new File("src/main/webapp/dashboard/imgLesoesGeral/antigas/"+imgPath) );
				cropada.renameTo( new File("src/main/webapp/dashboard/imgLesoesGeral/"+imgPath) );
			} catch (Exception e) {
				System.out.println("Problema ao salvar as imagens!");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Problema ao salvar as imagens.", "  "));
				return;
			}
			context.addMessage(null, new FacesMessage("Imagens salvas com sucesso!"));
			System.out.println("Imagens salvas com sucesso!");
			imgPathNovo = "";
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Para salvar um crop, você tem que ter cropado uma imagem", "  "));
		}
		
	}
	*/
	
	public void descartar () {
		FacesContext context = FacesContext.getCurrentInstance();
		if (!imgPathNovo.equals("")) {						
			File cropada = new File (imgPathNovo);
			
			try {				
				cropada.delete();
				imgPathNovo = "";
			} catch (Exception e) {
				System.out.println("Problema ao descartar a imagem");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! Problema ao salvar as imagens.", "  "));
			}
			context.addMessage(null, new FacesMessage("Crop descartado!"));			
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Para descartar um crop, você tem que ter cropado uma imagem", "  "));
		}
	}
	
	
	
	
	
	
	

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public CroppedImage getCroppedImage() {
		return croppedImage;
	}

	public void setCroppedImage(CroppedImage croppedImage) {
		this.croppedImage = croppedImage;
	}

	public String getImgPathNovo() {
		return imgPathNovo;
	}

	public void setImgPathNovo(String imgPathNovo) {
		this.imgPathNovo = imgPathNovo;
	}

	public LesaoGeral getLesao() {
		return lesao;
	}

	public void setLesao(LesaoGeral lesao) {
		this.lesao = lesao;
	}

	public List<ImagemGeral> getImagensCropadas() {
		return imagensCropadas;
	}

	public void setImagensCropadas(List<ImagemGeral> imagensCropadas) {
		this.imagensCropadas = imagensCropadas;
	}

	public ImagemGeral getImgSelecionada() {
		return imgSelecionada;
	}

	public void setImgSelecionada(ImagemGeral imgSelecionada) {
		this.imgSelecionada = imgSelecionada;
	}


}
