package tmge;

// probably should extend javafx Application to Bejeweled can extend it
public abstract class Game {
//	public abstract void update();
	public abstract void startGame();
	public abstract void initGrid();
	public abstract void handleInput();
	public abstract void checkGameover();
	public abstract void displayGrid();
	public abstract void matchCheck();
	public abstract void save();
	public abstract int quit();
  
}
