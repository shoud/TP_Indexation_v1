package indexation.content;

import java.io.Serializable;

public class Posting implements Comparable<Posting>, Serializable
{
	private static final long serialVersionUID = 1L;
	public int docId;
	
	public Posting(int doId)
	{
		this.docId = doId;
	}

	@Override
	public int compareTo(Posting posting)
	{
		return docId-posting.docId;	
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this.compareTo((Posting)o) == 0)
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		return(docId + "\n");
	}
}
