package de.mainaim.scrabblesolver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Database {
	private Analyzer mAnalyzer = new StandardAnalyzer(Version.LUCENE_36);
	private IndexWriterConfig mIwc = new IndexWriterConfig(Version.LUCENE_36, mAnalyzer);
	private Directory mDir = new RAMDirectory();
	private Document mDoc = createDocument();
	private QueryParser mParser = new QueryParser(Version.LUCENE_36, "word", mAnalyzer);
	//private static Set<String> mValidWords = new HashSet<String>();
	
	public Set<String> findMatches(String letters) {
		letters = letters.toLowerCase();
		HashSet<String> validWords = new HashSet<String>();
		
		try {
			IndexWriter mWriter = new IndexWriter(mDir, mIwc);
			mWriter.addDocument(mDoc);
			mWriter.close();
			IndexSearcher isearcher = new IndexSearcher(mDir);
			
			validWords.addAll(findMatches(letters,"",isearcher, letters.length()));
			
		} catch (IOException e) {
			System.out.println("Caught a " + e.getClass() +
				       "\n with message: " + e.getMessage());
		}

		return validWords;
		
	}
	
	private Set<String> findMatches(String letters, String substring, IndexSearcher isearcher, int maxLength) {
		HashSet<String> validWords = new HashSet<String>();
		int n = letters.length();
		for(int i = 0; i < n; i++) {
			String contdQuery = "\n"+substring+letters.charAt(i)+"*";
			String finQuery = "\n"+substring+letters.charAt(i);
			if(doQuery(finQuery,isearcher)) {
				validWords.add(substring + letters.charAt(i));
			}
			if(doQuery(contdQuery,isearcher)) {
				validWords.addAll(findMatches(letters.substring(0,i) + letters.substring(i+1,n), substring+letters.charAt(i) ,isearcher, maxLength));
			}
		}		
		return validWords;
		//
	}
	
	
	
	private boolean doQuery(String queryString, IndexSearcher isearcher) {
		
		/* Debug Query
		Set<String> words = new HashSet<String>(Arrays.asList(new String[]{"ab","ac","cb","cab","bac","ba"}));
		
		Pattern pattern = Pattern.compile(queryString);
		
		for(String element : words) {
			Matcher matcher = pattern.matcher(element);
			if(matcher.find()) {
				return true;
			}
		}
		
		return false;
		
		*/
		
		//Reenable after Debugging
		try {
			Query query = mParser.parse(queryString);
			return isearcher.search(query,100).totalHits > 0;
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			return false;
		}
		
	}
	

	private Document createDocument() {
		Document doc = new Document();
		
		try {
			FileInputStream fis = new FileInputStream("C:/Users/Jan/Documents/projekte/Java/Scrabble Solver/res/dict.txt");
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line;
			while((line = br.readLine()) != null) {
				doc.add(new Field("word",line.toLowerCase(),Field.Store.YES,Field.Index.ANALYZED));
			}
		} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}
		
		return doc;
	}
	

}
