package indexation.processing;

//import java.io.File; //Pour tester la classe
import java.io.Serializable;
import java.text.Normalizer.Form;
import java.util.Iterator;
import java.util.List;

import indexation.content.Token;
/**
 * Permet de faire une normalisation tr�s simple
 * @author thomas
 *
 */
public class Normalizer implements Serializable
{
	//Serializable
	private static final long serialVersionUID = 1L;

	/**
	 *  Prend en param�tre une cha�ne de caract�res type repr�sentant le type associ� � un token, et renvoie le terme r�sultant
	 *	de sa normalisation, sous la forme d�une cha�ne de caract�res.
	 *	La normalisation sera effectu�e simplement en transformant toute lettre majuscule en
	 * 	lettre minuscule, et en supprimant tous les signes diacritiques. 
	 * @param type Le type a normaliser
	 * @return null Si ce n'est pas un terme
	 * @return Le type normaliser sans majuscule ou caract�re sp�ciaux
	 */
	public String normalizeType(String type)
	{
		//Suppression des signes diacritiques 
		type = java.text.Normalizer.normalize(type, Form.NFD)
				 .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		//Mettre en minuscule
		type = type.toLowerCase();
		//Si le type est vide
		if(type.equals(""))
			return null;
		//Retourne le type normalis�
		return type;
	}
	/**
	 * Permet de normaliser un liste de tokens
	 * @param tokens La liste de token a normaliser
	 */
	public void normalizeTokens(List<Token> tokens)
	{
		//Token que l'on va normaliser
		Token token = null;
		//Le type a normaliser
		String type = null;
		//Pour ce d�placer dans la liste de token et en supprimer un facielement
		Iterator<Token> iterator = tokens.iterator();
		while(iterator.hasNext())
		{
			//R�cup�ration du token
			token = iterator.next();
			//R�cup�ration du type
			type = normalizeType(token.getType());
			//Si le type est null
			if(type == null)
				//Suppression du token
				iterator.remove();
			//Si le type est normalis� (non null)
			else
				//Mise � jour du type du token
				token.setType(type); 
		}
	}
	
	/**
	  * Permet de tester la classe Normalizer
	  * ATTENTION nom du dossier Common = TP_Indexation_Common car nom du d�pot git
	  * @param args
	  */
	/*
		public static void main(String[] args)
		{
			//Objet permettant de tokeniser un corpus
			Tokenizer tokenizer = new Tokenizer();
			//Objet permettant de normaliser des tokens
			Normalizer normalizer = new Normalizer();
			//Cr�ation de la liste de tokens
			List<Token> test = tokenizer.tokenizeCorpus(".."+ File.separator + "TP_Indexation_Common" + File.separator + "corpus2");
			//Normalisation de la liste de tokens
			normalizer.normalizeTokens(test);
			System.out.println("Affichage des tokens normalis�s");
			//Pour chaque tokens
			for(Token token : test)
				//Affichage du token
				System.out.println(token.toString());
		 }
		 */
}
