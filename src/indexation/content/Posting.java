package indexation.content;

import java.io.Serializable;
/**
 * Classe Posting représentant un posting
 * @author thomas
 *
 */
public class Posting implements Comparable<Posting>, Serializable
{
	//Serializable
	private static final long serialVersionUID = 1L;
	//Le document id
	public int docId;
	
	/**
	 * Permet de mettre à jour le doId
	 * @param doId L'id du document
	 */
	public Posting(int doId)
	{
		//Récupération du doId
		this.docId = doId;
	}

	@Override
	/**
	 * Permet de comparer deux posting avec le doId
	 */
	public int compareTo(Posting posting)
	{
		//Comparaison des docId
		return docId-posting.docId;	
	}
	
	@Override
	/**
	 * Comparaison de deux objets posting
	 */
	public boolean equals(Object o)
	{
		//Si les deux posting sont identiques
		if(this.compareTo((Posting)o) == 0)
			return true;
		//Si ils ne sont pas identiques
		return false;
	}
	
	@Override
	/**
	 * Permet d'afficher le doId du posting
	 */
	public String toString()
	{
		return(docId + " ");
	}
}
