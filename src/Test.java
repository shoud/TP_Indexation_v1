import java.io.File;
import java.util.LinkedList;
import java.util.List;

import indexation.Index;
import indexation.content.Posting;
import query.AndQueryEngine;
/**
 * Permet de tester l'indexation
 * @author thomas
 *
 */
public class Test
{
	//le chemin du corpus remplacer TP_Indexation_Common par Common si le dépot n'est pas utilisé
	static String CORPUS_FOLDER =".." + File.separator +"TP_Indexation_Common" + File.separator +"corpus2";
	static String INDEX_DATA =".." + File.separator +"data" + File.separator +"index.data";
	
	/**
	 * Chargée de construire et afficher un index
	 */
	public static void testIndexation()
	{
		Index index = Index.indexCorpus(CORPUS_FOLDER);
		//index.write(INDEX_DATA);
	}
	
	private static List<String> getFileNames(List<Posting> postings)
	{
		String name = null;
		File fileFolder = new File(CORPUS_FOLDER);
		File fileText[] = fileFolder.listFiles();
		List<String> listFileNames = new LinkedList<String>();
		for(Posting posting: postings)
		{ 
			name = fileText[posting.docId].getName();
			listFileNames.add(name);
		}
		return listFileNames;
	}
	
	private static void testQuery()
	{ 
		String query = "project"; 
		Index index = Index.read(INDEX_DATA);
		AndQueryEngine andQueryEngine = new AndQueryEngine(index);
		List<Posting> listPosting = andQueryEngine.processQuery(query);
		System.out.println(getFileNames(listPosting));
	}
	
	public static void main(String[] args)
	{
		testIndexation();
		//testQuery();
	}
}
