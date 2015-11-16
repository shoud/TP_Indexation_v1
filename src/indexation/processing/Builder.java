package indexation.processing;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import indexation.Index;
import indexation.content.IndexEntry;
import indexation.content.Posting;
import indexation.content.Token;

public class Builder
{

	private int filterTokens(List<Token>tokens)
	{
		int compteur = 0;
		Iterator<Token> interator = tokens.iterator();
		Token tokenPrecedent = null;
		while(interator.hasNext())
		{
			Token tokenSuivant = interator.next();
			if(tokenPrecedent==null)
				compteur = 1;
			else
			{ 
				if(tokenPrecedent.equals(tokenSuivant))
					interator.remove();
				if(!tokenPrecedent.type.equals(tokenSuivant.type))
					compteur++;
			}
			tokenPrecedent = tokenSuivant;
		}
		return compteur;
	}
	
	private int buildPostings(List<Token> tokens, Index index)
	{
		int compteur = 0;
		int i = 0;
		IndexEntry entry = null;
		for(Token token: tokens)
		{ 
			if(entry ==null || !entry.term.equals(token.type))
			{ 
				entry = new IndexEntry(token.type);
				index.data[i] = entry;
				i++;
			}
		Posting posting = new Posting(token.getDocId());
		entry.postings.add(posting);
		compteur++;
		}
		return compteur;
	}
	
	public Index buildIndex(List<Token> tokens)
	{
		System.out.println("Sorting tokens...");
		long start = System.currentTimeMillis();
		Collections.sort(tokens);
		long end = System.currentTimeMillis();
		System.out.println(tokens.size() + " tokens sorted, duration="+ (end - start) +" ms");
		System.out.println("Filtering tokens...");
		start = System.currentTimeMillis();
		int size = filterTokens(tokens);
		end = System.currentTimeMillis();
		System.out.println(tokens.size() + " tokens remaining,corresponding to "+ size +" terms, duration="+ (end - start) +" ms");
		Index index = new Index(size);
		System.out.println("Building posting lists...");
		start = System.currentTimeMillis();
		long nbPosting = buildPostings(tokens, index);
		end = System.currentTimeMillis();
		System.out.println(nbPosting + " postings listed, duration="+ (end - start) +" ms");
		return index;

	}

}
