package de.mainaim.scrabblesolver;

import java.util.ArrayList;


public class Move {
	private ArrayList<Word> mWords = new ArrayList<Word>();
	private int mPoints;
	private boolean mValid;
	private int mRow;
	private int mCol;
	private String mLetters;
	private Board mBoard;
	private boolean mHorizontal;
	
	
	public Move(String letters, int row, int col, boolean horizontal, Board board) {
		mRow = row;
		mCol = col;
		mLetters = letters;
		mBoard = board;
		mHorizontal = horizontal;
		//mWords.add(new Word(letters,row,col,horizontal,board));
		
		// Get main word
		String word = getFullWord(letters,row,col,horizontal);
		System.out.println(word);
		
		for(int i = 0; i < letters.length(); i++) {
			if(horizontal) {
				// Check for vertical neighbours;
				System.out.println("" + i + ", " + (row+1) + "," + (col+i));
				System.out.println(board.getField(row+1,col+i));
				if(board.getField(row+1,col+i).getTile() != null) {
					System.out.println("Lower neighbour at: " + row + "," + (col+i));
				}
				if(board.getField(row-1,col+i).getTile() != null) {
					System.out.println("Upper neighbour at: " + row + "," + (col+i));
				}
			}
		}
	}
	
	
	public String getFullWord(String letters, int row, int col, boolean horizontal) {
		String word = "";
		
		if(horizontal) {
			if(col > 0) {
				// Get all letters before new letters
				
				Field prevHor;
				int i = 1;
				while((prevHor = mBoard.getField(row,col-i)) != null) {
					Tile prevTile = prevHor.getTile();
					if(prevTile == null)
						break;
					
					i++;
					word = prevTile.mChar + word; 
				}
			}
			
			// Get letters between new letters
			
			for(int i = 0; i < letters.length(); i++) {
				if(letters.charAt(i) == ' ') {
					System.out.println("Leerzeichen bei " + i);
					letters = letters.substring(0,i) + mBoard.getField(row,col+i).getTile().mChar + letters.substring(i+1,letters.length());
				}
			}
				
			word += letters;
			
			if(col + letters.length() < mBoard.getSize() -1) {
				// Get all letters after new letters
				Field nextHor;
				int i = 0;
				while((nextHor = mBoard.getField(row,col + letters.length() +i)) != null) {
					Tile nextTile = nextHor.getTile();
					if(nextTile == null)
						break;
					
					i++;
					word += nextTile.mChar; 
				}
			}
		} else {
			// horizontal == false
			
			if(row > 0) {
				// Get all letters before new letters
				
			}
		}
		
		return word;
	}
}
