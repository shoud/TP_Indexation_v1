package query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import indexation.Index;
import indexation.content.IndexEntry;
import indexation.content.Posting;
import indexation.processing.Normalizer;
import indexation.processing.Tokenizer;

/**
 * Destinée à traiter des requêtes ne contenant que des opérateurs ET 
 * @author thomas
 *
 */
public class AndQueryEngine
{
	//L'index a utiliser
	Index index;
	
	/**
	 * Pour pouvoir trier la liste de liste de posting
	 */
	static Comparator<List<Posting>> COMPARATOR = new Comparator<List<Posting>>()
	{

		@Override
		public int compare(List<Posting> listPosting01, List<Posting> listPosting02)
		{
			return listPosting01.size() - listPosting02.size();
		}

	};
	
	/**
	 * Permet d'initialiser le champ index 
	 * @param index L'index a récupérer 
	 */
	public AndQueryEngine(Index index)
	{
		//Récupération de l'index
		this.index = index;
	}
	
	/**
	 * Reçoit une requête query sous forme de chaîne de caractères et une liste vide result. Cette requête est supposée ne contenir que des
	 *	opérateurs ET (i.e. pour nous : n’importe quel séparateur). Comme son nom l’indique, la liste
	 *	est destinée à être complétée par la méthode.
	 * @param query La requete à traiter
	 * @param result La liste de posting résulats
	 */
	private void  splitQuery(String	query, List<List<Posting>> result)
	{
		String terme = null;
		IndexEntry indexEntry = null;
		//Tokenisation de la requete
		List<String> listType = index.tokenizer.tokenizeString(query);
		//Pour chaque type / terme de la requete
		for(String type: listType)
		{ 
			//Normalisation du type 
			terme = index.normalizer.normalizeType(type);
			//Si le terme n'est pas null
			if(terme != null)
			{ 
				//Recherche du terme dans l'index
				indexEntry = index.getEntry(terme);
				//Si le terme n'est pas dans l'index
				if(indexEntry == null)
					//Rajout d'une liste vide  dans la liste de resulat
					result.add(new LinkedList<Posting>());
				//Si le terme est présent dans l'index
				else
					//Rajout de la liste de posting du terme dans la liste de résultat
					result.add(indexEntry.postings);
			}
			//Si le terme est null
			else
				//Rajout d'une liste vide  dans la liste de resulat
				result.add(new LinkedList<Posting>());
		}
	}
	
	/**
	 * Reçoit deux listes de postings en paramètres, et calcule leur intersection, puis la renvoie comme
	 * résultat.
	 * @param list1 La première liste de posting
	 * @param list2 La seconde liste de posting
	 * @return Liste d'intersection des deux liste d'entrée
	 */
	private List<Posting> processConjunction(List <Posting> list1, List<Posting> list2)
	{
		//La liste de résultat
		List<Posting> listPosting = new LinkedList<Posting>();
		//Position dans la liste 2 car triée
		int pos = 0;
		//Parcour de chaque posting de la liste 1
		for(Posting posting01 : list1 )
		{
			//Parcour de chaque posting de la liste 2
			for(int i = pos; i < list2.size();i++)
			{
				//Si les postings sont identiques
				if(posting01.compareTo(list2.get(i)) == 0)
				{
					//On rajoute le posting dans la liste de résultat
					listPosting.add(posting01);
					//On change le départ dans la liste 2 car liste triée
					pos = i + 1;
				}	
			}
		}
		//Retourne la liste de posting identiques
		return listPosting;
	}
	
	/**
	 * Reçoit une liste de listes de postings en paramètre, et calcule leur intersection, puis la renvoie comme résultat.
	 * @param postings Liste de listes de postings
	 * @return La liste de posting d'interceptions
	 */
	private List<Posting> processConjunctions(List<List<Posting>> postings)
	{
		//La liste des intersections
		List<Posting> listPostingFinale = null;
		//Si il y a qu'une liste dans la liste de posting
		if(postings.size() == 1)
			//On retourne la seule liste 
			return postings.get(0);
		//Permet de trier la liste des listes
		Collections.sort(postings, COMPARATOR);
		//Supression des listes vides
		for(int i = 0; i < postings.size(); i++)
			if(postings.get(0).isEmpty())
				postings.remove(0);
		//Si il y a plus de deux liste de liste de posting
		if(postings.size() > 1)
		{
			//Récupération de la première liste
			List<Posting> listPosting01 = postings.get(0);
			//Supression de la première liste pour pas l'utiliser dans la boucle
			postings.remove(0);
			//Récupération  de la deuxième liste
			List<Posting> listPosting02 = postings.get(0);
			//Supression de la deuxieme liste dans la liste
			postings.remove(0);
			//Initialisation de la première valeur de la liste de résultat
			listPostingFinale = processConjunction(listPosting01, listPosting02);
			//Si il reste des listes
			if(!postings.isEmpty())
			{
				//Pour toutes les listes de postings restantes
				for(List<Posting> listPosting: postings)
					//Mise à jour de la liste de résultat
					listPostingFinale = processConjunction(listPostingFinale, listPosting);
			}
		}
		//La liste de posting après le traitement
		return listPostingFinale;
	}
	
	/**
	 * Prend en paramètre une requête et renvoie la liste des
	 *	documents (ou plutôt de leur docId) qui la satisfont.
	 * @param query La requete a executer
	 * @return Les docId contenant tout les mots de la requetes (ET)
	 */
	public List<Posting> processQuery(String query)
	{
		long debut = System.currentTimeMillis(), fin;
		//La liste de résultat
		List<Posting> listPostingRes;
		//La liste de liste de postings
		List<List<Posting>> listListPosting = new LinkedList<List<Posting>>();
		//Initialisation de la liste de liste de posting
		splitQuery(query,listListPosting);
		//La liste de posting contenant les mots de la requete
		listPostingRes = processConjunctions(listListPosting);
		fin = System.currentTimeMillis();
		System.out.println("Query processed, duration="+(fin-debut)+" ms");
		//La liste de posting 
		return listPostingRes;
	}
	
	/**
	  * Permet de tester la classe AndQueryEngine
	  * @param args
	  */
		/*
		public static void main(String[] args)
		{
			//Test de processConjunction
			List <Posting> list1 = new LinkedList<Posting>();
			List <Posting> list2 = new LinkedList<Posting>();
			AndQueryEngine andQueryEngine = new AndQueryEngine(null);
			//Initialisation des valeurs de test
			list1.add(new Posting(1));
			list1.add(new Posting(2));
			list1.add(new Posting(3));
			list1.add(new Posting(4));
			list1.add(new Posting(5));
			list1.add(new Posting(6));
			list1.add(new Posting(7));
			list1.add(new Posting(17));
			list1.add(new Posting(18));
			
			list2.add(new Posting(3));
			list2.add(new Posting(4));
			list2.add(new Posting(10));
			list2.add(new Posting(16));
			list2.add(new Posting(17));
			list2.add(new Posting(19));
			
			//Appel de la méthode
			List <Posting> test = andQueryEngine.processConjunction(list1, list2);
			//Affichage du résultat
			for(Posting posting : test)
				System.out.print(posting.toString());
			
			//Test de processConjunctions
			List <Posting> list1 = new LinkedList<Posting>();
			List <Posting> list2 = new LinkedList<Posting>();
			List <Posting> list3 = new LinkedList<Posting>();
			AndQueryEngine andQueryEngine = new AndQueryEngine(null);
			//Initialisation des valeurs de test
			list1.add(new Posting(1));
			list1.add(new Posting(2));
			list1.add(new Posting(3));
			list1.add(new Posting(4));
			list1.add(new Posting(5));
			list1.add(new Posting(6));
			list1.add(new Posting(7));
			list1.add(new Posting(17));
			list1.add(new Posting(18));
			
			list2.add(new Posting(3));
			list2.add(new Posting(4));
			list2.add(new Posting(10));
			list2.add(new Posting(16));
			list2.add(new Posting(17));
			list2.add(new Posting(19));
			
			list3.add(new Posting(1));
			list3.add(new Posting(4));
			list3.add(new Posting(19));
			list3.add(new Posting(26));
			
			List<List<Posting>> postings = new LinkedList<List<Posting>>();
			postings.add(list1);
			postings.add(list2);
			postings.add(list3);
			//Appel de la méthode
			List <Posting> test = andQueryEngine.processConjunctions(postings);
			//Affichage du résultat
			for(Posting posting : test)
				System.out.print(posting.toString());
		}
		*/
}
