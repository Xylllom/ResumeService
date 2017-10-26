package outil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

@WebServlet("/erreur")
public class Erreur extends HttpServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  //serverDebug(request);
	  this.endSession(request, response);
      Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
      Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
      String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
      String message = throwable.getClass( ).getName( );
      if (servletName == null){
    	  	servletName = "Inconnu";
      }
      String requestUri = (String)
      request.getAttribute("javax.servlet.error.request_uri");
      if (requestUri == null){
    	  	requestUri = "Inconnu";
      }
      response.setContentType("text/html");
      		PrintWriter out = response.getWriter();
      		String title = "Rapport Exception";
      		String docType = "<!doctype html public \"-//w3c//dtd html 5.0 " + "transitional//fr\">\n";
      		out.println(docType + "<html>\n" + "<head><title>" + title + "</title></head>\n" + "<body bgcolor=\"#f0f0f0\">\n");
      switch(throwable.getClass().getName()){
      case "java.io.FileNotFoundException" : message = "Aucun fichier n'a été trouvé";
      		break;
      case "java.util.zip.ZipException" : message = "Le fichier soumis n'est pas une archive";
      		break;
      case "javax.servlet.ServletException" : message = "Le servlet a rencontré une erreur";
      		break;
      case "java.lang.NullPointerException" : message = "Le formulaire retourné est vide";
      		break;
      case "java.lang.ArrayIndexOutOfBoundsException" : message = "Le tableau n'a pas la bonne taille";
      		break;
      case "java.lang.ClassCastException" : message = "Un des services ne parvient pas à communiquer";
      		break;
      case "java.lang.Thread" : message = "L'application serveur a rencontré un problème";
      		break;
      default : message = "Une exception non gérée a été récupérée";
      		break;
      }
      
      // MOCHE MAIS FONCTIONNEL, PERMET DE RETOURNER AU DEBUT LORS DE LA FIN DU SERVICE
      if (throwable.getMessage() == "Finish"){
    	  response.setHeader("Location", request.getContextPath() + "/");
          response.setStatus(HttpServletResponse.SC_FOUND);
      }
      
      //serverDebug(request);
      if (statusCode != null){
    	 	out.print("<p>Erreur : " + statusCode +"</p>");
    	  	out.print("<p>Message Exception : " + throwable.getMessage()+"</p>");
    	  	out.print("<p>Exception : " + message + "</p>");
      }
      else{
    	  	out.println("<h2>Error information</h2>");
    	  	out.println("Nom du Servlet en cause : " + servletName + "</br></br>");
    	  	out.println("Exception : " + message + "</br></br>");
    	  	out.println("Requete URI : " + requestUri + "<br><br>");
         	out.println("Message Exception : " + throwable.getMessage( ));
      }
      out.println("<br/>");
      String baseURL = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().length() - request.getRequestURI().length()) + request.getContextPath() + "/";
      out.println("<a href=\""+ baseURL + "\">Recommencer</a>");
      out.println("</body>");
      out.println("</html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	private void serverDebug(HttpServletRequest request) {
		System.out.println("\n##"+request.getAttribute("javax.servlet.error.exception"));
		System.out.println("##"+request.getAttribute("javax.servlet.error.status_code"));
		System.out.println("##"+request.getAttribute("javax.servlet.error.exception_type"));
		System.out.println("##"+request.getAttribute("javax.servlet.error.message"));
		System.out.println("##"+request.getAttribute("javax.servlet.error.request_uri"));
		System.out.println("##"+request.getAttribute("javax.servlet.error.servlet_name")+"\n");
	}

	public static boolean valid(HttpServletRequest request){
		try {
		    if (request.getSession(false) == null){
		    	return false;
		    }
		} 
		catch(IllegalStateException ex) {
			return false;
		}
		return true;
	}
	
	public void endSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		if (Erreur.valid(request)){
			File destroy = new File((String) request.getSession().getAttribute("directory_path"));
		    //FileUtils.cleanDirectory(destroy);
			request.getSession().invalidate();
		    //FileDeleteStrategy.FORCE.delete(destroy);
			System.out.println("La session actuelle est terminée");
		}
		else{
			System.out.println("La session est terminée mais les fichiers n'ont pas pu être nettoyés");
		}
	}
}