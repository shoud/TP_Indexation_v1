package indexation.content;

public class Token implements Comparable<Token>
{
	public String type;
	int docId;
	
	public Token(String type, int doId)
	{
		this.type = type;
		this.docId = doId;
	}

	@Override
	public int compareTo(Token token)
	{
		int res = type.compareTo(token.type);
		if(res != 0)
			return res;
		return docId-token.docId;	
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this.compareTo((Token)o) == 0)
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		return("(" + type + ", " + docId + ")\n");
	}
	
	public String getType(){return type;}
	public void setType(String type){this.type = type;}
	public int getDocId(){return docId;}
}
