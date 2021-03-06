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
	private IndexSearcher mISearcher;
	public int mTries = 0;
	//private static Set<String> mValidWords = new HashSet<String>();
	
	public Database() {
		try {
			IndexWriter mWriter = new IndexWriter(mDir, mIwc);
			mWriter.addDocument(mDoc);
			mWriter.close();
			mISearcher = new IndexSearcher(mDir);
		} catch (IOException e) {
			System.out.println("Caught a " + e.getClass() +
				       "\n with message: " + e.getMessage());
		}
	}
	
	
	public Set<String> findWords(String letters) {
		letters = letters.toLowerCase();
		HashSet<String> validWords = new HashSet<String>();	
		long starttime = System.nanoTime();
		validWords.addAll(findWords(letters,"",mISearcher));
		System.out.println("Total search time: " + Long.toString((System.nanoTime() - starttime)/1000000));
		System.out.println("Matches: " + Integer.toString(validWords.size()));
		System.out.println("Tries: " + Integer.toString(mTries));
		mTries = 0;
		return validWords;
		
	}
	
	private Set<String> findWords(String letters, String substring, IndexSearcher isearcher) {
		HashSet<String> validWords = new HashSet<String>();
		int n = letters.length();
		for(int i = 0; i < n; i++) {
			String finalQuery = substring+letters.charAt(i);
			String contdQuery = finalQuery+"*";
			if(doQuery(finalQuery)) {
				validWords.add(substring + letters.charAt(i));
			}
			if(doQuery(contdQuery)) {
				validWords.addAll(findWords(letters.substring(0,i) + letters.substring(i+1,n), substring+letters.charAt(i) ,isearcher));
			}
		}		
		return validWords;
		//
	}
	
	
	public boolean doQuery(String queryString) {
		mTries += 1;
		
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
			return mISearcher.search(query,100).totalHits > 0;
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
				doc.add(new Field("word",line.toLowerCase(),Field.Store.YES,Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
			}
		} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}
		return doc;
	}
	
	
	// Unsuccessful attempt at getting wildcard-matches
	/*
	public Set<String> getWords(String queryString, IndexSearcher isearcher) {
		
		try {
			IndexReader reader = IndexReader.open(mDir);
			Query query = mParser.parse(queryString);
			ScoreDoc[] matches = isearcher.search(query, 100).scoreDocs;
			for(int i = 0; i < matches.length; i++) {
				int docId = matches[i].doc;
				TermFreqVector tfvector = reader.getTermFreqVector(docId, "word");
				TermPositionVector tpvector = (TermPositionVector)tfvector;
				int termidx = tfvector.indexOf(queryString);
				int[] termposx = tpvector.getTermPositions(termidx);
				System.out.println(Integer.toString(termidx));
				TermVectorOffsetInfo[] tvoffsetinfo = tpvector.getOffsets(termidx);
				//System.out.println(Integer.toString(termposx.length));
				for (int j=0;j<termposx.length;j++) {  
	                System.out.println("termpos : "+termposx[j]);  
	            }  
	            
				for (int j=0;j<tvoffsetinfo.length;j++) {  
	                int offsetStart = tvoffsetinfo[j].getStartOffset();  
	                int offsetEnd = tvoffsetinfo[j].getEndOffset();  
	                System.out.println("offsets : "+offsetStart+" "+offsetEnd);  
	            }
			}
		} catch (IOException e) {
			System.out.println("Caught a " + e.getClass() +
				       "\n with message: " + e.getMessage());
		} catch (ParseException e) {
			System.out.println("Caught a " + e.getClass() +
				       "\n with message: " + e.getMessage());
		}
		

		return new HashSet<String>();
	}
	*/
	

}
