package servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import outil.Erreur;
import outil.GestionXML;
import outil.Reserve;

@WebServlet("/config")
public class Configurateur extends HttpServlet {
	
	private NodeList scoring;
	private NodeList process;
	private NodeList summary;	
	private String[] customVal;
	private String[] customName;
	
	// ETAPE 5
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		System.out.println("\n### Configurateur $Post ###\n");
		if (!Erreur.valid(request)){
			response.sendRedirect(request.getContextPath() + "/clean");
			this.destroy();
		}
		else{
			String path = Reserve.DIRECTORY + File.separator + "Config" + File.separator+request.getSession().getAttribute("config");
			final Document template = GestionXML.openXML(path);
			this.initListeCorpus(template,request);
			request.getSession().removeAttribute("nbCorpus");
			request.getSession().removeAttribute("config");
			this.initListeOptions(template);
			String[] test = request.getParameterValues("choix3");
			modifOptions(test);
			this.enableMultiThread(template, request);
			try {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(template);
				String config_file = request.getSession().getAttribute("directory_path")+File.separator+"custom.xml";
				StreamResult result = new StreamResult(new File(config_file));
				transformer.transform(source, result);
				request.getSession().setAttribute("custom",config_file);
			} 
			catch (TransformerException e) {
				throw new ServletException("La création du fichier XML a échoué");
			}
		//response.sendRedirect(request.getContextPath() + "/execute");
		request.getRequestDispatcher("execute").forward(request, response);
		}
	}

	// ETAPE 4
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		System.out.println("\n### Configurateur $Get ###\n");
		if (!Erreur.valid(request)){
			response.sendRedirect(request.getContextPath() + "/clean");
			this.destroy();
		}
		else{
			String[] config = request.getParameterValues("choix2");
			request.getSession().setAttribute("config",config[0]);
			String path = Reserve.DIRECTORY + File.separator + "Config" + File.separator + request.getSession().getAttribute("config");
			final Document original = GestionXML.openXML(path);  	        
			if (original.getElementsByTagName("PROCESS").item(0).getChildNodes() != null && original.getElementsByTagName("SUMMARIZE_METHOD").item(0).getChildNodes() != null){
				this.initListeOptions(original);
			}
			else{
				throw new ServletException("Pas d'élément <process>, <scoring> ou <summary>");
			}
			this.initCorpusId(request);
			original.getDocumentElement().normalize();
			request.setAttribute("customVal", customVal);
			request.setAttribute("customName", customName);
			//response.sendRedirect(request.getContextPath() + "/custom.jsp");
			request.getRequestDispatcher("custom.jsp").forward(request, response);
		}
	}
	
	private void initCorpusId(HttpServletRequest request) {
		String[] nb = (String[]) request.getSession().getAttribute("id_corpus");
		for (int i = 0; i < nb.length; i++){
			this.customVal[0] = (i == 0) ? nb[i] : this.customVal[0] + nb[i];
			this.customVal[0]+="\t";
		}
		this.customVal[0].substring(0,this.customVal[0].length()-1);
		request.getSession().removeAttribute("id_corpus");
	}

	private void initListeOptions(Document doc) throws ServletException{
		this.summary = doc.getElementsByTagName("SUMMARIZE_METHOD");
		this.process = doc.getElementsByTagName("PROCESS");
		this.scoring = doc.getElementsByTagName("SCORING_METHOD");
		
		// SUMMARIZE NE PEUT PAS ETRE VIDE MAIS L'UN DES AUTRES OUI
		if (taille(scoring) == 0 ^ taille(process) == 0){
			this.customVal = new String[taille(process)+taille(scoring)+taille(summary)-1];
			this.customName = new String[taille(process)+taille(scoring)+taille(summary)-1];
		}
		else {
			this.customVal = new String[taille(process)+taille(scoring)+taille(summary)-2];
			this.customName = new String[taille(process)+taille(scoring)+taille(summary)-2];
		}
		modifOptions(new String[0]);
	}
	
	// CREE LES ELEMENTS CORPUS ET AJOUTE LES INFOS DANS LE FICHIER XML FINAL
	private void initListeCorpus(Document doc, HttpServletRequest request) {
		NodeList corpus = doc.getElementsByTagName("MULTICORPUS");
		int z = 0;
		// SI LE TEMPLATE EST DE TYPE [CUSTOM] aka les noeuds existent déjà
		if (corpus.item(0).hasChildNodes()){
			for(int i = 1; i < corpus.item(0).getChildNodes().getLength(); i+=2){
				corpus.item(0).getChildNodes().item(i).getChildNodes().item(1).setTextContent((String) request.getSession().getAttribute("directory_path")+File.separator+String.valueOf(z));
				corpus.item(0).getChildNodes().item(i).getChildNodes().item(3).setTextContent("ALL");
				z++;
				}
		}
		// SI LE TEMPLATE EST DE TYPE [VIDE] aka les noeuds sont inexistants
		else{
			for (int i = 0; i < (int) request.getSession().getAttribute("nbCorpus"); i ++){
				// e1 CORRESPOND AU TAG CORPUS (ID EN FONCTION DE i)
				Element e1 = doc.createElement("CORPUS");
				e1.setAttribute("ID",String.valueOf(i));
				// e2 CORRESPOND AU TAG INPUT_PATH (chemin vers le dossier)
				Element e2 = doc.createElement("INPUT_PATH");
				Text t = doc.createTextNode((String) request.getSession().getAttribute("directory_path")+File.separator+String.valueOf(i));
				// e3 CORRESPOND AU TAG DOCUMENT (indique quels fichiers vont être résumés)
				Element e3 = doc.createElement("DOCUMENT");
				e3.setAttribute("ID", "0");
				e3.setTextContent("ALL");
				e2.appendChild(t);
				e1.appendChild(e2);
				e1.appendChild(e3);
				corpus.item(0).appendChild(e1);
			}
		}
	}
	
	// PAS TRES JOLI MAIS J'AI FAIS CE QUE J'AI PU :D
	private void modifOptions(String[] param){
		int index = 0;
		if (taille(process) > 0){
			for(int i = 1; i < process.item(0).getChildNodes().getLength(); i+=2){
				if (process.item(0).getChildNodes().item(i).getNodeName() == "OPTION"){
					if (param.length == 0){
						this.customName[index] = process.item(0).getChildNodes().item(i).getAttributes().item(0).getNodeValue();
						this.customVal[index] = process.item(0).getChildNodes().item(i).getTextContent();
					}	
					else {
						process.item(0).getChildNodes().item(i).setTextContent(param[index]);
					}
					index++;
				}
			}
		}
		// LES OPTIONS DE SCORING
		if (taille(scoring) > 0){
			for(int i = 1; i < scoring.item(0).getChildNodes().getLength(); i+=2){
				if (scoring.item(0).getChildNodes().item(i).getNodeName() == "OPTION"){
					if (param.length == 0){
						this.customName[index] = scoring.item(0).getChildNodes().item(i).getAttributes().item(0).getNodeValue();
						this.customVal[index] = scoring.item(0).getChildNodes().item(i).getTextContent();
					}
					else{
						scoring.item(0).getChildNodes().item(i).setTextContent(param[index]);
					}
					index++;
				}
			}
		}
		// LES OPTIONS DE SUMMARIZE
		if (taille(summary) > 0){
			for(int i = 1; i < summary.item(0).getChildNodes().getLength(); i+=2){
				if (summary.item(0).getChildNodes().item(i).getNodeName() == "OPTION"){
					if (param.length == 0){
						this.customName[index] = summary.item(0).getChildNodes().item(i).getAttributes().item(0).getNodeValue();
		        		this.customVal[index] = summary.item(0).getChildNodes().item(i).getTextContent();
					}
					else{
						summary.item(0).getChildNodes().item(i).setTextContent(param[index]);
					}
	        		index++;
				}
			}
		}
	}
	
	private void enableMultiThread(Document doc,HttpServletRequest request){
		NodeList mt = doc.getElementsByTagName("MULTITHREADING");
		if (request.getParameter("multi") != null){
			mt.item(0).setTextContent("TRUE");
		}
		else {
			mt.item(0).setTextContent("FALSE");
		}
	}
	
	private int taille(NodeList liste){
		// PERMET DE TESTER SI UNE NODELISTE EST VIDE SANS GENERER D'ERREUR
		// LE ROUND PERMET D'EVITER LE 0 SI IL N'Y A QU'UN SEUL CHILDNODE
		try{ 
			return Math.round(liste.item(0).getChildNodes().getLength()/2);
		}
		catch(NullPointerException e){
			return 0;
		}
	}
}
