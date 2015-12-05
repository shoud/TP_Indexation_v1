package indexation.content;

/**
 * Classe permettant de repr�senter un token basique
 * @author thomas
 *
 */
public class Token implements Comparable<Token>
{
	//type associ� au token
	public String type;
	//Repr�sentation du docID
	int docId;
	
	/**
	 * Constructeur public charg� d�initialiser ces deux champs gr�ce aux param�tres re�us.
	 * @param type Type associ� au token
	 * @param doId Repr�sentation du docID
	 */
	public Token(String type, int doId)
	{
		//R�cup�ration du type
		this.type = type;
		//R�cup�ration du docID
		this.docId = doId;
	}
	
	@Override
	/**
	 *  Renvoie un entier n�gatif, nul ou positif si le token consid�r� (i.e. l�objet this) est respectivement plus
	 *	petit, �gal ou plus grand que le token pass� en param�tre token. On comparera les tokens en
	 *	utilisant le type comme crit�re primaire, et en cas d��galit�, le docID comme crit�re
	 *	secondaire.
	 * @param token Le token � comparer
	 */
	public int compareTo(Token token)
	{
		//Compare les deux chaines de caract�res
		int res = type.compareTo(token.type);
		//Si les chaines ne sont pas �gaux
		if(res != 0)
			return res;
		//Si les types sont �gaux
		return docId-token.docId;	
	}
	
	@Override
	/**
	 * Renvoie true si l�objet pass� en param�tre est
	 * le m�me que l�objet consid�r�
	 */
	public boolean equals(Object o)
	{
		//Si les tokens sont �gaux
		if(this.compareTo((Token)o) == 0)
			return true;
		//Si les tokens ne sont pas �gaux
		return false;
	}
	
	@Override
	/**
	 *  Renvoie une cha�ne de caract�res repr�sentant le token
	 *	consid�r�
	 */
	public String toString()
	{
		return("(" + type + ", " + docId + ")\n");
	}
	
	//Permet de r�cup�rer le type du token
	public String getType(){return type;}
	//Permet de mettre � jour le type du token
	public void setType(String type){this.type = type;}
	//Permet de r�cup�rer le doId du token
	public int getDocId(){return docId;}
}
