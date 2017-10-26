package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FileDeleteStrategy;

import outil.Erreur;
import outil.Reserve;

@WebServlet(urlPatterns ="/result")
public class Finisseur extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\n### Finisseur $Post ###\n");
		throw new UnavailableException("Cette méthode n'est pas utilisée");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\n### Finisseur $Get ###\n");
		if (!Erreur.valid(request)){
			response.sendRedirect(request.getContextPath() + "/clean");
			this.destroy();
		}
		else{
		
        File downloadFile = new File((String) request.getSession().getAttribute("directory_path")+File.separator+"result.txt");
        FileInputStream inStream = new FileInputStream(downloadFile);
        ServletContext context = getServletContext();
        String mimeType = context.getMimeType((String) request.getSession().getAttribute("directory_path")+File.separator+"result.txt");
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
        OutputStream outStream = response.getOutputStream();
        byte[] buffer = new byte[Reserve.BUFFER];
        int bytesRead = -1;
        while ((bytesRead = inStream.read(buffer)) != -1) {
        	outStream.write(buffer, 0, bytesRead);
        }
        inStream.close();
        outStream.close();
        FileCleaningTracker cleaner = new FileCleaningTracker();
        
        cleaner.track((String) request.getSession().getAttribute("directory_path")+File.separator+"result.txt", FileDeleteStrategy.FORCE);
        throw new ServletException("Finish");
        
        //response.setHeader("Location", request.getContextPath() + "/");
        //response.setStatus(HttpServletResponse.SC_FOUND);
        // IMPOSSIBLE DE SUPPRIMER LE RESULT.TXT (IN USE)
        // IMPOSSIBLE DE CALL response.sendRedirect CAR LE DOWNLOAD DU FICHIER OBLIGE DEJA UNE REPONSE DU SERVLET
        
        // AFFICHAGE DIRECTEMENT DANS LE NAVIGATEUR
		
		/*OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(Reserve.UPLOAD_DIRECTORY+File.separator+"result.txt");
		byte[] buffer = new byte[Reserve.BUFFER];
		int length;
		while ((length = in.read(buffer)) > 0){
		    out.write(buffer, 0, length);
		}
		in.close();
		out.flush();*/
	}}
}