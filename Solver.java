package de.mainaim.scrabblesolver;
import java.util.Set;




public class Solver {
	
	public static void main(String[] args) {

		String letters = "STYXWEN";
		
		Database db = new Database();
		Set<String> matches = db.findMatches(letters);
		System.out.println(matches.toString());
	
	}
	

}
