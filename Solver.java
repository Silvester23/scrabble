package de.mainaim.scrabblesolver;
import java.util.Set;




public class Solver {
	
	public static void main(String[] args) {

		String letters = "STYXWE?";
		
		Database db = new Database();
		Set<String> matches = db.findMatches(letters);
		//System.out.println(matches.toString());
		Board board = new Board();
		System.out.println(board.toString());
	
	}
	

}
