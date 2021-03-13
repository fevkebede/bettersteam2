package tmge;


public abstract interface Game {
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
