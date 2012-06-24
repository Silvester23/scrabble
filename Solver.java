package de.mainaim.scrabblesolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;




public class Solver {
	static Database mDb;
	static Board mBoard;
	
	public static void main(String[] args) {
		
		long starttime;

		String letters = "wzggmtu";
		
		starttime = System.nanoTime();
		mDb = new Database();
		mBoard = new Board();
		System.out.println("Db init: " + Long.toString((System.nanoTime() - starttime)/1000000));
		
		mBoard.setWord(0,7,"jet", false);
		mBoard.setWord(1,6,"jet", false);
		mBoard.setWord(1,4,"lacks", false);
		mBoard.setWord(1,11,"kid", false);
		mBoard.setWord(3,10,"im", false);
		mBoard.setWord(5,6,"böser", false);
		mBoard.setWord(9,4,"vase", false);
		mBoard.setWord(11,1,"qis", false);
		mBoard.setWord(4,9,"hüfen", false);
		mBoard.setWord(7,10,"noten", false);
		mBoard.setWord(11,6,"er", false);
		
		mBoard.setWord(3,2,"nacht", true);
		mBoard.setWord(5,4,"sub", true);
		mBoard.setWord(6,6,"öl", true);
		mBoard.setWord(7,6,"suren", true);
		mBoard.setWord(8,6,"es", true);
		mBoard.setWord(9,4,"vor", true);
		mBoard.setWord(11,1,"questen", true);
		mBoard.setWord(12,0,"zimte", true);
		
		

		starttime = System.nanoTime();
		findMoves(letters);
		System.out.println("Finding Moves: " + ((System.nanoTime() - starttime)/1000000));
		
		System.out.println(mBoard);
		

		
		
	}
	
	
	public static void findMoves(String letters) {
		Set<Move> moves = new HashSet<Move>();
		
		// Behaviour for empty Board
		if(mBoard.isEmpty()) {
			/*
			 * TODO
			*/
		}
		
		// Behaviour for non-empty Board
		else {
			// Find all potential positions
			int[][][] minLengths = mBoard.getMinWordLengths();
			for(int row = 0; row < mBoard.getSize(); row++) {
				for(int col = 0; col < mBoard.getSize(); col++) {
					if(minLengths[row][col][0] > 0) {
						moves.addAll(getMovesAtPosition(letters,row,col,true,minLengths[row][col][0]));
					}
					if(minLengths[row][col][1] > 0) {
						moves.addAll(getMovesAtPosition(letters, row,col,false,minLengths[row][col][1]));
					}
				}
			}
		}
		ArrayList<Move> movesList = new ArrayList<Move>(moves);
		Collections.sort(movesList);
		System.out.println(movesList);
	}
	
	public static Set<Move> getMovesAtPosition(String letters, int row, int col, boolean horizontal, int minLength) {
		Set<Move> moves = new HashSet<Move>();
		
		moves.addAll(getMovesAtPosition(letters,"",row,col,horizontal,minLength));

		return moves;
	}
	
	public static Set<Move> getMovesAtPosition(String letters, String prefix, int row, int col, boolean horizontal, int minLength) {
		Set<Move> moves = new HashSet<Move>();
		int n = letters.length();
		String curWord;
		String suffix;
		String contdQuery;
		String finalQuery;
		if(horizontal) {
			suffix = mBoard.getSuffix(row,col+minLength,horizontal);
			if(col + prefix.length() >= mBoard.getSize()) {
				return moves;
			}
		} else {
			suffix = mBoard.getSuffix(row+minLength,col,horizontal);
			if(row + prefix.length() >= mBoard.getSize()) {
				return moves;
			}
		}
		
		
		for(int i = 0; i < n; i++) {
			curWord = prefix + letters.charAt(i);
			
			if(curWord.length() < minLength) {
				contdQuery = curWord + "*";
				if(suffix.length() > 0) {
					contdQuery += suffix + "*";
				}
				if(mDb.doQuery(contdQuery)) {
					moves.addAll(getMovesAtPosition(letters.substring(0,i) + letters.substring(i+1,n), curWord, row, col, horizontal, minLength));
				} 				
			} else {
				Word fullWord;
				if(curWord.length() == minLength) {
					fullWord = mBoard.getFullWord(curWord + suffix,row,col,horizontal);
					curWord = fullWord.getLetters();
				} else {
					fullWord = mBoard.getFullWord(curWord,row,col,horizontal);
					curWord = fullWord.getLetters();
				}
				finalQuery = curWord;
				contdQuery = finalQuery + "*";
				
				if(mDb.doQuery(finalQuery)) {
					
					Move move = new Move(curWord,fullWord.getRow(),fullWord.getCol(),horizontal, mBoard);
					if(move.isValid(mDb)) {
						moves.add(move);
					}
				}
				
				if(mDb.doQuery(contdQuery)) {
					moves.addAll(getMovesAtPosition(letters.substring(0,i) + letters.substring(i+1,n), curWord, row, col, horizontal, minLength));
				}
				
			}
		}
		return moves;
	}
}
	

	
