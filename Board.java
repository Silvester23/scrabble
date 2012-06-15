package de.mainaim.scrabblesolver;

public class Board {
	private Field[][] mBoard;
	private int mSize = 15;
	private boolean mEmpty = true;
	
	public Board() {
		mBoard = new Field[mSize][mSize];
		for(int row = 0; row < mBoard.length; row++) {
			for(int col = 0; col < mBoard[row].length; col++) {
				if(row == 7 && col == 7) {
					mBoard[row][col] = new Field(FieldType.START);
				}  else if(row % 7 == 0 && col % 7 == 0) {
					mBoard[row][col] = new Field(FieldType.TRIPLEWORD);
				} else if((row == 0 && (col == 3 || col == 11))) {
					mBoard[row][col] = new Field(FieldType.DOUBLECHAR);
				} else if((row == col || row + col == 14) && (row <= 4 || row >= 10) && (col <= 4 || col >= 10)) {
					mBoard[row][col] = new Field(FieldType.DOUBLEWORD);
				} else if(row % 4 == 1 && col % 4 == 1) {
					mBoard[row][col] = new Field(FieldType.TRIPLECHAR);
				} else if(((row == 6 || row == 8) && (col == 2 || col == 6 || col == 8 || col == 12))
						 ||(col == 6 || col == 8) && (row == 2 || row == 6 || row == 8 || row == 12)
						 ||(row == 7 && (col % 4 == 3))
						 ||(col == 7 && (row % 4 == 3))) {
					mBoard[row][col] = new Field(FieldType.DOUBLECHAR);
				}

				else {
					mBoard[row][col] = new Field();
				}
			}
		}
	}
	
	public void setTile(int row, int col, Tile tile) {
		if(mBoard[row][col].getTile() == null) {
			mBoard[row][col].setTile(tile);
			
			
			// Multipliers can only be used once
			mBoard[row][col].setType(FieldType.STANDARD);
			
			
			if(mEmpty == true) {
				mEmpty = false;
			}
		} 
	}
	
	public void setTile(int row, int col, char character) {
		if(mBoard[row][col].getTile() == null) {
			mBoard[row][col].setTile(character);
			if(mEmpty == true) {
				mEmpty = false;
			}
		} 
	}
	
	public boolean isEmpty() {
		return mEmpty;
	}
	
	public Field getField(int row, int col) {
		if(row < 0 || col < 0 || row >= mSize || col >= mSize) {
			return null;
		}
		return mBoard[row][col];
	}
	
	public int getSize() {
		return mSize;
	}
	
	@Override
	public String toString() {
		String output = "   Col No.:  ";
		for(int col = 0; col < mBoard.length; col++) {
			output += String.format(" %02d  ",col);
		}
		output += "\n";
		for(int row = 0; row < mBoard.length; row++) {
			output += String.format("Row No. %02d: [",row);
			for(int col = 0; col < mBoard[row].length; col++) {
				output += mBoard[row][col].toString();
			}
			output += "]\n";
		}
		return output;
	}
}
