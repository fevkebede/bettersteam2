package tmge;

import java.util.function.Function;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class Game {
	private static int ROWS;
	private static int COLUMNS;
	
	protected Grid grid;
	protected PlayerData player;
	protected IntegerProperty score = new SimpleIntegerProperty();
	protected Function<Integer, Integer> onGameEnd;
	
	
	protected Game(int rows, int columns) {
		ROWS = rows;
		COLUMNS = columns;
	}
	
	protected abstract Tile createTile(int row, int col);
	protected abstract GridPane createGame();
	protected abstract void checkGameover();
	protected abstract void quit();
//	public abstract void save();
	
	
	protected GridPane createBoard(boolean withLabels) {
		GridPane board = new GridPane();
		
		for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
            	
            	Tile new_tile = createTile(j, i);
            	
            	if (withLabels) {
            		Text tileLabel = new Text();
                	tileLabel.setFont(Font.font(40));  	
                	
            		IntegerProperty tileValue = ((TFETile) new_tile).getValueProperty();
                	tileValue.addListener((property, oldVal, newVal) -> {
                    	if (newVal.intValue() == 0) {
                    		tileLabel.setText("");
                    	}
                    	else {   
                    		tileLabel.setText(String.valueOf(newVal));
                    	}
                	});
                	
                	board.add(new StackPane(new_tile, tileLabel), j, i);
                	
            	} else {
            		board.add(new_tile, j, i);            		
            	}
            	
            	grid.setTile(i, j, new_tile);
            	
            }
		}
		
		return board;
		
	}
	
	
	protected Text createTitle(String name) {
		Text title = new Text(name);
		title.setFont(Font.font(64));
		return title;
	}
	
	protected Text createScoreText(String score) {
		Text scoreText = new Text(score);
		scoreText.setFont(Font.font(44));
		scoreText.textProperty().bind(this.score.asString("Score: %d"));
		return scoreText;
	}

}
