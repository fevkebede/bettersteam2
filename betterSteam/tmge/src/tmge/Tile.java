package tmge;

import javafx.scene.shape.Rectangle;

public abstract class Tile extends Rectangle {
	protected int row;
	protected int col;
	
	public Tile(int size) {
		super(size, size);
	}
	  
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return col;
	}
	
	public abstract int getValue();
	public abstract void setValue(int value);
	public abstract String toString();
	
}
