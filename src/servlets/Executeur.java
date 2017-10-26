package servlets;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import outil.Erreur;
import outil.Reserve;

@WebServlet(urlPatterns ="/execute")
public class Executeur extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\n### Executeur $Post ###\n");
		if (!Erreur.valid(request)){
			response.sendRedirect(request.getContextPath() + "/clean");
			this.destroy();
		}
		else{			
			String[] args = new String[2];
			args[0] = "-c";
			/*
			 * CHEMIN COMPLET
			 * SCORING OBLIGATOIRE
			 * -o FAIT BOUCLER L'ALGO
			 * 
			 */
			args[1] = (String) request.getSession().getAttribute("custom");
			request.getSession().removeAttribute("custom");
			PrintStream console = System.out;
			System.out.println("Début Algo");
			long debut = System.currentTimeMillis();
			try {
				System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream( (String) request.getSession().getAttribute("directory_path")+File.separator+"result.txt")),true));
				System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream( Reserve.DIRECTORY + File.separator +"log.txt")),true));
				launcher.Start.main(args);
			} catch (Exception e) {
				throw new RuntimeException("L'algorithme a rencontré un problème");
			}
			System.setOut(console);
			System.out.println("Terminé en : "+TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-debut)+" secondes");
			response.sendRedirect(request.getContextPath() + "/resultat.jsp");
			//request.getRequestDispatcher("resultat.jsp").forward(request, response);
		}
	}
		
		

	// ETAPE 6
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\n### Executeur $Get ###\n");
		throw new UnavailableException("Cette méthode n'est pas utilisée");
	}
}