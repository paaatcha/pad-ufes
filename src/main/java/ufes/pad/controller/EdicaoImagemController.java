package ufes.pad.controller;

import java.io.File;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.CroppedImage;

@ManagedBean
@ViewScoped
public class EdicaoImagemController {

	private CroppedImage croppedImage;
	
	private String imgPath;
	
	private String imgPathNovo = "";
	
	@PostConstruct
	public void pegaImgURL () {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
				
		setImgPath(request.getParameter("imgpath"));
		
				
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
         
        
		
			
        System.out.println("Path da imagem cropada: " + imgPathNovo);
		System.out.println("Tamanho final da imagem: ");		
		System.out.println(getCroppedImage().getLeft());
		System.out.println(getCroppedImage().getTop());
		System.out.println(getCroppedImage().getHeight());
		System.out.println(getCroppedImage().getWidth());
		context.addMessage(null, new FacesMessage("Imagem editada com sucesso!"));
	}
	
	
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
}
