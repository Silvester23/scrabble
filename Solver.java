package de.mainaim.scrabblesolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;




public class Solver {
	
	public static void main(String[] args) {
		long starttime;

		String letters = "sexytes";
		
		starttime = System.nanoTime();
		Database db = new Database();
		System.out.println("Db init: " + Long.toString((System.nanoTime() - starttime)/1000000));
		Board board = new Board();
		
		
		board.setTile(7,9,'n');
		board.setTile(7,8,'o');
		board.setTile(7,7,'f');
		board.setTile(7,6,'e');
		board.setTile(7,5,'l');
		board.setTile(7,4,'e');
		board.setTile(7,3,'t');
		
		board.setTile(5,5,'h');
		board.setTile(6,5,'a');
		board.setTile(8,5,'l');
		board.setTile(9,5,'o');
		
		
		starttime = System.nanoTime();
		findMoves(db, board, letters);
		System.out.println("Finding Moves: " + Long.toString((System.nanoTime() - starttime)));
		System.out.println(board.toString());
	
	}
	
	
	public static void findMoves(Database db, Board board, String letters) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		// Behaviour for empty Board
		if(board.isEmpty()) {
			Set<String> matches = db.findWords(letters);
			System.out.println(matches);
			String[] matchArray = matches.toArray(new String[0]);
			for(int i = 0; i < matchArray.length; i++) {
				String word = matchArray[i];				
				for(int j = 0; j < word.length(); j++) {
					moves.add(new Move(matchArray[i],7,7-j,true,board));
					moves.add(new Move(matchArray[i],7-j,7,false,board));
				}
				
			}
			Collections.sort(moves);
			System.out.println(moves.toString());
		}
		
		// Behaviour for non-empty Board
		else {
			Set<int[]> indices = new HashSet<int[]>();
			for(int row = 0; row < board.getSize(); row++) {
				for(int col = 0; col < board.getSize(); col++) {
					if(board.getField(row,col).getTile() == null) {
						if(board.getField(row,col-1) != null && board.getField(row,col-1).getTile() != null) {
							indices.add(new int[]{row,col});
						}
						
						if(board.getField(row,col+1) != null && board.getField(row,col+1).getTile() != null) {
							indices.add(new int[]{row,col});
						}
						
						if(board.getField(row-1,col) != null && board.getField(row-1,col).getTile() != null) {
							indices.add(new int[]{row,col});
						}
						
						if(board.getField(row+1,col) != null && board.getField(row+1,col).getTile() != null) {
							indices.add(new int[]{row,col});
						}
					}
				}
			}
			
			ArrayList<Move> movesAtPosition = new ArrayList<Move>();
			Iterator<int[]> ite = indices.iterator();
			while(ite.hasNext()) {
				int[] cur = ite.next();
				movesAtPosition = getMovesAtPosition(cur, board);
			}
			System.out.println(movesAtPosition);
		}
		
		
	}
	

	public static ArrayList<Move> getMovesAtPosition(int[] position, Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		
		return moves;
	}
}
