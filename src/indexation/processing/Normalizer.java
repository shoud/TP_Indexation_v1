package indexation.processing;

import java.io.Serializable;
import java.text.Normalizer.Form;
import java.util.Iterator;
import java.util.List;

import indexation.content.Token;

public class Normalizer implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String normalizeType(String type)
	{
		type = java.text.Normalizer.normalize(type, Form.NFD)
				 .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		type = type.toLowerCase();
		if(type.isEmpty())
			return null;
		return type;
	}
	
	public void normalizeTokens(List<Token> tokens)
	{
		Token token = null;
		String type = null;
		Iterator<Token> iterator = tokens.iterator();
		while(iterator.hasNext())
		{
			token = iterator.next();
			type = normalizeType(token.getType());
			if (type == null)
				iterator.remove();
			else
				token.setType(type); 
		}
	}
	
		//Pour tester avec corpus2
		/*
		public static void main(String[] args)
		{
			Tokenizer tokenizer = new Tokenizer();
			Normalizer normalizer = new Normalizer();
			List<Token> test = tokenizer.tokenizeCorpus("../Common/corpus2");
			normalizer.normalizeTokens(test);
			System.out.println("----------------- Début du test ->");
			for(Token token : test)
				System.out.println(token.toString());
			 	System.out.println("----------------- Fin du test ->");
		 }
		 */
}
