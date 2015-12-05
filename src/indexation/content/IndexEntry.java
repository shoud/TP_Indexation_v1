package indexation.content;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Repr�snetion d'une entr�e de l'index
 * @author thomas
 *
 */
public class IndexEntry implements Comparable<IndexEntry>, Serializable
{
	//Serializable
	private static final long serialVersionUID = 1L;
	//Terme concern�
	public String term;
	//Les postings associ�s � au terme
	public ArrayList<Posting> postings;
	int frequency;
	
	public IndexEntry(String term, ArrayList<Posting> postings)
	{
		frequency = 0;
		this.term = term;
		this.postings = postings;
	}
	
	/**
	 * Cr�e une instance contenant le terme term et une liste de postings vide 
	 * @param term
	 */
	public IndexEntry(String term)
	{ 
		//Initialisation de la fr�quence � 0
		frequency = 0;
		//Le terme 
		this.term = term;
		//Cr�ation de la liste de posting vide
		postings = new ArrayList<Posting>();
	}
	
	@Override
	/**
	 * Comparaison de deux indexEntry avec leur terme
	 */
	public int compareTo(IndexEntry indexEntry)
	{
		//Comparaison des deux termes
		return term.compareTo(indexEntry.term);	
	}
	
	@Override
	/**
	 * Savoir si deux objet IndexEntry sont �gaux
	 */
	public boolean equals(Object o)
	{
		//Si les deux objets sont �gaux
		if(this.compareTo((IndexEntry)o) == 0)
			return true;
		//S'ils ne sont pas �gaux
		return false;
	}
	
	@Override
	/**
	 * Affichage d'un indexEntry
	 */
	public String toString()
	{
		//La chaine pour l'affichage, ajout du terme
		String res = "<" + term + " ( ";
		//Pour tout les postings du terme
		for(Posting posting : postings)
			//Rajout de l'afficage du posting dans la chaine
			res += posting.toString() + " ";
		//Rajout de la fr�quence dans la chaine
		res += "frequency = " + frequency + " "; 
		res += ")>\n";
		//Retourne la chaine permettant d'afficher un indexEntry
		return res;
	}
	
	public void addPosting(int docId)
	{
		postings.add(new Posting(docId));
	}
	
	public void frequencyPlusPlus()
	{
		frequency++;
	}

}
