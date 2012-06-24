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
			
			
			// Multipliers can only be used once
			mBoard[row][col].setType(FieldType.STANDARD);
			
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
	
	public int[][][] getMinWordLengths() {
		int[][][] res = new int[mSize][mSize][2];
		for(int row = 0; row < mSize; row++) {
			for(int col = 0; col < mSize; col++) {
				res[row][col] = getMinWordLength(row,col);
			}
		}
		return res;
	}
	
	public int[] getMinWordLength(int row, int col) {
		int[] res = new int[2];
		int curRow = row;
		int curCol = col;
		boolean horFound = false;
		boolean verFound = false;
		
		if(getField(row,col-1) != null && getField(row,col-1).getTile() != null) {
			horFound = true;
		}
		if(getField(row-1,col) != null && getField(row-1,col).getTile() != null) {
			verFound = true;
		}
		
		for(curCol = col; curCol < mSize; curCol++) {
			 if(getField(curRow,curCol+1) != null && getField(curRow,curCol+1).getTile() == null) {
				if(((getField(curRow-1,curCol) == null)
				||  (getField(curRow-1,curCol) != null && getField(curRow-1,curCol).getTile() == null))
				&&((getField(curRow+1,curCol) == null)
				|| (getField(curRow+1,curCol) != null && getField(curRow+1,curCol).getTile() == null))) {
					res[0]++;
				} else {
					horFound = true;
					break;
				}
			} else if(getField(curRow,curCol+1) != null) {
				horFound = true;
				break;
			} else {
				if(horFound) {
					res[0] = 0;
				}
			}
		}
		
		curCol = col;
		
		for(curRow = row; curRow < mSize; curRow++) {
			if(getField(curRow+1,curCol) != null && getField(curRow+1,curCol).getTile() == null) {
				if((getField(curRow,curCol-1) == null || (getField(curRow,curCol-1) != null && getField(curRow,curCol-1).getTile() == null))
				&& (getField(curRow,curCol+1) == null || (getField(curRow,curCol+1) != null && getField(curRow,curCol+1).getTile() == null))) {
					res[1]++;
				} else {
					verFound = true;
					break;
				}
			} else if(getField(curRow+1,curCol) != null) {
				verFound = true;
				break;
			} else {
				if(verFound) {
					res[1] = 0;
				}
			}
		}
		
		
		if(!horFound || res[0] > 6) {
			res[0] = -1;
		} else {
			res[0]++;
		}
		if(!verFound || res[1] > 6) {
			res[1] = -1;
		} else {
			res[1]++;
		}
		
		if(getField(row,col).getTile() != null) {
			res[0] = -1;
			res[1] = -1;
		}		
		
		return res;
	}
	
	public String getSuffix(int row, int col, boolean horizontal) {
		String res = "";
		int n;
		int curCol = col;
		int curRow = row;
		if(horizontal) {
			n = mSize - col;
		} else {
			n = mSize - row;
		}
		for(int i = 0; i < n; i++) {
			if(horizontal) {
				curCol = col+i;
			} else {
				curRow = row+i;
			}
			if(getField(curRow,curCol) != null && getField(curRow,curCol).getTile() != null)  {
				res += getField(curRow,curCol).getTile().mChar;
			} else {
				break;
			}
		}
		
		return res;
	}
	
	public int getSize() {
		return mSize;
	}
	
	public Word getFullWord(String letters, int row, int col, boolean horizontal) {
		String wordString = "";
		int finalCol = col;
		int finalRow = row;
		
		if(horizontal) {
			if(col > 0) {
				// Get all letters before new letters
				Field prevHor;
				int i = 1;
				while((prevHor = getField(row,col-i)) != null) {
					prevHor = getField(row,col-i);
					Tile prevTile = prevHor.getTile();
					if(prevTile == null)
						break;
					
					i++;
					wordString = prevTile.mChar + wordString;
									
					//Adjust horizontal starting position for word
					finalCol--;
				}
			}
			
			// Get letters between new letters
			wordString += fillGaps(letters, row, col, horizontal);
			
			if(col + letters.length() < getSize() -1) {
				// Get all letters after new letters
				Field nextHor;
				int i = 0;
				while((nextHor = getField(row,col + letters.length() +i)) != null) {
					Tile nextTile = nextHor.getTile();
					if(nextTile == null)
						break;
					
					i++;
					wordString += nextTile.mChar; 
				}
			}
		} else {
			// horizontal == false
			
			if(row > 0) {
				// Get all letters before new letters
				Field prevVer;
				int i = 1;
				while((prevVer = getField(row-i,col)) != null) {
					Tile prevTile = prevVer.getTile();
					if(prevTile == null)
						break;
					
					i++;
					wordString = prevTile.mChar + wordString;
					
					//Adjust vertical starting position for move
					finalRow--;
				}	
			}
			
			// Get letters between new letters
			try {
				wordString += fillGaps(letters, row, col, horizontal);
			} catch(NullPointerException npe) {
				System.out.println("Caught NPE with '" + letters + "' at " + row + ", " + col + ". Horizontal: " + horizontal);
			}
						
			if(row + letters.length() < getSize() -1) {
				// Get all letters after new letters
				Field nextVer;
				int i = 0;
				while((nextVer = getField(row + letters.length() +i,col)) != null) {
					Tile nextTile = nextVer.getTile();
					if(nextTile == null)
						break;	
					i++;
					wordString += nextTile.mChar; 
				}
			}
			
		}
		
		return new Word(wordString,finalRow,finalCol,horizontal,this);
	}
	
	public String fillGaps(String letters, int row, int col, boolean horizontal) {
		// Fill gaps between letters in given direction
		String word = "";
		
		if(horizontal) {
			
			// Get letters between new letters
			
			for(int i = 0; i < letters.length(); i++) {
				
				if(letters.charAt(i) == ' ') {
					letters = letters.substring(0,i) + getField(row,col+i).getTile().mChar + letters.substring(i+1,letters.length());
				}
				
			}
				
			word += letters;
		
		} else {
			// horizontal == false
			
			// Get letters between new letters
			
			for(int i = 0; i < letters.length(); i++) {
				if(letters.charAt(i) == ' ') {
					letters = letters.substring(0,i) + getField(row+i,col).getTile().mChar + letters.substring(i+1,letters.length());
				}
			}
							
			word += letters;
			
		}
		
		return word;
	}
	
	public void setWord(int row, int col, String letters, boolean horizontal) {
		int n = letters.length();
		for(int i = 0; i < n; i++) {
			mBoard[row][col] = new Field(letters.charAt(i));
			if(horizontal) {
				col += 1;
			} else {
				row += 1;
			}
		}
		mEmpty = false;
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
