package tmge;

//import javafx.geometry.Point2D;
//import javafx.scene.Parent;
import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;


public class BejeweledTile extends Tile {
//    private Circle circle;
//    private int size;
    private int colorId; // used for comparisons since Color is an object
    private boolean flagged = false;

    BejeweledTile(int row, int col, Color color, int colorId, int size) {
    	super(size);
    	
    	this.row = row;
    	this.col = col;
    	this.colorId = colorId;
    	
    	setArcWidth(50);
    	setArcHeight(50);
    	
    	setFill(color);

    }


    public int getValue() {
    	return colorId;
    }

    public void setValue(int newColor) {
//    	if (val == 0) val = null;
    	this.colorId = newColor;
    	setFill(TileFactory.colors[newColor]);
    }
    
    public void setSeleted() {
    	setStroke(Color.BLACK);
    	setStrokeWidth(5);
    	setStrokeType(StrokeType.INSIDE);
    }
    
    public void removeSelected() {
    	setStroke(null);
    }
    
    
    public void setFlag(boolean flag) {
    	flagged = flag;
    }
    
    public boolean getFlag() {
    	return flagged;
    }

    @Override
    public String toString() {
    	if (flagged) return "(null)";
    	return String.format("(%d,%d) ", getRow(), getColumn());
    }


}
