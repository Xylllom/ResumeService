package servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InfoServeur {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
        response.setContentType("text/html");
        ServletOutputStream out =response.getOutputStream();
        out.println("<html>");
        out.println("<body>");
        out.println("<head>");
        out.println("<title>Informations connues par le Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>Type mime de la requête :"+request.getContentType()+"</p>");
        out.println("<p>Protocole de la requête :"+request.getProtocol()+"</p>");
        out.println("<p>Adresse IP du client :"+request.getRemoteAddr()+"</p>");
        out.println("<p>Nom du client : "+request.getRemoteHost()+"</p>");
        out.println("<p>Nom du serveur qui a reçu la requête :"+request.getServerName()+"</p>");
        out.println("<p>Port du serveur qui a reçu la requête :"+request.getServerPort()+"</p>");
        out.println("<p>scheme: "+request.getScheme()+"</p>");
        out.println("<p>liste des paramètres </p>");
        for (Enumeration e =request.getParameterNames() ; e.hasMoreElements() ; ) {
        	Object p = e.nextElement();
        	out.println("<p>&nbsp;&nbsp;nom : "+p+" valeur :"+request.getParameter(""+p)+"</p>");
        }
        out.println("</body>");
        out.println("</html>");
    }
}
