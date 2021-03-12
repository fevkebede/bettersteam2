package tmge;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TileFactory {
	Random rand = new Random();
//	boolean tfe;
	
	Color[] colors = new Color[] {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE, Color.GREEN, Color.PURPLE, Color.SILVER, Color.CORAL 
    };
	

//	TFE
    public int getRandom(int low, int high) {
        return rand.nextInt(high-low) + low;
    }
    
    public SquareTile createSquareTile(int row, int col, int size, Text label) {
    	return new SquareTile(row, col, size, label);
    }
    
    
//    Bejeweled 
    public Tile createCircleTile(Point2D point, int size) {
    	int colorId = getRandomInt();
    	return new Tile(point, getRandomColor(colorId), colorId, size);
    }

    public void setRandomColor(Tile tile) {
    	int colorId = getRandomInt();
    	tile.setColor(getRandomColor(colorId), colorId);
    }
    
    private Color getRandomColor(int randomInt) {
    	return colors[randomInt];
    }

    private int getRandomInt() {
    	return rand.nextInt(colors.length);
    }
    
}
