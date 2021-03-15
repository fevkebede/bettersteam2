package tfe;

import tmge.Game;
import tmge.Grid;
import tmge.Cell;
import tmge.TFETile;
import tmge.TFETileFactory;
import tmge.Tile;
import tmge.PlayerData;

import java.util.ArrayList;
import java.util.function.Function;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class TFE extends Game {
	private final static int ROWS = 4;
    private final static int COLUMNS = 4;
    
    private TFETileFactory tfeTileFactory = TFETileFactory.getInstance();
    private IntegerProperty highestScore = new SimpleIntegerProperty(2);
    private boolean boardFilled = false;

    private final int GOAL = 2048;
    private boolean GAME_ACTIVE = true;
    
    public TFE(PlayerData player, Function<Integer, Integer> onGameEnd) {
    	super(ROWS, COLUMNS);
    	this.grid = new Grid(ROWS, COLUMNS);
    	this.player = player;
    	this.onGameEnd = onGameEnd;
    	
    	System.out.println("TFE Contructor " + player.getName() );
    }
    
	public GridPane createGame() {
    	
    	GridPane root = new GridPane();
    	root.setHgap(10);
        root.setVgap(10);

    	GridPane board = createBoard(true);
        
        fillTwo();
        fillTwo();

        Text highestBrick = new Text();
        Text playerScore = createScoreText("Score: 0");
        
        Button up = new Button("Up");
        Button down = new Button("Down");
        Button left = new Button("Left");
        Button right = new Button("Right");
        Button quit = new Button("Quit and Save");
        
        up.setOnAction(e -> { if (validMove(Move.UP)) handleMove(Move.UP); });
        down.setOnAction(e -> { if (validMove(Move.DOWN)) handleMove(Move.DOWN); });
        left.setOnAction(e -> { if (validMove(Move.LEFT)) handleMove(Move.LEFT); });
        right.setOnAction(e -> { if (validMove(Move.RIGHT)) handleMove(Move.RIGHT); });
        quit.setOnAction(e -> { quit(); });
        
        ButtonBar options = new ButtonBar();
        options.getButtons().addAll(up, down, left, right);
 
        highestBrick.setFont(Font.font(44));
        highestBrick.textProperty().bind(highestScore.asString("Highest Brick: %d"));
        
//        root = 2 cols x 5 rows
        root.add(createTitle("2048"), 0, 0, 2, 1);
        root.add(board, 0, 1, 1, 2);
        root.add(options, 0, 3);
        root.add(highestBrick, 1, 1);
        root.add(playerScore, 1, 2);
        root.add(quit, 0, 4);
        
        return root;
    }
	
    protected TFETile createTile(int row, int col) {
    	return tfeTileFactory.createTile(row, col);
    }
    
    
    private void handleMove(Move dir) {
        if (GAME_ACTIVE) {
        	moveBlocks(dir);
            fillTwo();
            checkGameover();
        } else {
        	System.out.println("Game over");
        }
    }
    
//  TODO fill either 2 or 4
    private void fillTwo() {
  	ArrayList<Cell> emptyCells = new ArrayList<Cell>();
      for (int i = 0; i < ROWS; i++){
          for(int j = 0; j < COLUMNS; j++){
              if (getTileValue(i, j) == 0){
                  emptyCells.add(new Cell(i, j));
              }
          }
      }
      
      if (emptyCells.size() == 0) {
          boardFilled = true;
      }

      int index = tfeTileFactory.getRandomValue(emptyCells.size());
      Cell tempCell = emptyCells.get(index);
      
      Tile to_fill = grid.getTile(tempCell.getRow(), tempCell.getCol());
      int fill_val = tfeTileFactory.getFillValue();
      
//      System.out.println("fill_val " + fill_val);
      to_fill.setValue(fill_val);
  }  
    
    private boolean validMove(Move dir) {
        switch(dir){
            case UP: { // UP
                for(int i = 1; i < ROWS; i++){
                    for(int j = 0; j < COLUMNS; j++){
                        int currCell = getTileValue(i, j);
                        if((getTileValue(i - 1, j) == 0 && currCell != 0) ||
                        		(getTileValue(i - 1, j) == currCell)){
                        	return true;
                        }
                    }
                }
                break;
            }
            case DOWN: { // DOWN
                for(int i = 0; i < ROWS - 1; i++){
                    for(int j = 0; j < COLUMNS; j++){
                        int currCell = getTileValue(i, j);
                        if((getTileValue(i + 1, j) == 0 && currCell != 0) || 
                        		(getTileValue(i + 1, j) == currCell)){
                        	return true;
                        }
                    }
                }
                break;
            }
            case LEFT: { // LEFT
                for(int i = 0; i < ROWS; i++){
                    for(int j = 1; j < COLUMNS; j++){
                        int currCell = getTileValue(i, j);
                        if((getTileValue(i, j -1) == 0 && currCell != 0) || 
                        		(getTileValue(i, j-1) == currCell)){
                            return true;
                        }
                    }
                }
                break;
            }
            case RIGHT: { // RIGHT
                for(int i = 0; i < ROWS; i++){
                    for(int j = 0; j < COLUMNS - 1; j++){
                        int currCell = getTileValue(i, j);
                        if((getTileValue(i, j + 1) == 0 && currCell != 0) || 
                        		(getTileValue(i, j+1) == currCell)){
                        	return true;
                        }
                    }
                }
                break;
            }
        }
        System.out.print("Invalid direction!\n");
        return false;
    }
    
    private void moveBlocks(Move dir) {
        switch(dir){
            case UP: { // UP
                for(int j = 0; j < COLUMNS; j++){
                    for(int i = 1; i < ROWS; i++){
                        int k = 0;
                        int[] tempArr = {0, 0 ,0, 0};
                        while(k < i){
                        	Tile A = grid.getTile(i, j);
                        	Tile B = grid.getTile(k, j);
                        	if(B.getValue() == 0){
                                moveBlock(A, B);
                            }
                            else{
                                if(canCombine(A, B) && tempArr[j] != 1){
                                    combine(A, B);
                                    tempArr[j] = 1;
                                }
                            }
                            k++;
                        }
                    }
                }
                break;
            }
            case DOWN: { // DOWN
                for(int j = 0; j < COLUMNS; j++){
                    for(int i = ROWS - 2; i >= 0; i--){
                        int k = ROWS - 1;
                        int[] tempArr = {0, 0 ,0, 0};
                        while(k > i) {
                            Tile A = grid.getTile(i, j);
                            Tile B = grid.getTile(k, j);
                            if (getTileValue(k, j) == 0) {
                                moveBlock(A, B);
                            } else {
                                if (canCombine(A, B) && tempArr[j] != 1){
                                    combine(A, B);
                                    tempArr[j] = 1;
                                }
                            }
                            k--;
                        }
                    }
                }
                break;
            }
            case LEFT: { // LEFT
                for(int i = 0; i < ROWS; i++){
                    int[] tempArr = {0, 0 ,0, 0};
                    for(int j = 1; j < COLUMNS; j++){
                        int k = 0;
                        while(k < j){
                            Tile A = grid.getTile(i, j);
                            Tile B = grid.getTile(i, k);
                            if(getTileValue(i, k) == 0){
                                moveBlock(A, B);
                            }
                            else{
                                if(canCombine(A, B) && tempArr[i] != 1){
                                    combine(A, B);
                                    tempArr[i] = 1;
                                }
                            }
                            k++;
                        }
                    }
                }
                break;
            }
            case RIGHT: { // RIGHT
                for(int i = 0; i < ROWS; i++){
                    int[] tempArr = {0, 0 ,0, 0};
                    for(int j = COLUMNS - 2; j >= 0; j--){
                        int k = COLUMNS - 1;
                        while(k > j){
                            Tile A = grid.getTile(i, j);
                            Tile B =grid.getTile(i, k);
                            if(getTileValue(i, k) == 0){
                                moveBlock(A, B);
                            }
                            else{
                                if(canCombine(A, B) && tempArr[i] != 1){
                                    combine(A, B);
                                    tempArr[i] = 1;
                                }
                            }
                            k--;
                        }
                    }
                }
                break;
            }
        }
    }

    private boolean canCombine(Tile A, Tile B) {
    	return A.getValue() == B.getValue();
    }

    // A to B
    private void combine(Tile A, Tile B) {
        int doubleValue = B.getValue() * 2;
        B.setValue(doubleValue);
        A.setValue(0);

        System.out.println("double " + doubleValue + " highestScore " + highestScore.getValue());
        if (doubleValue > highestScore.getValue()){ highestScore.setValue(doubleValue); }
        score.setValue(score.getValue() + doubleValue);
    }

    private void moveBlock(Tile A, Tile B) {
        B.setValue(A.getValue());
        A.setValue(0);
    }

    @Override
    protected void checkGameover() {
        GAME_ACTIVE = !boardFilled && highestScore.getValue() < GOAL;
    }
    
    private int getTileValue(int row, int col) {
    	return grid.getTile(row, col).getValue();
	}
   
    
    @Override
    protected void quit() { 
    	player.setHighScore(0, score.getValue());
        onGameEnd.apply(1);
    }
  
}