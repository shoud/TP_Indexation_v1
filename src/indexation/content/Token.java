package indexation.content;

/**
 * Classe permettant de représenter un token basique
 * @author thomas
 *
 */
public class Token implements Comparable<Token>
{
	//type associé au token
	public String type;
	//Représentation du docID
	int docId;
	
	/**
	 * Constructeur public chargé d’initialiser ces deux champs grâce aux paramètres reçus.
	 * @param type Type associé au token
	 * @param doId Représentation du docID
	 */
	public Token(String type, int doId)
	{
		//Récupération du type
		this.type = type;
		//Récupération du docID
		this.docId = doId;
	}
	
	@Override
	/**
	 *  Renvoie un entier négatif, nul ou positif si le token considéré (i.e. l’objet this) est respectivement plus
	 *	petit, égal ou plus grand que le token passé en paramètre token. On comparera les tokens en
	 *	utilisant le type comme critère primaire, et en cas d’égalité, le docID comme critère
	 *	secondaire.
	 * @param token Le token à comparer
	 */
	public int compareTo(Token token)
	{
		//Compare les deux chaines de caractéres
		int res = type.compareTo(token.type);
		//Si les chaines ne sont pas égaux
		if(res != 0)
			return res;
		//Si les types sont égaux
		return docId-token.docId;	
	}
	
	@Override
	/**
	 * Renvoie true si l’objet passé en paramètre est
	 * le même que l’objet considéré
	 */
	public boolean equals(Object o)
	{
		//Si les tokens sont égaux
		if(this.compareTo((Token)o) == 0)
			return true;
		//Si les tokens ne sont pas égaux
		return false;
	}
	
	@Override
	/**
	 *  Renvoie une chaîne de caractères représentant le token
	 *	considéré
	 */
	public String toString()
	{
		return("(" + type + ", " + docId + ")\n");
	}
	
	//Permet de récupérer le type du token
	public String getType(){return type;}
	//Permet de mettre à jour le type du token
	public void setType(String type){this.type = type;}
	//Permet de récupérer le doId du token
	public int getDocId(){return docId;}
}
