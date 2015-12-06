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
	 * Reçoit en paramètre
	 *	une liste de tokens normalisés (i.e. de termes). La méthode doit construire une map associant
	 *	à chaque terme son nombre d’occurrences dans la liste, et la renvoyer comme résultat.
	 * @param tokens Liste de tokens normalisés
	 * @return Map de chaque terme et leur nombre d'occurences
	 */
	private static	Map<String,Integer> countTerms(List<Token> tokens)
	{
		//Création de la map à retourner
		Map<String, Integer> mapRes = new HashMap<String, Integer>();
		//Permet de compter le nombre de répétition d'un terme
		Integer compteur;
		//Pour chaque tokens
		for(Token token : tokens)
		{
			//Si le terme n'est pas dans la HashMap, 0 itération
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
	 * Reçoit en paramètres une map counts similaire à celle calculée par countTerms et un nom de fichier
	 *	fileName. La méthode doit créer un fichier texte appelé fileName et y enregistrer le
	 *	contenu de la map
	 * @param counts Une map a enregistrer 
	 * @param fileName Le nom du fichier à créer 
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
				//Récupération du terme
				String  terme = (String)it.next();
				//Récupération du nombre d'itération du terme
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
	 * Reçoit en paramètres le nom folder du dossier contenant le corpus à traiter, et le nom outFile du fichier texte à créer
	 * @param folder Le nom du dossier contenant le corpus à traiter
	 * @param outFile Le nom du fichier text créé
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
		//Enregistrement des décomptes
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
