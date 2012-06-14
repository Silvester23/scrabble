package de.mainaim.scrabblesolver;

public class Field {
	private Tile mTile;
	private FieldType mType;
	
	public Field() {
		mType = FieldType.STANDARD;
	}
	
	public Field(Field field) {
		mTile = field.getTile();
		mType = field.getType();
	}
	
	public Field(Tile tile) {
		mTile = tile;
		mType = FieldType.STANDARD;
	}
	
	public Field(FieldType type) {
		mType = type;
	}
	
	public Field(Tile tile, FieldType type) {
		mTile = tile;
		mType = type;
	}

	public Tile getTile() {
		return mTile;
	}

	public void setTile(Tile tile) {
		mTile = tile;
	}
	
	public void setTile(char character) {
		mTile = new Tile(character);
	}

	public FieldType getType() {
		return mType;
	}
	
	@Override
	public String toString() {
		String output = "(";
		switch(mType) {
			case STANDARD:
				output += " ";
				break;
			case DOUBLECHAR:
				output += "c";
				break;
			case DOUBLEWORD:
				output += "w";
				break;
			case TRIPLECHAR:
				output += "C";
				break;
			case TRIPLEWORD:
				output += "W";
				break;
			case START:
				output += "X";
				break;
		}
		output += ",";
		if(mTile != null) {
			output += Character.toUpperCase(mTile.mChar);
		} else {
			output += " ";
		}
		output += ")";	
		
		
		return output;
	}
	
}
