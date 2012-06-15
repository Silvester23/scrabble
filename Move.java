package de.mainaim.scrabblesolver;

public class Move implements Comparable<Move> {
	private boolean mHorizontal;
	private int mRow;
	private int mCol;
	private String mLetters;
	private int mPoints;	
	private Field[] mFields;
	private boolean mValidStart = false;
	
	
	public Move(String letters, int row, int col, boolean horizontal, Board board) {
		mRow = row;
		mCol = col;
		mLetters = letters;
		mHorizontal = horizontal;
		mFields = new Field[letters.length()];
		for(int i = 0; i < mFields.length; i++) {
			if(horizontal) {
				mFields[i] = new Field(board.getField(row,col+i));
				mFields[i].setTile(mLetters.charAt(i));
			} else {
				mFields[i] = new Field(board.getField(row+i,col));
				mFields[i].setTile(mLetters.charAt(i));
			}
		}
		mPoints = calcPoints();
	}
	
	public int getPoints() {
		return mPoints;
	}	
	
	private int calcPoints() {
		int points = 0;
		int wordmultiplier = 1;
		for(Field field : mFields) {
			int charValue = field.getTile().mValue;
			switch(field.getType()) {
				case DOUBLECHAR:
					charValue *= 2;
					break;
				case TRIPLECHAR:
					charValue *= 3;
					break;
				case DOUBLEWORD:
					wordmultiplier = 2;
					break;
				case TRIPLEWORD:
					wordmultiplier = 3;
					break;
				case START:
					mValidStart = true;
					break;
					
			}
			points += charValue;
		}
		points *= wordmultiplier;
		return points;
	}
	
	@Override
	public String toString() {
		String output = "{";
		output += "Word: " + mLetters + ", ";
		output += "Row: " + mRow + ", ";
		output += "Col: " + mCol + ", ";
		if(mHorizontal) {
			output += "Dir: Hor, ";
		} else {
			output += "Dir: Ver, ";
		}
		output += "Points : " + mPoints + "}";
		
		return output;
	}

	@Override
	public int compareTo(Move move) {
		if(move.getPoints() > mPoints) {
			return 1;
		} else if(move.getPoints() < mPoints) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
