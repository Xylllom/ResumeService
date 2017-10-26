package outil;

import java.io.File;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public abstract class GestionXML {
	
	// Ouvre un fichier xml
	public static Document openXML(String path) throws ServletException{
		try{
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document original = builder.parse(new File(path));
			original.getDocumentElement().normalize();
			return original;
		}
		catch(Exception e){
			throw new ServletException("Le fichier XML n'existe pas ou n'est pas valide");
		}
	}
	
	// Récupère la liste des fichiers xml dans le dossier CONFIG
	public static String[] getConfigDispo(){
		File directory = new File(Reserve.DIRECTORY + File.separator + "Config");
		String[] dispo = new String[directory.list().length];
		int x = 0;
		for (int i=0; i<directory.list().length; i++){
			if (directory.list()[i].endsWith(".xml")){
				dispo[x] = directory.list()[i];
				x++;
			}
		}
		return dispo;
	}
}