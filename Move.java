package de.mainaim.scrabblesolver;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public final class Move implements Comparable<Move> {
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
		String newLetters = "";
		
		
		// Get main word
		Word mainWord = board.getFullWord(letters,row,col,horizontal);
		String wordstring = mainWord.getLetters();
		if(wordstring.length() > 1) {
			mWords.add(mainWord);
		}
		for(int i = 0; i < letters.length(); i++) {
			if(letters.charAt(i) != ' ') {
				if(horizontal) {
					if(board.getField(row,col+i).getTile() != null) {
						continue;
					}
					// Check for vertical neighbours if field is empty
					if((board.getField(row+1,col+i) != null && board.getField(row+1,col+i).getTile() != null) || (board.getField(row-1,col+i) != null && board.getField(row-1,col+i).getTile() != null)) {
						Word vertWord = board.getFullWord(Character.toString(letters.charAt(i)),row,col+i,false);
						if(vertWord.getLetters().length() > 1) {
							mWords.add(vertWord);
						}
					}
				} else {
					if(board.getField(row+i,col).getTile() != null) {
						continue;
					}
					//Check for horizontal neighbours
					if((board.getField(row+i,col+1) != null && board.getField(row+i,col+1).getTile() != null) || (board.getField(row+i,col-1) != null && board.getField(row+i,col-1).getTile() != null)) {
						Word horWord  = board.getFullWord(Character.toString(letters.charAt(i)),row+i,col,true);
						if(horWord.getLetters().length() > 1) {
							mWords.add(horWord);
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
	
	public int getRow() {
		return mRow;
	}
	
	public int getCol() {
		return mCol;
	}
	
	public String getLetters() {
		return mLetters;
	}
	
	public ArrayList<Word> getWords() {
		return mWords;
	}
	
	
	public boolean isValid(Database db) {
		
		if(mValidCheck == true) {				
			return mValid;
		}
		

		
		if(mWords.size() > 0) {
			mValid = true;
			Iterator<Word> iter = mWords.iterator();
			while(iter.hasNext()) {
				Word word = iter.next();				
				if(word.isValid(db) == false) {
					mValid = false;
				}
			}
		} 
		
		mValidCheck = true;
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
			word += mBoard.fillGaps(letters, row, col, horizontal);
			
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
			try {
				word += mBoard.fillGaps(letters, row, col, horizontal);
			} catch(NullPointerException npe) {
				System.out.println("Caught NPE with '" + letters + "' at " + row + ", " + col + ". Horizontal: " + horizontal);
			}
						
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
		output += "Letters: \"" + mLetters + "\" ";
		output += "Pts: " + getPoints() + ", ";
		output += "Pos: " + mRow + "," + mCol + ", ";
		output += "Hor:" + mHorizontal + ", ";
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
	
	@Override
	public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(mPoints).
            append(mLetters).
            append(mRow).
            append(mCol).
            toHashCode();
    }
	
	@Override
	public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;

        Move rhs = (Move) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
        		append(mPoints, rhs.getPoints()).
                append(mLetters, rhs.getLetters()).
                append(mRow, rhs.getRow()).
                append(mCol, rhs.getCol()).
                isEquals();
    }


}
