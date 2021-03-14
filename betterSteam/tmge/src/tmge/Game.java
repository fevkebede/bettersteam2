package tmge;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class Game {
//	Grid grid;
	
//	public abstract void update();
	
//	public abstract void startGame();
//	public abstract void initGrid();
//	public abstract void handleInput();
//	public abstract void checkGameover();
//	public abstract void displayGrid();
//	public abstract void matchCheck();
//	public abstract void save();
	public abstract void quit();
	
//	void swapCell(Tile a, Tile b) {
//		
//	}
//	
//	void checkMatch(Tile a, Tile b) {
//		
//	}
	
	public Text createTitle(String name) {
		
		Text title = new Text(name);
		title.setFont(Font.font(64));
		return title;
	}

}
