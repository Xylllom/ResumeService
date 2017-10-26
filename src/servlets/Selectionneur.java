package servlets;

import java.io.File;
import java.io.IOException;

import java.util.zip.ZipException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

import outil.Erreur;
import outil.GestionArchive;
import outil.GestionXML;

@WebServlet("/select")
public class Selectionneur extends HttpServlet {
	
	// ETAPE 2
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ZipException { 
		System.out.println("\n### Selectionneur $Get ###\n");
		if (!Erreur.valid(request)){
			response.sendRedirect(request.getContextPath() + "/clean");
			this.destroy();
		}
		else {
			String file = (String) request.getSession().getAttribute("file_path");
			String folder = (String) request.getSession().getAttribute("directory_path");
			GestionArchive.extraire(file, folder);
			request.getSession().removeAttribute("file_path");
			final Document corpus = GestionXML.openXML(folder+File.separator+"corpus.xml");
			String[] name = new String[corpus.getElementsByTagName("CORPUS").getLength()];
			request.getSession().setAttribute("nomCorpus", name);
			request.getSession().setAttribute("nbCorpus", name.length);
			response.sendRedirect(request.getContextPath() + "/select.jsp");
			//request.getRequestDispatcher("select.jsp").forward(request, response);
		}
	}

	// ETAPE 3
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("\n### Selectionneur $Post ###\n");
			if (!Erreur.valid(request)){
				response.sendRedirect(request.getContextPath() + "/clean");
				this.destroy();
			}
			else {
				String[] test = request.getParameterValues("choix");
				if (test.length == 0){
					throw new NullPointerException();
				}
				request.getSession().setAttribute("id_corpus", test);
				request.getSession().setAttribute("dispo", GestionXML.getConfigDispo());
				response.sendRedirect(request.getContextPath() + "/config.jsp");
				//request.getRequestDispatcher("config.jsp").forward(request, response);
			}
		//}
	}
}
