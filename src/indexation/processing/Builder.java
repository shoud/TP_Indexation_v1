package indexation.processing;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import indexation.Index;
import indexation.content.IndexEntry;
import indexation.content.Posting;
import indexation.content.Token;
/**
 *  Classe Builder charg�e d�impl�menter l�algorithme :
 *  1. (Obtenir une liste de paires (terme, docID) repr�sentant le corpus ;)
 *	2. Trier cette liste par terme puis docID ;
 *	3. Fusionner les occurrences multiples relatives au m�me document ;
 *	4. Grouper les occurrences multiples relatives � des documents diff�rents afin de
 *	produire les listes de postings
 * @author thomas
 *
 */
public class Builder
{
	/**
	 *  Re�oit la liste tri�e des tokens normalis�s et en supprime les occurrences multiples
	 *	relatives au m�me document
	 * @param tokens La liste de token
	 * @return Nombre de tokens cons�cutifs dont les types diff�rent
	 */
	private int filterTokens(List<Token>tokens)
	{
		//Pour compter le nombre de tokens ayant des types diff�rents
		int compteur = 0;
		//Pour parcourir le liste de token et supprimer facilement 
		Iterator<Token> iterator = tokens.iterator();
		//Le token suivant
		Token tokenSuivant = null;
		//Initialisation du token precedent 
		Token tokenPrecedent = null;
		while(iterator.hasNext())
		{
			//R�cup�ration du token suivant
			tokenSuivant = iterator.next();
			//Si on est dans la premi�re it�ration de la boucle
			if(tokenPrecedent==null)
				//Intialisation du compteur � 1 car pas de precedent
				compteur = 1;
			//Si il y a un precedent
			else
			{ 
				//Si les deux tokens cons�cutifs sont identique
				if(tokenPrecedent.equals(tokenSuivant))
					//Supression du doublon
					iterator.remove();
				//Si les types des deux tokens sont diff�rents
				//On peut pas utiliser compareTo de token car elle utilse le docId si type identiques
				if(!tokenPrecedent.getType().equals(tokenSuivant.getType()))
					//Incrementation du compteur de 1 car les types diff�rent 
					compteur++;
			}
			//On garde le token precdedent
			tokenPrecedent = tokenSuivant;
		}
		//Retourne le nombre de type cons�cutifs diff�rents
		return compteur;
	}
	/**
	 *  Remplir le tableau data de l�index avec le contenu de la liste re�ue
	 * @param tokens Liste tri�e filtr�e des tokens normalis�s
	 * @param index Index vide
	 * @return  le nombre total de postings plac�s dans les listes associ�es aux
	 *	diff�rentes entr�es constituant l�index
	 */
	private int buildPostings(List<Token> tokens, Index index)
	{
		//Nombre total de postings plac�s 
		int compteur = 0;
		//La position dans data
		int positionData = 0;
		//Le terme courant
		String termeCourant = null;
		//On, parcour tout les tokens
		for(Token token: tokens)
		{ 
			//Premi�re it�ration de la boucle
			if(termeCourant == null)
				//Cr�ation du nouvelle indexEntry dans data
				index.data[positionData] = new IndexEntry(token.getType());
			//Si le terme courant et diff�rent de celui du token actuel
			if(!termeCourant.equals(token.getType()))
			{ 
				//D�placement dans le tableau data
				positionData++;
				//R�cup�ration du nouveau terme courant
				termeCourant = token.getType();
				//Cr�ation d'un nouvelle indexEntry avec le nouveau terme
				index.data[positionData] = new IndexEntry(termeCourant);
			}
			//Cr�ation d'un nouveau posting
			Posting posting = new Posting(token.getDocId());
			//Rajout du posting dans l'indexEntry du tableau data
			index.data[positionData].postings.add(posting);
			//Incrementation car un posting plac�
			compteur++;
		}
		//Le nombre total de posting plac�s
		return compteur;
	}
	
	/**
	 * Permet de construire un index
	 * @param tokens Liste des tokens normalis�s
	 * @return l�index construit
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
		//Cr�ation de l'index avec le nombre de token restant
		Index index = new Index(size);
		System.out.println("Building posting lists...");
		start = System.currentTimeMillis();
		//Cr�ation des posting en fonction des termes
		long nbPosting = buildPostings(tokens, index);
		end = System.currentTimeMillis();
		System.out.println(nbPosting + " postings listed, duration="+ (end - start) +" ms");
		//L'index cr��
		return index;

	}

}
