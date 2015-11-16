package indexation.content;

import java.io.Serializable;
import java.util.ArrayList;

public class IndexEntry implements Comparable<IndexEntry>, Serializable
{
	private static final long serialVersionUID = 1L;
	public String term;
	public ArrayList<Posting> postings;
	int frequency;
	
	public IndexEntry(String term, ArrayList<Posting> postings)
	{
		frequency = 0;
		this.term = term;
		this.postings = postings;
	}
	
	public IndexEntry(String term)
	{ 
		frequency = 0;
		this.term = term;
		postings = new ArrayList<Posting>();
	}
	
	@Override
	public int compareTo(IndexEntry indexEntry)
	{
		return term.compareTo(indexEntry.term);	
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this.compareTo((IndexEntry)o) == 0)
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		String res = "<" + term + " ( ";
		for(Posting posting : postings)
			res += posting.toString() + " ";
		res += "frequency = " + frequency + " "; 
		res += ")>\n";
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
