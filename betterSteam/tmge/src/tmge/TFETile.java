package tmge;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class TFETile extends Tile {
//    private int size;
	private IntegerProperty value = new SimpleIntegerProperty();
	private Text label;

    public TFETile(int row, int col, int size, Text label) {
    	super(size);
    	this.row = row;
    	this.col = col;
    	this.label = label;
    	
    	setFill(Color.GREY);
    	setStroke(Color.BLACK);

    }
    
    public IntegerProperty getValueProperty() {
    	return value;
    }
    
    public int getValue() {
    	return value.getValue();
    }

    public void setValue(int val) {
    	this.value.setValue(val);
    }

    public Text getLabel() {
    	return label;
    }

    @Override
    public String toString() {
//    	if (flagged) return "(null)";
    	return String.format("(%d,%d) ", getRow(), getColumn());
    }
}
