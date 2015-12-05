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
/**
 *  Classe Index charg�e de repr�senter le fichier inverse
 * @author thomas
 *
 */
public class Index implements Serializable
{
	//Serializable
	private static final long serialVersionUID = 1L;
	//Pour stocker le lexique et les listes de postings associ�es aux termes
	public IndexEntry[] data;
	//Permet de tokeniser
	public Tokenizer tokenizer;
	//Permet de normaliser des tokens
	public Normalizer normalizer;
	
	/**
	 * Permet d'initialiser le tableau data d'indexEntry
	 * @param size La taille du tableau data
	 */
	public Index(int size)
	{
		//Initialisation du tableau
		data = new IndexEntry[size];
	}
	/**
	 * Affiche le contenu de l�index dans la console
	 */
	void print()
	{
		//Parcour tout le tableau data
		for(IndexEntry indexEntry : data)
			//Affichage de chaque IndexEntry
			System.out.println(indexEntry.toString());
	}
	/**
	 * Renvoie l�entr�e de l�index associ�e au terme term pass� en param�tre
	 * @param term Le terme que l'on cherche
	 * @return null Si le terme n'existe pas
	 * @return l'indexEntry du terme
	 */
	public IndexEntry getEntry(String term)
	{
		//Cr�ation d'un IndexEntry repr�sentant le terme
		IndexEntry indexEntry = new IndexEntry(term);
		//Cherche si le l'indexEntry repr�sentant le terme est dans le tableau
		int res = Arrays.binarySearch(data,indexEntry);
		//Si le terme n'est pas pr�sent
		if(res < 0)
			return null;
		//Si le terme est pr�sent on retourne son IndexEntry Corespondant 
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
