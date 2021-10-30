package es.florida.AE3_LLibres;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Biblioteca {

	static int idUltim=5;
	
	public static class ListaLlibres{
		private List<Llibre> lista = new ArrayList<Llibre>();
		
		public ListaLlibres() {
			
		}
		
		public void anyadirLlibre(Llibre llibre) {
			lista.add(llibre);
		}
		
		public List<Llibre> getListaLlibres(){
			return lista;
		}
	}
	public static class Llibre{
		private String id, titulo, autor, any, editorial, pagines;
		
		public Llibre(String id, String titulo, String autor, String any, String editorial, String pagines) {
			this.id=id;
			this.titulo=titulo;
			this.autor=autor;
			this.any=any;
			this.editorial= editorial;
			this.pagines=pagines;
		}
		
		public String getId() {
			return id;
		}
		
		public String getTitulo() {
			return titulo;
		}
		
		public String getAutor() {
			return autor;
		}
		
		public String getAny() {
			return any;
		}
		
		public String getEditorial() {
			return editorial;
		}
		
		public String getPagines() {
			return pagines;
		}
	}
	
	public static void crearLlibre() {
		int idLlibre=idUltim ;
		Llibre llibre;
		ListaLlibres lista = new ListaLlibres();
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Desea anyadir nuevo libro (s/n?)");
		try {
			String respuesta = reader.readLine();
			while(respuesta.equals("s")) {
				System.out.print("Titulo: ");String titulo=reader.readLine();
				System.out.print("Autor: ");String autor=reader.readLine();
				System.out.print("Any: ");String any=reader.readLine();
				System.out.print("Editorial: ");String editorial=reader.readLine();
				System.out.print("Paginas: ");String pagines=reader.readLine();
				idLlibre++;
				llibre = new Llibre(Integer.toString(idLlibre),titulo,autor,any,editorial,pagines);
				lista.anyadirLlibre(llibre);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		escribirXML(lista);
		
	}
	
	public static void recuperarTots(){
		ListaLlibres lista = new ListaLlibres();
		Llibre llibre;
		int idUltim=0;
		try {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(new File("llibres.xml"));
		
		Element raiz = document.getDocumentElement();
		System.out.println("Contenidp XML " + raiz.getNodeName()+ ":");
		NodeList nodelist = document.getElementsByTagName("llibre");
		for(int i =0; i < nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			System.out.println("");
			Element eElement = (Element) node;
			String id =eElement.getAttribute("id");
			System.out.println("ID Llibre " + id);
			String titulo = eElement.getElementsByTagName("titol").item(0).getTextContent();
			System.out.println("Titulo: " + titulo);
			String autor = eElement.getElementsByTagName("autor").item(0).getTextContent();
			System.out.println("Autor: " + autor);
			String any = eElement.getElementsByTagName("any").item(0).getTextContent();
			System.out.println("Any de publicacio" +  any);
			String editorial = eElement.getElementsByTagName("editorial").item(0).getTextContent();
			System.out.println("Editorial: " + editorial);
			String pagines = eElement.getElementsByTagName("pagines").item(0).getTextContent();
			System.out.println("Pagines: " + pagines);
			llibre=new Llibre(id,titulo,autor, any, editorial, pagines);
			lista.anyadirLlibre(llibre);
			idUltim=Integer.parseInt(id);
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void borrarLlibre() {
		System.out.println("Introduce la ID del libro que deseas borrar");
		Scanner scanner = new Scanner(System.in);
		int idBorrar = scanner.nextInt();
		try {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(new File("llibres.xml"));
		Element raiz = document.getDocumentElement();
		NodeList nodelist = document.getElementsByTagName("llibre");
		Node item=nodelist.item(idBorrar);
		raiz.removeChild(item);
		
	
	
		
		}catch(Exception  e) {
		System.out.println("Error borrando el ID");
		}
	}
	
	
	public static void escribirXML(ListaLlibres lista) {
		try {
			DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = dFact.newDocumentBuilder();
			Document doc = build.newDocument();
			
			Element raiz = doc.createElement("llibres");
			doc.appendChild(raiz);
			
			for(Llibre llibre : lista.getListaLlibres()) {
				Element llib=doc.createElement("llibre");
				String id = String.valueOf(llibre.getId());
				llib.setAttribute("id", id);
				raiz.appendChild(llib);
				
				Element titulo = doc.createElement("titol");
				titulo.appendChild(doc.createTextNode(String.valueOf(llibre.getTitulo())));
				llib.appendChild(titulo);
				
				Element autor = doc.createElement("autor");
				autor.appendChild(doc.createTextNode(String.valueOf(llibre.getAutor())));
				llib.appendChild(autor);
				
				Element any = doc.createElement("any");
				any.appendChild(doc.createTextNode(String.valueOf(llibre.getAny())));
				llib.appendChild(any);
				
				Element editorial = doc.createElement("editorial");
				editorial.appendChild(doc.createTextNode(String.valueOf(llibre.getEditorial())));
				llib.appendChild(editorial);
				
				Element pagines = doc.createElement("pagines");
				pagines.appendChild(doc.createTextNode(String.valueOf(llibre.getPagines())));
				llib.appendChild(pagines);
			}
			
			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			
			aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			
			aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			DOMSource source = new DOMSource(doc);
			try {
				FileWriter fw = new FileWriter("llibres2.xml");
				StreamResult result = new StreamResult(fw);
				aTransformer.transform(source, result);
				fw.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}catch (TransformerException ex) {
			System.out.println("Error escribiendo el documento");
		} catch (ParserConfigurationException ex) {
			System.out.println("Error construyendo el documento");
		}
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Llibre llibre;
		System.out.println("----------------------");
		System.out.println("1-Mostrar tots el titol de la biblioteca");
		System.out.println("2-Mostrar informacion detallada de un llibre");
		System.out.println("3-Crea un nou llibre");
		System.out.println("4-Actualitzar llibre");
		System.out.println("5-Borrar llibre");
		System.out.println("6-Tanca la biblioteca");

		Scanner scanner = new Scanner(System.in);
		int num = scanner.nextInt();
		
		switch(num) {
		case 1: recuperarTots();
				break;
				
		case 2:;	
				break;
				
		case 3: crearLlibre();		
				break;
		
		case 4: 
				break;
		
		case 5: borrarLlibre();
				break;
				
		case 6: System.exit(0);
				break;
		default:System.out.println("Numero invalido");
		
		}		
		
		
		
	}

}
