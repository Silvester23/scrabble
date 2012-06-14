package de.mainaim.scrabblesolver;

public class Tile {
	public int mValue;
	public char mChar;
	
	public Tile(char character) {
		mChar = Character.toLowerCase(character);
		switch(mChar) {
			case 'e':
			case 'n':
			case 's':
			case 'i':
			case 'r':
			case 't':
			case 'u':
			case 'a':
			case 'd':
				mValue = 1;
				break;
			case 'h':
			case 'g':
			case 'l':
			case 'o':
				mValue = 2;
				break;
			case 'm':
			case 'b':
			case 'w':
			case 'z':
				mValue = 3;
				break;
			case 'c':
			case 'f':
			case 'k':
			case 'p':
				mValue = 4;
				break;
			case '�':
			case 'j':
			case '�':
			case 'v':
				mValue = 6;
				break;
			case '�':
			case 'x':
				mValue = 8;
				break;
			case 'q':
			case 'y':
				mValue = 10;
				break;
				
		}
	}
}
