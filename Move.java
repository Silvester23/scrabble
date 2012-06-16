package de.mainaim.scrabblesolver;

import java.util.ArrayList;
import java.util.Iterator;


public class Move implements Comparable<Move> {
	private ArrayList<Word> mWords = new ArrayList<Word>();
	private int mPoints = -1;
	private boolean mValid;
	private boolean mValidCheck;
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
		
		// Get main word
		String wordstring = getFullWord(letters,row,col,horizontal);
		if(wordstring.length() > 1) {
			mWords.add(new Word(wordstring,mRow,mCol,horizontal,board));
			
			// Reset positions
			mRow = row;
			mCol = col;
		}
		for(int i = 0; i < letters.length(); i++) {
			if(letters.charAt(i) != ' ') {
				if(horizontal) {
					// Check for vertical neighbours
					if(board.getField(row+1,col+i).getTile() != null || board.getField(row-1,col+i).getTile() != null) {
						String vertWord  = getFullWord(Character.toString(letters.charAt(i)),row,col+i,false);
						if(vertWord.length() > 1) {
							mWords.add(new Word(vertWord,mRow,mCol+i,false,board));
						}
					}
				} else {
					//Check for horizontal neighbours
					if(board.getField(row+i,col+1).getTile() != null || board.getField(row+i,col-1).getTile() != null) {
						String horWord  = getFullWord(Character.toString(letters.charAt(i)),row+i,col,true);
						if(horWord.length() > 1) {
							mWords.add(new Word(horWord,mRow+i,mCol,true,board));
						}
					}
				}
			}
		}
	}
	
	public int getPoints() {
		if(mPoints != -1) {
			return mPoints;
		}
		mPoints = 0;
		Iterator<Word> iter = mWords.iterator();
		while(iter.hasNext()) {
			Word word = iter.next();
			mPoints += word.getPoints();
		}		
		
		return mPoints;
	}
	
	public boolean isValid(Database db) {
		
		if(mValidCheck == true) {				
			return mValid;
		}
		
		mValid = true;
		
		Iterator<Word> iter = mWords.iterator();
		while(iter.hasNext()) {
			Word word = iter.next();
			if(word.isValid(db) == false) {
				mValid = false;
			}
		}	
		
		return mValid;
	}
	
	private String getFullWord(String letters, int row, int col, boolean horizontal) {
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
					
					//Adjust horizontal starting position for move
					mCol--;
				}
			}
			
			// Get letters between new letters
			
			for(int i = 0; i < letters.length(); i++) {
				if(letters.charAt(i) == ' ') {
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
				Field prevVer;
				int i = 1;
				while((prevVer = mBoard.getField(row-i,col)) != null) {
					Tile prevTile = prevVer.getTile();
					if(prevTile == null)
						break;
					
					i++;
					word = prevTile.mChar + word;
					
					//Adjust vertical starting position for move
					mRow--;
				}	
			}
			
			// Get letters between new letters
			
			for(int i = 0; i < letters.length(); i++) {
				if(letters.charAt(i) == ' ') {
					letters = letters.substring(0,i) + mBoard.getField(row+i,col).getTile().mChar + letters.substring(i+1,letters.length());
				}
			}
							
			word += letters;
						
			if(row + letters.length() < mBoard.getSize() -1) {
				// Get all letters after new letters
				Field nextVer;
				int i = 0;
				while((nextVer = mBoard.getField(row + letters.length() +i,col)) != null) {
					Tile nextTile = nextVer.getTile();
					if(nextTile == null)
						break;
					
					i++;
					word += nextTile.mChar; 
				}
			}
			
		}
		
		return word;
	}
	
	@Override
	public String toString() {
		String output = "[";
		output += "Points : " + getPoints() + ", ";
		output += "Words: " + mWords.toString() + "]";
		
		return output;
	}

	@Override
	public int compareTo(Move move) {
		if(move.getPoints() > getPoints()) {
			return 1;
		} else if(move.getPoints() < getPoints()) {
			return -1;
		} else {
			return 0;
		}
	}
}
