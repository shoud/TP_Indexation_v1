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

public class AndQueryEngine
{
	Index index;
	static Comparator<List<Posting>> COMPARATOR = new Comparator<List<Posting>>()
	{

		@Override
		public int compare(List<Posting> listPosting01, List<Posting> listPosting02)
		{
			return listPosting01.size() - listPosting02.size();
		}

	};
	public AndQueryEngine(Index index)
	{
		this.index = index;
	}
	
	private void  splitQuery(String	query, List<List<Posting>> result)
	{
		String terme = null;
		IndexEntry entry = null;
		Tokenizer tokenizer = index.tokenizer;
		Normalizer normalizer = index.normalizer;
		List<String> listType = tokenizer.tokenizeString(query);
		for(String type: listType)
		{ 
			terme = normalizer.normalizeType(type);
			if(terme!=null)
			{ 
				entry = index.getEntry(terme);
				if(entry==null)
					result.add(new ArrayList<Posting>());
				else
					result.add(entry.postings);
			}
		}
	}
	
	private List<Posting> processConjunction(List <Posting> list1, List<Posting> list2)
	{
		int comparePost01Post02;
		Posting posting1 = null;
		Posting posting2 = null;
		Iterator<Posting> iteratorListe1 = list1.iterator();
		Iterator<Posting> iteratorListe2 = list2.iterator();
		List<Posting> listPosting = new LinkedList<Posting>();
		while((posting1!=null || iteratorListe1.hasNext()) && (posting2!=null || iteratorListe2.hasNext()))
		{ 
			if(posting1==null)
				posting1 = iteratorListe1.next();
			if(posting2==null)
				posting2 = iteratorListe2.next();
			comparePost01Post02 = posting1.compareTo(posting2);
			if(comparePost01Post02<0)
				posting1 = null;
			else
			{ 
				if(comparePost01Post02==0)
				{
					listPosting.add(posting1);
					posting1 = null;
					posting2 = null;
				}
				if(comparePost01Post02>0)
					posting2 = null;
			}	
		}
		return listPosting;
	}
	
	private List<Posting> processConjunctions(List<List<Posting>> postings)
	{
		List<Posting> listPostingFinale = null;
		Collections.sort(postings, COMPARATOR);
		List<Posting> listPosting01 = postings.get(0);
		postings.remove(0);
		List<Posting> listPosting02 = postings.get(0);
		postings.remove(0);
		listPostingFinale = processConjunction(listPosting01, listPosting02);
		for(List<Posting> listPosting: postings)
			listPostingFinale = processConjunction(listPostingFinale, listPosting);
		return listPostingFinale;
	}
	
	public List<Posting> processQuery(String query)
	{
		long debut = System.currentTimeMillis(), fin;
		List<Posting> listPostingRes;
		List<List<Posting>> listListPosting = new LinkedList<List<Posting>>();
		splitQuery(query,listListPosting);
		if(listListPosting.size() == 1)
		{
			listPostingRes = listListPosting.get(0);
			fin = System.currentTimeMillis();
			System.out.println("processQuery, duration="+(fin-debut)+" ms");
			return listPostingRes;
		}
		listPostingRes = processConjunctions(listListPosting);
		fin = System.currentTimeMillis();
		System.out.println("processQuery, duration="+(fin-debut)+" ms");
		return listPostingRes;
	}
}
