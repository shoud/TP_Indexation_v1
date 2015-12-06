package tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import indexation.content.Token;
import indexation.processing.Normalizer;
import indexation.processing.Tokenizer;

public class TermCounter
{
	/**
	 * Re�oit en param�tre
	 *	une liste de tokens normalis�s (i.e. de termes). La m�thode doit construire une map associant
	 *	� chaque terme son nombre d�occurrences dans la liste, et la renvoyer comme r�sultat.
	 * @param tokens Liste de tokens normalis�s
	 * @return Map de chaque terme et leur nombre d'occurences
	 */
	private static	Map<String,Integer> countTerms(List<Token> tokens)
	{
		//Cr�ation de la map � retourner
		Map<String, Integer> mapRes = new HashMap<String, Integer>();
		//Permet de compter le nombre de r�p�tition d'un terme
		Integer compteur;
		//Pour chaque tokens
		for(Token token : tokens)
		{
			//Si le terme n'est pas dans la HashMap, 0 it�ration
			if(mapRes.get(token.getType()) == null)
				//Rajout du terme dans la HashMap
				mapRes.put(token.getType(), 1);
			else
				//Rajout de + 1 dans la HashMap
				mapRes.put(token.getType(), mapRes.get(token.getType())+1);
		}
		return mapRes;
	}
	
	/**
	 * Re�oit en param�tres une map counts similaire � celle calcul�e par countTerms et un nom de fichier
	 *	fileName. La m�thode doit cr�er un fichier texte appel� fileName et y enregistrer le
	 *	contenu de la map
	 * @param counts Une map a enregistrer 
	 * @param fileName Le nom du fichier � cr�er 
	 */
	private static void	writeCounts(Map<String,Integer> counts, String fileName)
	{
		try
		{
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			PrintWriter writer = new PrintWriter(osw);
			
			Set cles = counts.keySet();
			Iterator it = cles.iterator();
			while (it.hasNext())
			{
				//R�cup�ration du terme
				String  terme = (String)it.next();
				//R�cup�ration du nombre d'it�ration du terme
				Integer nbterme = counts.get(terme);
				//Ecriture dans le fichier
				writer.println(terme + "\t" + nbterme);
			}
			writer.close();
		}catch(Exception e)
		{
			System.err.println(e);
		}

	}
	
	/**
	 * Re�oit en param�tres le nom folder du dossier contenant le corpus � traiter, et le nom outFile du fichier texte � cr�er
	 * @param folder Le nom du dossier contenant le corpus � traiter
	 * @param outFile Le nom du fichier text cr��
	 */
	private static void	processCorpus(String folder, String outFile)
	{
		//Initialisation du tokenize
		Tokenizer tokenizer = new Tokenizer();
		//Tokenisation du corpus
		List<Token> listToken = tokenizer.tokenizeCorpus(folder);
		//Initialisation du normaliser
		Normalizer normalizer = new Normalizer();
		//Normalisation des tokens
		normalizer.normalizeTokens(listToken);
		//Contage des termes
		Map<String,Integer> counts = countTerms(listToken);
		//Enregistrement des d�comptes
		writeCounts(counts, outFile);
	}
	
	/**
	  * Permet de tester la classe TermCounter
	  * @param args
	  */

		public static void main(String[] args)
		{
			TermCounter termCounter = new TermCounter();
			/*
			//Test de countTerms
			List<Token> test = new LinkedList<Token>();
			test.add(new Token("chat", 0));
			test.add(new Token("bateau", 0));
			test.add(new Token("chapeau", 0));
			test.add(new Token("chat", 0));
			test.add(new Token("chat", 0));
			Map<String, Integer> mapRes = termCounter.countTerms(test);
			//System.out.println(mapRes.toString());
			
			//Test de writeCounts
			termCounter.writeCounts(mapRes, "testmap");*/
			
			//Test processCorpus
			 String CORPUS_FOLDER =".." + File.separator +"TP_Indexation_Common" + File.separator +"corpus";
			 String TERM_COUNT ="data" + File.separator +"term-count.txt";
			processCorpus(CORPUS_FOLDER, TERM_COUNT);
			
			
		}
}
