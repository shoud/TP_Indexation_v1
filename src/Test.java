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
	static String INDEX_DATA ="data" + File.separator +"index.data";
	
	/**
	 * Chargée de construire et afficher un index
	 */
	public static void testIndexation()
	{
		Index index = Index.indexCorpus(CORPUS_FOLDER);
		index.write(INDEX_DATA);
		//Index index = Index.read(INDEX_DATA);
		//index.print();
	}
	
	/**
	 * Reçoit une liste de postings et renvoie la 
	 *	résolution de requêtes liste des noms de fichier correspondant aux docIds
	 * @param postings La liste de postings 
	 * @return Liste des noms corespondant aux postings (docId)
	 */
	private static List<String> getFileNames(List<Posting> postings)
	{
		//Le dossier du corpus
		File fileFolder = new File(CORPUS_FOLDER);
		//La liste des fichiers dans le dossier
		File fileText[] = fileFolder.listFiles();
		//Initialisation de la liste de résultat
		List<String> listFileNames = new LinkedList<String>();
		//Parcour de tout les postings
		for(Posting posting: postings)
			//Récupération du Nom du fichier grâce au docId
			listFileNames.add(fileText[posting.docId].getName());
		//Retourn la liste des noms de fichier corespondant au posting (docId)
		return listFileNames;
	}
	
	/**
	 * Tester le traitement des requêtes
	 */
	private static void testQuery()
	{ 
		System.out.println("Loading the index");
		long debut = System.currentTimeMillis();
		//Chargement de l'index
		Index index = Index.read(INDEX_DATA);
		System.out.println("Index loaded, duration= " + (System.currentTimeMillis() - debut) + " ms\n" );
		//Initialisation de l'objet permettant de faire les requetes sur l'index
		AndQueryEngine andQueryEngine = new AndQueryEngine(index);
		
		//Requête project
		String query = " Project which was created for the Web"; //project 
		System.out.println("Processing request : " + query );
		List<Posting> listPosting = andQueryEngine.processQuery(query);
		System.out.println("Result: " + listPosting.size()  +" document(s)");
		System.out.print("[");
		for(Posting posting : listPosting)
			System.out.print(posting.toString());
		System.out.println("]");
		System.out.println("Files :");
		System.out.println(getFileNames(listPosting) + "\n");
		
		//Requête project SOFTWARE
		/*query = "project SOFTWARE"; 
		System.out.println("Processing request : " + query );
		listPosting = andQueryEngine.processQuery(query);
		System.out.println("Result: " + listPosting.size()  +" document(s)");
		System.out.print("[");
		for(Posting posting : listPosting)
			System.out.print(posting.toString());
		System.out.println("]");
		System.out.println("Files :");
		System.out.println(getFileNames(listPosting) + "\n");*/
		
		//Requête project SOFTWARE Web
		/*query = "project SOFTWARE Web"; 
		System.out.println("Processing request : " + query );
		listPosting = andQueryEngine.processQuery(query);
		System.out.println("Result: " + listPosting.size()  +" document(s)");
		System.out.print("[");
		for(Posting posting : listPosting)
			System.out.print(posting.toString());
		System.out.println("]");
		System.out.println("Files :");
		System.out.println(getFileNames(listPosting) + "\n");*/
	}
	
	public static void main(String[] args)
	{
		//testIndexation();
		testQuery();
	}
}
