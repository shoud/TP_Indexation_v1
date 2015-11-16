package indexation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import indexation.content.IndexEntry;
import indexation.content.Token;
import indexation.processing.Builder;
import indexation.processing.Normalizer;
import indexation.processing.Tokenizer;

public class Index implements Serializable
{
	private static final long serialVersionUID = 1L;
	public IndexEntry[] data;
	public Normalizer normalizer;
	public Tokenizer tokenizer;
	
	public Index(int size)
	{
		System.out.println("Size est de = " + size);
		data = new IndexEntry[size];
	}

	void print()
	{
		for(IndexEntry indexEntry : data)
			indexEntry.toString();
	}
	
	public IndexEntry getEntry(String term)
	{
		int res = Arrays.binarySearch(data,term);
		if(res < 0)
			return null;
		return this.data[res];
	}
	
	public static Index indexCorpus(String folder)
	{
		long startTotal = System.currentTimeMillis();
		Index index;
		Tokenizer tokenizer = new Tokenizer();
		Normalizer normalizer = new Normalizer();
		Builder builder = new Builder();
		System.out.println("Tokenizing corpus...");
		long start = System.currentTimeMillis();
		List<Token> listToken = tokenizer.tokenizeCorpus(folder);
		long end = System.currentTimeMillis();
		System.out.println(listToken.size() + " tokens were found, duration="+ (end - start) +" ms");
		System.out.println("Normalizing tokens...");
		start = System.currentTimeMillis();
		normalizer.normalizeTokens(listToken);
		end = System.currentTimeMillis();
		System.out.println(listToken.size() + " tokens remaining after normalization, duration="+ (end - start) +" ms");
		System.out.println("Building index...");
		start = System.currentTimeMillis();
		index = builder.buildIndex(listToken);
		end = System.currentTimeMillis();
		System.out.println(listToken.size() + " tokens remaining after normalization, duration="+ (end - start) +" ms");
		index.normalizer = normalizer;
		index.tokenizer = tokenizer;
		System.out.println("Total duration="+ (System.currentTimeMillis() - startTotal) +" ms");
		index.print();
		return index;
	}
	
	public void write(String fileName)
	{
		try
		{
			File file = new File(fileName); 
			FileOutputStream fos = new FileOutputStream(file); 
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
		}catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		
	}
	
	public static Index read(String fileName)
	{
		try
		{
			File file = new File(fileName); 
			FileInputStream fis = new FileInputStream(file); 
			ObjectInputStream ois = new ObjectInputStream(fis);
			Index result = (Index) ois.readObject();
			ois.close();
			return result;
		}catch(Exception e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}
}
