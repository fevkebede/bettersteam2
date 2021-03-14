package tmge;

import java.util.ArrayList;
import java.util.function.Function;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class Game {
	protected PlayerData player;
	protected Grid grid;
	
	protected IntegerProperty score = new SimpleIntegerProperty();
	
	protected Function<Integer, Integer> onGameEnd;
	
//	public abstract void update();
	
//	public abstract void startGame();
//	public abstract void initGrid();
//	public abstract void handleInput();
//	public abstract void checkGameover();
//	public abstract void displayGrid();
//	public abstract void matchCheck();
//	public abstract void save();
	public abstract void quit();
	
	
	
	void swapTiles(Tile a, Tile b) {
		
	}
//	
//	void checkMatch(Tile a, Tile b) {
//		
//	}
	
	public Text createTitle(String name) {
		Text title = new Text(name);
		title.setFont(Font.font(64));
		return title;
	}
	
	public Text createScoreText(String score) {
		Text scoreText = new Text(score);
		scoreText.setFont(Font.font(44));
		scoreText.textProperty().bind(this.score.asString("Score: %d"));
		return scoreText;
	}

}
