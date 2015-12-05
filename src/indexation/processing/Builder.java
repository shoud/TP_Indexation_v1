package indexation.processing;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import indexation.Index;
import indexation.content.IndexEntry;
import indexation.content.Posting;
import indexation.content.Token;
/**
 *  Classe Builder chargée d’implémenter l’algorithme :
 *  1. (Obtenir une liste de paires (terme, docID) représentant le corpus ;)
 *	2. Trier cette liste par terme puis docID ;
 *	3. Fusionner les occurrences multiples relatives au même document ;
 *	4. Grouper les occurrences multiples relatives à des documents différents afin de
 *	produire les listes de postings
 * @author thomas
 *
 */
public class Builder
{
	/**
	 *  Reçoit la liste triée des tokens normalisés et en supprime les occurrences multiples
	 *	relatives au même document
	 * @param tokens La liste de token
	 * @return Nombre de tokens consécutifs dont les types diffèrent
	 */
	private int filterTokens(List<Token>tokens)
	{
		//Pour compter le nombre de tokens ayant des types différents
		int compteur = 0;
		//Pour parcourir le liste de token et supprimer facilement 
		Iterator<Token> iterator = tokens.iterator();
		//Le token suivant
		Token tokenSuivant = null;
		//Initialisation du token precedent 
		Token tokenPrecedent = null;
		while(iterator.hasNext())
		{
			//Récupération du token suivant
			tokenSuivant = iterator.next();
			//Si on est dans la première itération de la boucle
			if(tokenPrecedent==null)
				//Intialisation du compteur à 1 car pas de precedent
				compteur = 1;
			//Si il y a un precedent
			else
			{ 
				//Si les deux tokens consécutifs sont identique
				if(tokenPrecedent.equals(tokenSuivant))
					//Supression du doublon
					iterator.remove();
				//Si les types des deux tokens sont différents
				//On peut pas utiliser compareTo de token car elle utilse le docId si type identiques
				if(!tokenPrecedent.getType().equals(tokenSuivant.getType()))
					//Incrementation du compteur de 1 car les types diffèrent 
					compteur++;
			}
			//On garde le token precedent
			tokenPrecedent = tokenSuivant;
		}
		//Retourne le nombre de type consécutifs différents
		return compteur;
	}
	/**
	 *  Remplir le tableau data de l’index avec le contenu de la liste reçue
	 * @param tokens Liste triée filtrée des tokens normalisés
	 * @param index Index vide
	 * @return  le nombre total de postings placés dans les listes associées aux
	 *	différentes entrées constituant l’index
	 */
	private int buildPostings(List<Token> tokens, Index index)
	{
		//Nombre total de postings placés 
		int compteur = 0;
		//La position dans data
		int positionData = 0;
		//Le terme courant
		String termeCourant = null;
		//On, parcour tout les tokens
		for(Token token: tokens)
		{ 
			//Première itération de la boucle
			if(termeCourant == null)
			{
				//Récupération du nouveau terme courant
				termeCourant = token.getType();
				//Création du nouvelle indexEntry dans data
				index.data[positionData] = new IndexEntry(termeCourant);
			}
			//Si le terme courant et différent de celui du token actuel
			else if(!termeCourant.equals(token.getType()))
			{ 
				//Déplacement dans le tableau data
				positionData++;
				//Récupération du nouveau terme courant
				termeCourant = token.getType();
				//Création d'un nouvelle indexEntry avec le nouveau terme
				index.data[positionData] = new IndexEntry(termeCourant);
			}
			//Création d'un nouveau posting
			Posting posting = new Posting(token.getDocId());
			//Rajout du posting dans l'indexEntry du tableau data
			index.data[positionData].postings.add(posting);
			//Incrementation de la fréquence dans l'indexEntry
			index.data[positionData].setFrequency();
			//Incrementation car un posting placé
			compteur++;
		}
		//Le nombre total de posting placés
		return compteur;
	}
	
	/**
	 * Permet de construire un index
	 * @param tokens Liste des tokens normalisés
	 * @return l’index construit
	 */
	public Index buildIndex(List<Token> tokens)
	{
		System.out.println("Sorting tokens...");
		long start = System.currentTimeMillis();
		//Tri la liste de tokens
		Collections.sort(tokens);
		long end = System.currentTimeMillis();
		System.out.println(tokens.size() + " tokens sorted, duration="+ (end - start) +" ms");
		System.out.println("Filtering tokens...");
		start = System.currentTimeMillis();
		//Supression des occurences multiples
		int size = filterTokens(tokens);
		end = System.currentTimeMillis();
		System.out.println(tokens.size() + " tokens remaining,corresponding to "+ size +" terms, duration="+ (end - start) +" ms");
		//Création de l'index avec le nombre de token restant
		Index index = new Index(size);
		System.out.println("Building posting lists...");
		start = System.currentTimeMillis();
		//Création des posting en fonction des termes
		long nbPosting = buildPostings(tokens, index);
		end = System.currentTimeMillis();
		System.out.println(nbPosting + " postings listed, duration="+ (end - start) +" ms");
		//L'index créé
		return index;

	}

}
