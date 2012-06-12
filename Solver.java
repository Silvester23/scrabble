package de.mainaim.scrabblesolver;
import java.util.Set;




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
