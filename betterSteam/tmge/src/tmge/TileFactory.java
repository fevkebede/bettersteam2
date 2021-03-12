package tmge;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class TileFactory {
	Color[] colors = new Color[] {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE, Color.GREEN, Color.PURPLE, Color.SILVER, Color.CORAL 
    };
	
    Random rand;

//    constructor can take in tile types for diff games?
    public TileFactory () {
        this.rand = new Random();
    }

    public int getNewTile(int low, int high) {
        return rand.nextInt(high-low) + low;
    }
    
    public Tile createTile(Point2D point, int size) {
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
