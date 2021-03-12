package tmge;

import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


// TODO update to extend from rectangle and add circle inside 
public class BejeweledTile extends Parent {
    private Circle circle;
    private int size;
    private int colorId; // used for comparisons since Color is an object
    private boolean flagged = false;

    public BejeweledTile(Point2D point, Color color, int colorId, int size) {
    	circle = new Circle(size / 2);
        circle.setCenterX(size / 2);
        circle.setCenterY(size / 2);
        circle.setFill(color);


//        setPosition((int)point.getX(), (int)point.getY());
        setTranslateX(point.getX() * size);
        setTranslateY(point.getY() * size);
        getChildren().add(circle);
        
        this.size = size;
        this.colorId = colorId;

    }

    public int getColumn() {
//    	inherited from Parent, gets x position
        return (int)getTranslateX() / size;
    }

    public int getRow() {
        return (int)getTranslateY() / size;
    }

    public void setColor(Paint color, int colorId) {
        circle.setFill(color);
        this.colorId = colorId;
    }

    public Paint getColor() {
        return circle.getFill();
    }
    
    public int getColorId() {
    	return colorId;
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
