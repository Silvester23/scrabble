package de.mainaim.scrabblesolver;

public class Field {
	private String mValue;
	private FieldType mType;
	
	public Field() {
		mValue = "";
		mType = FieldType.STANDARD;
	}
	
	public Field(String value) {
		mValue = value;
		mType = FieldType.STANDARD;
	}
	
	public Field(FieldType type) {
		mType = type;
		mValue = "";
	}
	
	public Field(String value, FieldType type) {
		mValue = value;
		mType = type;
	}

	public String getValue() {
		return mValue;
	}

	public void setValue(String value) {
		mValue = value;
	}

	public FieldType getType() {
		return mType;
	}

	public void setType(FieldType type) {
		mType = type;
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
		output += ","+mValue+")";	
		
		
		return output;
	}
	
}
