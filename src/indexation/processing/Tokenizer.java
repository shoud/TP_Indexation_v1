package indexation.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import indexation.content.Token;
/**
 * Classe charg�e d�effectuer la tok�nisation
 * @author thomas
 *
 */
public class Tokenizer implements Serializable
{
	//Car objet serialisable
	private static final long serialVersionUID = 1L;
	//Permet de d�finir un docId par document
	private static int docId = 0;
	//Pour ne pas prendre en compte ce qui n'est pas une lettre ou un chiffre
	private String regex = "[^\\pL\\pN]";
	
	/**
	 * Tok�nise la
	 * cha�ne de caract�res string pass�e en param�tre, et renvoie une liste correspondant � cette
	 * d�composition
	 * @param string cha�ne de caract�res � tokeniser
	 * @return  liste correspondant � la d�composition
	 */
	 public List<String> tokenizeString(String string)
	 {
		 //La liste retourn�e		 
		 List<String> list = new LinkedList<String>(); //ArrayList
		 //R�cup�ration de chaque mots
		 for(String mot : string.split(regex))
			 //Si mot n'est pas vide
			 if(!mot.equals(""))
				 //Rajout du mot dans la liste
				 list.add(mot);
		 //Retourne la liste correspondant � la d�composition
		 return list;
	 }
	 /**
	  * Charg�e d�effectuer la tok�nisation du document pass� en param�tre. Les tokens obtenus doivent �tre 
	  *	d�finition d�un fichier inverse renvoy�s en compl�tant la liste tokens pass�e en param�tre
	  * @param document Le document � tokeniser
	  * @param docId L'id li� au document
	  * @param tokens La liste de token a mettre � jour
	  */
	 private void tokenizeDocument(File document, int docId, List<Token> tokens)
	 {
		 try
		 {
			 //Ouverture du document
			 FileInputStream fis = new FileInputStream(document);
			 InputStreamReader isr = new InputStreamReader(fis);
			 //Ouverture du flux 
			 Scanner scanner = new Scanner(isr);
			 //Lire le docuement ligne par ligne
			 while(scanner.hasNextLine())
			 { 
				 //R�cup�ration de la ligne � traiter
				 String line = scanner.nextLine();
				 //R�cup�ration de tout les mots dans une liste
				 List<String> list = tokenizeString(line);
				 for(String type : list)
					 //Si le type n'est pas vide
					 if(!type.equals(""))
						 //Rajout du type dans la liste de r�sultat
						 tokens.add(new Token(type,docId));
			}
			 //Fermeture du flux
			 scanner.close();
		 }
		 //Si il n'est pas possible d'ouvrir le document
		 catch(FileNotFoundException e)
		 {
			 //Affichage de l'erreur
			 e.printStackTrace();
		 }	 
	 }
	 /**
	  * Permet de tokeniser un corpus en entier (plusieurs document dans un dossier)
	  * @param folder Le dossier o� ce situe les docuement � traiter
	  * @return La liste de token r�alis�e
	  */
	 public List<Token> tokenizeCorpus(String folder)
	 {
		 //La liste contenant les tokens � retourner
		 ArrayList<Token> list = new ArrayList<Token>();
		 //Pour ouvrir un dossier
		 File file = new File(folder);
		 //R�cup�ration de la liste des documents dans le dossier
		 String [] files = file.list();
		 //Trie les documents par nom
		 Arrays.sort(files);	
		 //Pour chaques documents
		 for(String nameFile : files )
		 {
			 //R�cup�ration du document gr�ce � son nom
			 File document = new File(folder + File.separator + nameFile);
			 //Tokenisation du document, mise � jour de la liste de token
			 tokenizeDocument(document, docId, list);
			 docId++;
		 }
		 //La liste de token du corpus
		 return list;
	}
	 
	 /**
	  * Permet de tester la classe Tokenizer
	  * ATTENTION nom du dossier Common = TP_Indexation_Common car nom du d�pot git
	  * @param args
	  */
	 /*
	 public static void main(String[] args)
	 {
		 //Cr�ation de l'objet permettant de tokeniser un corpus
		 Tokenizer tokenizer = new Tokenizer();
		 //On tekenise un petit ensemble de document
		 List<Token> test = tokenizer.tokenizeCorpus(".."+ File.separator + "TP_Indexation_Common" + File.separator + "corpus2");
		 System.out.println("Affichage du resulat du test : ");
		 //Pour tout les tokens de la liste test
		 for(Token token : test)
			 //Affichage du token
			 System.out.println(token.toString());
	 }*/
}
