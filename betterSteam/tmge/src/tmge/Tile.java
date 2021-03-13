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
	public abstract String toString();
	
}

//new_tile.setSeleted();
//delayedSwap(new_tile, selected);

//private void delayedSwap(BejeweledTile a, BejeweledTile b) {
//	System.out.println("\nstart delay");
//  
//	PauseTransition pause = new PauseTransition(Duration.millis(3000));
//	
//	pause.setOnFinished(event -> {
//		System.out.println("PAUSE end");
//		swap(a,b);
//		
//		a.removeSelected();
//      selected.removeSelected();
//      selected = null;
//	});
//	
//	pause.play();
//	
//	System.out.println("end delay");
//}