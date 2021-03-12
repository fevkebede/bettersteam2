package tmge;

import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class SquareTile extends Rectangle {
//    private int size;
	private IntegerProperty value = new SimpleIntegerProperty();
	private Text label;
    private int row;
    private int col;
//    private boolean flagged = false;

    public SquareTile(int row, int col, int size, Text label) {
    	super(size, size);
    	this.row = row;
    	this.col = col;
    	this.label = label;
    	
    	setFill(Color.GREY);
    	setStroke(Color.BLACK);

    }

    public int getColumn() {
//    	inherited from Parent, gets x position
        return col;
    }

    public int getRow() {
        return row;
    }
    
    public IntegerProperty getValueProperty() {
    	return value;
    }
    
    public int getValue() {
    	return value.getValue();
    }

    public void setValue(int val) {
//    	if (val == 0) val = null;
    	this.value.setValue(val);
    }

//    public void setFlag(boolean flag) {
//    	flagged = flag;
//    }
    
    public Text getLabel() {
    	return label;
    }

    @Override
    public String toString() {
//    	if (flagged) return "(null)";
    	return String.format("(%d,%d) ", getRow(), getColumn());
    }
}
