package de.mainaim.scrabblesolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;




public class Solver {
	
	public static void main(String[] args) {
		long starttime;

		String letters = "sexyten";
		
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
		
		
		Move move = new Move(" allo",5,5,true,board);
		
		/*
		starttime = System.nanoTime();
		findMoves(db, board, letters);
		System.out.println("Finding Moves: " + Long.toString((System.nanoTime() - starttime)/100000));
		*/
		System.out.println(board.toString());
		
	}
	
	
	public static void findMoves(Database db, Board board, String letters) {
		ArrayList<Word> moves = new ArrayList<Word>();
		
		// Behaviour for empty Board
		if(board.isEmpty()) {
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
			
			ArrayList<Word> movesAtPosition = new ArrayList<Word>();
			Iterator<int[]> ite = indices.iterator();
			while(ite.hasNext()) {
				int[] curPosition = ite.next();
				movesAtPosition = getMovesAtPosition(db, curPosition, board, letters);
			}
			System.out.println(movesAtPosition);
		}
		
		
	}
	

	public static ArrayList<Word> getMovesAtPosition(Database db, int[] position, Board board, String letters) {
		int row = position[0];
		int col = position[1];
		ArrayList<Word> moves = new ArrayList<Word>();
		for(int i = 0; i < letters.length(); i++) {
			char newChar = letters.charAt(i);
			
			
			// Set-Direction: Up
			if(row > 0 && row < board.getSize() -1) {
				// Adjacent lower tile?
				if(board.getField(row+1,col).getTile() != null) {
					// Get current lower word
					String curWord = "";
					curWord += newChar;
					int j = 1;
					Field nextVert = board.getField(row+j,col);
					while(nextVert != null && nextVert.getTile() != null) {
						j++;
						curWord += nextVert.getTile().mChar;
						nextVert = board.getField(row+j,col);
					}
					String finalQuery = curWord;
					String contdQuery = curWord + "*";
					
					//If placing the new char is valid vertically, check for horizontal neighbours
					if(db.doQuery(finalQuery)) {
						Word verMove = new Word(curWord,row,col,false,board);
						String horWord = "";
						Field prevHor;
						j = 1;
						while((prevHor = board.getField(row,col-j)) != null) {
							if(prevHor.getTile() == null)
								break;
							horWord = prevHor.getTile().mChar + horWord;
							j++;
						}
						horWord += newChar;
						
						Field nextHor;
						j = 1;
						while((nextHor = board.getField(row,col+j)) != null) {
							if(nextHor.getTile() == null)
								break;
							horWord += nextHor.getTile().mChar;
							j++;
						}
						if(horWord.length() > 1) {
							System.out.println();
							System.out.println("Hier neu in " + row + ", " + col);
							System.out.println("\""+horWord+"\"");
							System.out.println();
							if(db.doQuery(horWord)) {
								Word horMove = new Word(horWord,row,col,true,board);
								System.out.println(horMove);
								System.out.println(verMove);
							}
						} else {
							System.out.println(verMove);
							// The new character has no horizontal neighbours => placing the char is valid
						}
					}
					
					if(db.doQuery(contdQuery)) {
						System.out.println(curWord);
						System.out.println("" + row + "," + col);
					}
						
						

				}
			}
			
			if(i > 0) {
				if(board.getField(row-1,col).getTile() != null) {
					
				}
				if(board.getField(row,col-1).getTile() != null) {
					
				}
				
			}
		}
		
		
		return moves;
	}
}
