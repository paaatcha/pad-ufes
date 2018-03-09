package ufes.pad.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ufes.pad.model.Imagem;

public class ImagemController {

	List<Imagem> imagens = new ArrayList<Imagem>();
	
	public void processaImg(FileUploadEvent event) {
		try {
			UploadedFile arq = event.getFile();
			InputStream in = new BufferedInputStream(arq.getInputstream());
			File file = new File("src/main/webapp/dashboard/imgLesoes/" + arq.getFileName());
    		FileOutputStream fout = new FileOutputStream(file);

		while (in.available() != 0) {
			fout.write(in.read());
		}
			fout.close();
			FacesMessage msg = new FacesMessage("O Arquivo ", file.getName()+ " salvo.");
			FacesContext.getCurrentInstance().addMessage("msgUpdate", msg);
			
			Imagem img = new Imagem();
			img.setPath("imgLesoes/" + arq.getFileName());
			imagens.add(img);		
		
			System.out.println(img.getPath());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
}
