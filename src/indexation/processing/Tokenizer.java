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
 * Classe chargée d’effectuer la tokénisation
 * @author thomas
 *
 */
public class Tokenizer implements Serializable
{
	//Car objet serialisable
	private static final long serialVersionUID = 1L;
	//Permet de définir un docId par document
	private static int docId = 0;
	//Pour ne pas prendre en compte ce qui n'est pas une lettre ou un chiffre
	private String regex = "[^\\pL\\pN]";
	
	/**
	 * Tokénise la chaîne de caractères string passée en paramètre, et renvoie une liste correspondant à cette
	 * décomposition
	 * @param string chaîne de caractères à tokeniser
	 * @return  liste correspondant à la décomposition
	 */
	 public List<String> tokenizeString(String string)
	 {
		 //La liste retournée		 
		 List<String> list = new LinkedList<String>(); //ArrayList
		 //Récupération de chaque mots
		 for(String mot : string.split(regex))
			 //Si mot n'est pas vide
			 if(!mot.equals(""))
				 //Rajout du mot dans la liste
				 list.add(mot);
		 //Retourne la liste correspondant à la décomposition
		 return list;
	 }
	 /**
	  * Chargée d’effectuer la tokénisation du document passé en paramètre. Les tokens obtenus doivent être 
	  *	définition d’un fichier inverse renvoyés en complétant la liste tokens passée en paramètre
	  * @param document Le document à tokeniser
	  * @param docId L'id lié au document
	  * @param tokens La liste de token a mettre à jour
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
				 //Récupération de la ligne à traiter
				 String line = scanner.nextLine();
				 //Récupération de tout les mots dans une liste
				 List<String> list = tokenizeString(line);
				 for(String type : list)
					 //Si le type n'est pas vide
					 if(!type.equals(""))
						 //Rajout du type dans la liste de résultat
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
	  * @param folder Le dossier où ce situe les docuement à traiter
	  * @return La liste de token réalisée
	  */
	 public List<Token> tokenizeCorpus(String folder)
	 {
		 //La liste contenant les tokens à retourner
		 ArrayList<Token> list = new ArrayList<Token>();
		 //Pour ouvrir un dossier
		 File file = new File(folder);
		 //Récupération de la liste des documents dans le dossier
		 String [] files = file.list();
		 //Trie les documents par nom
		 Arrays.sort(files);	
		 //Pour chaques documents
		 for(String nameFile : files )
		 {
			 //Récupération du document grâce à son nom
			 File document = new File(folder + File.separator + nameFile);
			 //Tokenisation du document, mise à jour de la liste de token
			 tokenizeDocument(document, docId, list);
			 docId++;
		 }
		 //La liste de token du corpus
		 return list;
	}
	 
	 /**
	  * Permet de tester la classe Tokenizer
	  * ATTENTION nom du dossier Common = TP_Indexation_Common car nom du dépot git
	  * @param args
	  */
	 /*
	 public static void main(String[] args)
	 {
		 //Création de l'objet permettant de tokeniser un corpus
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
