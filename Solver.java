package de.mainaim.scrabblesolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;




public class Solver {
	
	public static void main(String[] args) {
		long starttime;

		String letters = "ybxrune";
		
		starttime = System.nanoTime();
		Database db = new Database();
		System.out.println("Db init: " + Long.toString((System.nanoTime() - starttime)/1000000));
		Board board = new Board();
		
		
		
		
		
		board.setTile(5,5,'k');
		board.setTile(6,5,'e');
		board.setTile(7,5,'i');
		board.setTile(8,5,'n');

		
		/*
		board.setTile(8,8,'d');
		
		board.setTile(8,9,'s');
		board.setTile(9,9,'o');
		*/
		
		
		//Move move = new Move("s",10,5,true,board);
		//[{Word: n, Row: 8, Col: 7, Dir: Hor, Points : 1}, {Word: chefn, Row: 4, Col: 7, Dir: Ver, Points : 12}]
		
		starttime = System.nanoTime();
		findMoves(db, board, letters);
		System.out.println("Finding Moves: " + Long.toString((System.nanoTime() - starttime)/100000));
		
		System.out.println(board.toString());
		
		ArrayList<Move> test = new ArrayList<Move>();
		test.add(null);
		test.remove(null);
		System.out.println(test);
		
	}
	
	
	public static void findMoves(Database db, Board board, String letters) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		// Behaviour for empty Board
		if(board.isEmpty()) {
			/*
			Set<String> matches = db.findWords(letters);
			System.out.println(matches);
			String[] matchArray = matches.toArray(new String[0]);
			for(int i = 0; i < matchArray.length; i++) {
				String word = matchArray[i];				
				for(int j = 0; j < word.length(); j++) {
					moves.add(new Word(matchArray[i],7,7-j,true,board));
					moves.add(new Word(matchArray[i],7-j,7,false,board));
				}
				
			}
			Collections.sort(moves);
			System.out.println(moves.toString());
			*/
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
				int[] curPosition = ite.next();
				movesAtPosition = getMovesAtPosition(letters,curPosition, board, db);
				moves.addAll(movesAtPosition);
			}
			Collections.sort(moves);
			System.out.println(moves);
		}
		
		
	}
	

	public static ArrayList<Move> getMovesAtPosition(String letters, int[] position, Board board, Database db) {
		int row = position[0];
		int col = position[1];
		ArrayList<Move> moves = new ArrayList<Move>();
		
		moves.addAll(getMovesAtPosition(letters, "", position, true, false, board, db));
		moves.addAll(getMovesAtPosition(letters, "", position, false, false, board, db));
		
		Collections.sort(moves);
		return moves;
	}
	
	public static ArrayList<Move> getMovesAtPosition(String letters, String substring, int[] position, boolean horizontal, boolean reverse, Board board, Database db) {
		int row = position[0];
		int col = position[1];
		ArrayList<Move> moves = new ArrayList<Move>();
		int n = letters.length();
		
		if(horizontal) {
			for(int i = 0; i < n; i++) {
				String newLetters = substring + letters.charAt(i);
				
				
				if(db.doQuery(newLetters + "*")) {
					Move move = new Move(newLetters,row,col,horizontal,board);
					
					if(move.isValid(db)) {
						moves.add(move);
					}
					newLetters = addSpacesToSubstring(newLetters, position, horizontal, reverse, board);
									
					
					moves.addAll(getMovesAtPosition(letters.substring(0,i) + letters.substring(i+1,n), newLetters, position, horizontal, reverse, board, db));
				}				
				
			}
		}
		
		return moves;
	}
	
	
	public static String addSpacesToSubstring(String substring, int[] position, boolean horizontal, boolean reverse, Board board) {
		int row = position[0];
		int col = position[1];
		int n = substring.length();
		if(horizontal) {
			if(col < board.getSize() -1) {
				// Add spaces for tiles after substring
				Field nextHor;
				int i = 0;
				while((nextHor = board.getField(row,col + n +i)) != null) {
					Tile nextTile = nextHor.getTile();
					if(nextTile == null)
						break;
					i++;
					substring += " "; 
				}
			}
		}
		
		return substring;
	}
}
