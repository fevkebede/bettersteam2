package tmge;

import javafx.scene.paint.Color;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class TFETile extends Tile {
	private IntegerProperty value = new SimpleIntegerProperty();

    TFETile(int row, int col, int size) {
    	super(size);
    	this.row = row;
    	this.col = col;
    	
    	setFill(Color.GREY);
    	setStroke(Color.BLACK);

    }
    
    public int getValue() {
    	return value.getValue();
    }

    public void setValue(int val) {
    	this.value.setValue(val);
    }
    
    protected IntegerProperty getValueProperty() {
    	return value;
    }

    @Override
    public String toString() {
    	return String.format("(%d,%d) ", getRow(), getColumn());
    }
}
