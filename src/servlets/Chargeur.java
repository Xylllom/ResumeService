package servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;

import outil.Erreur;
import outil.Reserve;

@WebServlet("/charge")
@MultipartConfig(fileSizeThreshold=1024*1024*3 /*TAILLE DU CACHE DISQUE*/,	maxFileSize=1024*1024*15 /*TAILLE MAX DU FICHIER UPLOAD*/, maxRequestSize=1024*1024*15 /*TAILLE MAX TOTALE*/)
public class Chargeur extends HttpServlet {
  	
	/* Methode doPost, permet de stocker un fichier sur un serveur
	 * <p>
	 * Récupère les données contenues dans la requête http et les copie dans un dossier sur le serveur en fonction de l'adresse IP du client
	 * @param request
	 * @param response
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("\n### Chargeur $Post ###\n");
    	if (!Erreur.valid(request)){
    		response.sendRedirect(request.getContextPath() + "/clean");
		}
    	else {
    		// ON CREE LE DOSSIER D'UPLOAD
    		new File (Reserve.DIRECTORY + File.separator + "Data" + File.separator +request.getRemoteAddr()).mkdir();
    		if (!this.isDirEmpty(Paths.get(Reserve.DIRECTORY + File.separator + "Data" + File.separator +request.getRemoteAddr()))){
    			FileUtils.cleanDirectory(new File(Reserve.DIRECTORY + File.separator + "Data" + File.separator +request.getRemoteAddr()));
    		}
            for(Part parts : request.getParts()){
            	// ON RECUPERE LE NOM DU FICHIER
                String fileName = extractFileName(parts);
                // ON VERIFIE QUE C'EST UNE ARCHIVE
            	if (fileName == null || !fileName.endsWith(".zip")) {
            		throw new ZipException("Impossible d'ouvrir le fichier");	
            	}
                try (InputStream input = parts.getInputStream()) {
                    // ON COPIE CE FICHIER A L'EMPLACEMENT SOUHAITE EN CONSERVANT LE NOM ET EN LE REMPLACANT SI IL EXISTE DEJA
                    Files.copy(input, Paths.get(Reserve.DIRECTORY + File.separator + "Data" + File.separator + request.getRemoteAddr() + File.separator + fileName),
                        			StandardCopyOption.REPLACE_EXISTING);
                }
                // SI LE CHARGEMENT RENCONTRE UNE ERREUR, ON LEVE UNE EXCEPTION
                catch (Exception e){
                    throw new FileNotFoundException("Le chargement du fichier n'a pas été effectué");
                }
                // ON SAUVEGARDE LE CHEMIN D'ACCES AU FICHIER DANS LA REQUETE TEMPORAIRE
                request.getSession().setAttribute("file_path", Reserve.DIRECTORY + File.separator + "Data" + File.separator + request.getRemoteAddr() + File.separator + fileName);
            }
            // ON SAUVEGARDE LE CHEMIN D'ACCES AU DOSSIER DANS LA VARIABLE DE SESSION
            request.getSession().setAttribute("directory_path", Reserve.DIRECTORY + File.separator + "Data" + File.separator + request.getRemoteAddr());
            // ON CONTINUE LE PROCESSUS DANS UN AUTRE SERVLET
            response.sendRedirect(request.getContextPath() + "/select");
            //request.getRequestDispatcher("select").forward(request, response);
    	}
    }
    
    /* Méthode doGet, initialise la session avec un timeout
	 * @param request
	 * @param response
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	System.out.println("\n### Chargeur $Get ###\n");
    	// ON AMORCE LE SERVICE WEB EN OUVRANT UNE SESSION
    	HttpSession session = request.getSession(true);
    	// ON MET UN TIMEOUT POUR DETRUIRE UNE SESSION APRES UNE CERTAINE PERIODE D'INACTIVITE
    	session.setMaxInactiveInterval(Reserve.AUTODELETE);
    	response.sendRedirect(request.getContextPath() + "/charge.jsp");
    	//request.getRequestDispatcher("charge.jsp").forward(request, response);
    }
    
    /* Methode pour récupérer uniquement le nom du fichier qui vient d'être tranféré
     * @param part
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    } 
    
    private boolean isDirEmpty(Path directory) throws IOException {
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }
}