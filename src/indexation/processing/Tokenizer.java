package indexation.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import indexation.content.Token;

public class Tokenizer implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String regex = "[^\\pL\\pN]";
	
	 public List<String> tokenizeString(String string)
	 {
		 //La liste retournée		 
		 List<String> list = new ArrayList<String>();
		 //Récupération de chaque mots
		 for(String mot : string.split(regex))		
			 list.add(mot);
		 return list;
	 }
	 
	 private void tokenizeDocument(File document, int docId, List<Token> tokens)
	 {
		 try
		 {
			 FileInputStream fis = new FileInputStream(document);
			 InputStreamReader isr = new InputStreamReader(fis);
			 Scanner scanner = new Scanner(isr);
			 while(scanner.hasNextLine())
				{ 
					String line = scanner.nextLine();
					List<String> list = tokenizeString(line);
					for(String type : list)
					{
						if(!type.isEmpty())
							tokens.add(new Token(type,docId));
					}
				}
				scanner.close();
		 }
		 catch(FileNotFoundException e)
		 {
				e.printStackTrace();	 
		 }
	 }
	 
	 public List<Token> tokenizeCorpus(String folder)
	 {
		 ArrayList<Token> list = new ArrayList<Token>();
		 
		 File file = new File(folder);
		 
		 String [] files = file.list();
		 Arrays.sort(files);	
		 
		 for(String nameFile : files )
		 {
			 File document = new File(folder + File.separator + nameFile);
			 tokenizeDocument(document, 0, list);
		 }	
		 return list;
	}
	 
	 //Pour tester avec corpus2
	 /*
	 public static void main(String[] args)
	 {
		 Tokenizer tokenizer = new Tokenizer();
		 List<Token> test = tokenizer.tokenizeCorpus("../Common/corpus2");
		 System.out.println("----------------- Début du test ->");
		 for(Token token : test)
			 System.out.println(token.toString());
	 }
	 */
}
