package de.mainaim.scrabblesolver;

public class Word implements Comparable<Word> {
	private boolean mHorizontal;
	private int mRow;
	private int mCol;
	private String mLetters;
	private int mPoints;	
	private Field[] mFields;
	private boolean mValid;
	private boolean mValidCheck = false;
	
	
	public Word(String letters, int row, int col, boolean horizontal, Board board) {
		mRow = row;
		mCol = col;
		mLetters = letters;
		mHorizontal = horizontal;
		mFields = new Field[letters.length()];
		for(int i = 0; i < mFields.length; i++) {
			int curRow = row;
			int curCol = col;
			if(horizontal) {
				curCol += i;
			} else {
				curRow += i;
			}
			
			mFields[i] = new Field(board.getField(curRow,curCol));
			if(board.getField(curRow,curCol).getTile() == null) {
					mFields[i].setTile(mLetters.charAt(i));
			} 
		}
		mPoints = calcPoints();
	}
	
	public int getPoints() {
		return mPoints;
	}
	
	public boolean isValid(Database db) {
		if(mValidCheck == true) {
			return mValid;
		}
		
		mValid = db.doQuery(mLetters);
		mValidCheck = true;
		return mValid;
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
	public int compareTo(Word move) {
		if(move.getPoints() > mPoints) {
			return 1;
		} else if(move.getPoints() < mPoints) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
