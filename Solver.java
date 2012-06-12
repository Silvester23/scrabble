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
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.skjegstad.utils.BloomFilter;



public class Solver {
	
	public static void main(String[] args) {

		String letters = "STYXWEN";
		
		Database db = new Database();
		Set<String> matches = db.findMatches(letters);
		System.out.println(matches.toString());
	
	}
	
	
	
	
	
/*	
public static BloomFilter<String> createBloomFilter() {
		
		double falsePositiveProbability = 0.1;
		int expectedSize = 327314;
		BloomFilter<String> bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
		
		try {
			FileInputStream fis = new FileInputStream("C:/Users/Jan/Documents/projekte/Java/Scrabble Solver/res/dict.txt");
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = br.readLine()) != null) {
				bloomFilter.add(line);
			}
		} catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
		}
		return bloomFilter;
	}
*/	

}
