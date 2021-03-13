package bejeweled;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class Jewel extends Parent {
    private Circle circle = new Circle(BejeweledGame.SIZE / 2);

    public Jewel(Point2D point) {
        circle.setCenterX(BejeweledGame.SIZE / 2);
        circle.setCenterY(BejeweledGame.SIZE / 2);
        circle.setFill(BejeweledGame.colors[new Random().nextInt(BejeweledGame.colors.length)]);

        setTranslateX(point.getX() * BejeweledGame.SIZE);
        setTranslateY(point.getY() * BejeweledGame.SIZE);
        getChildren().add(circle);

//        setOnMouseClicked(event -> {
//            if (selected == null) {
//                selected = this;
//            }
//            else {
//                swap(selected, this);
//                checkState();
//                selected = null;
//            }
//        });
    }

    public void randomize() {
        circle.setFill(BejeweledGame.colors[new Random().nextInt(BejeweledGame.colors.length)]);
    }

    public int getColumn() {
//    	inherited from Parent, gets x position
        return (int)getTranslateX() / BejeweledGame.SIZE;
    }

    public int getRow() {
        return (int)getTranslateY() / BejeweledGame.SIZE;
    }

    public void setColor(Paint color) {
        circle.setFill(color);
    }

    public Paint getColor() {
        return circle.getFill();
    }

}
