package tmge;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public abstract class TileFactory {
	Random rand = new Random();
//	public static int BEJEWELED_TILE_SIZE = 50;
	public static int TFE_TILE_SIZE = 100; 
//	boolean tfe;
	
	public abstract Tile createTile(int row, int col);
	static Color[] colors = new Color[] {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE, Color.GREEN, Color.PURPLE, Color.SILVER, Color.CORAL 
    };
//	static Color[] colors = new Color[] {
//            Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE, Color.GREEN, Color.PURPLE, Color.SILVER, Color.CORAL 
//    };
	
    
//    public TFETile createTFETile(int row, int col, Text label) {
//    	return new TFETile(row, col, TFE_TILE_SIZE, label);
//    }
    

//    public BejeweledTile createBejeweledTile(int row, int col) {
//    	int colorId = getRandomColorId();
//    	return new BejeweledTile(row, col, colors[colorId], colorId, BEJEWELED_TILE_SIZE);
//    }
    
    
//    public BejeweledTile createCircleTile(Point2D point, int size) {
//    	int colorId = getRandomColorId();
//    	return new BejeweledTile(point, getRandomColor(colorId), colorId, size);
//    }

//    public void setRandomColor(BejeweledTile tile) {
//    	int colorId = getRandomColorId();
//    	tile.setColor(getRandomColor(colorId), colorId);
//    }
    
//    private Color getRandomColor(int randomInt) {
//    	return colors[randomInt];
//    }

//    public int getRandomColorId() {
//    	return rand.nextInt(colors.length);
//    }
	
	public int getRandomColorId() {
    	return rand.nextInt(colors.length);
    }
//    
////	TFE
    public int getRandomValue(int low, int high) {
        return rand.nextInt(high-low) + low;
    }
    
}
