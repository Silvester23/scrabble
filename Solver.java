package de.mainaim.scrabblesolver;
import java.util.Set;




public class Solver {
	
	public static void main(String[] args) {

		String letters = "NERTDEL";
		
		Database db = new Database();		
		Board board = new Board();
		
		findMoves(db, board, letters);
		
		System.out.println(board.toString());
	
	}
	
	
	public static void findMoves(Database db, Board board, String letters) {
		if(board.isEmpty()) {
			Set<String> matches = db.findWords(letters);
			System.out.println(matches);
			String[] a = matches.toArray(new String[0]);
			
			Move move = new Move(matches.toArray(new String[0])[0],0,0,true,board);
			System.out.println(Integer.toString(move.getPoints()));
		}
		
		
		
	}
	

}
