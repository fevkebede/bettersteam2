package tfe;

import tmge.Cell;
import tmge.Tile;
import tmge.TFETile;
import tmge.TileFactory;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TFE extends Application {
	
//	TODO use grid 
	private TFETile[][] tileGrid = new TFETile[MAX_ROWS][MAX_COLS];;
	
    ArrayList<Cell> toDelete = new ArrayList<Cell>();
    private TileFactory tileFactory = new TileFactory();
    private IntegerProperty score = new SimpleIntegerProperty(2);
    private boolean boardFilled = false;
    
    public static int MAX_ROWS = 4;
    public static int MAX_COLS = 4;
    public static int TILE_SIZE = 100; //pixel diameter of tile
    
    private static final int UP = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;
    private static final int RIGHT = 4;

    private int goal = 2048;
    private boolean GAME_ACTIVE = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
    	Scene scene = new Scene(createContent(), MAX_ROWS * TILE_SIZE + 200, MAX_COLS * TILE_SIZE + 200);
        
		scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
            	System.out.println("LEFT");
            	handleMove(LEFT);
            } else if (e.getCode() == KeyCode.RIGHT) {
            	System.out.println("RIGHT");
            	handleMove(RIGHT);
            } else if (e.getCode() == KeyCode.UP) {
            	System.out.println("UP");
            	handleMove(UP);
            } else if (e.getCode() == KeyCode.DOWN) {
            	System.out.println("DOWN");
            	handleMove(DOWN);
            }
        });
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bejeweled");
        primaryStage.show();
    }
    
    private Parent createContent() {
    	GridPane root = new GridPane();
    	root.setPadding(new Insets(20, 20, 20, 20));

    	GridPane board = new GridPane();
        board.setPrefSize(MAX_ROWS * TILE_SIZE, MAX_COLS * TILE_SIZE);
        
        System.out.println("creating scene ");
        
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
            
            	Text text = new Text();
            	text.setFont(Font.font(40));
            	
            	TFETile new_tile = tileFactory.createSquareTile(j, i, TILE_SIZE, text);
            	
//            	StackPane layers text over tile
            	board.add(new StackPane(new_tile, text), j, i);
            	
                setInitialCell(i, j, new_tile);
            }
        }
        
        fillTwo();
        fillTwo();

        Text title = new Text("2048");
        Text textScore = new Text();
        
        title.setFont(Font.font(64));
        textScore.setFont(Font.font(44));
        textScore.textProperty().bind(score.asString("Score: %d"));

//        add(item_to_add, colInd, rowInd)
        root.add(title, 0, 0);
        root.add(board, 0, 1);
        root.add(textScore, 0, 2);
        return root;
    }
    
    
    private void handleMove(int dir) {
    	
        if (GAME_ACTIVE) {
        	moveBlocks(dir);
            fillTwo();
            checkGameover();
        } else {
        	System.out.println("Game over");
        }
    }

    public void moveBlocks(int dir) {
        switch(dir){
            case UP: { // UP
                for(int j = 0; j < MAX_COLS; j++){
                    for(int i = 1; i < MAX_ROWS; i++){
                        int k = 0;
                        int[] tempArr = {0, 0 ,0, 0};
                        while(k < i){
                            Cell A = new Cell(i, j);
                            Cell B = new Cell(k, j);
                            if(getTileValue(k, j) == 0){
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
                for(int j = 0; j < MAX_COLS; j++){
                    for(int i = MAX_ROWS - 2; i >= 0; i--){
                        int k = MAX_ROWS - 1;
                        int[] tempArr = {0, 0 ,0, 0};
                        while(k > i){
                            Cell A = new Cell(i, j);
                            Cell B = new Cell(k, j);
                            if(getTileValue(k, j) == 0){
                                moveBlock(A, B);
                            }
                            else{
                                if(canCombine(A, B) && tempArr[j] != 1){
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
                for(int i = 0; i < MAX_ROWS; i++){
                    int[] tempArr = {0, 0 ,0, 0};
                    for(int j = 1; j < MAX_COLS; j++){
                        int k = 0;
                        while(k < j){
                            Cell A = new Cell(i, j);
                            Cell B = new Cell(i, k);
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
                for(int i = 0; i < MAX_ROWS; i++){
                    int[] tempArr = {0, 0 ,0, 0};
                    for(int j = MAX_COLS - 2; j >= 0; j--){
                        int k = MAX_COLS - 1;
                        while(k > j){
                            Cell A = new Cell(i, j);
                            Cell B = new Cell(i, k);
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

    private boolean canCombine(Cell A, Cell B) {
        // Checking if values in both cells are the same
        int valueA = getTileValue(A.getRow(), A.getCol());
        int valueB = getTileValue(B.getRow(), B.getCol());
        if(valueA == valueB){
            return true;
        }
        return false;
    }

    // A to B
    private void combine(Cell A, Cell B) {
        int doubleValue = getTileValue(B.getRow(), B.getCol()) * 2;
        setTileValue(B.getRow(), B.getCol(), doubleValue);
        setTileValue(A.getRow(), A.getCol(), 0);

        System.out.println("double " + doubleValue + " score " + score.getValue());
        if (doubleValue > score.getValue()){ score.setValue(doubleValue); }
    }

    private void moveBlock(Cell A, Cell B){
        int tempVal = getTileValue(A.getRow(), A.getCol());
        setTileValue(B.getRow(), B.getCol(), tempVal);
        setTileValue(A.getRow(), A.getCol(), 0);
    }

//    @Override
    public void checkGameover() {
        GAME_ACTIVE = !boardFilled && score.getValue() < goal;
    }
    
    int getTileValue(int row, int col) { return tileGrid[row][col].getValue(); }
    
    public void setTileValue(int row, int col, int value) {
        updateTileValue(tileGrid[row][col], value);
    }
    
    private void updateTileValue(TFETile tile, int value) {
   	 tile.setValue(value);
   	 String label = tile.getValue() == 0 ? "" : String.valueOf(tile.getValue());
   	 tile.getLabel().setText(label);
   }
    
    private void setInitialCell(int row, int col, TFETile tile) {
        tileGrid[row][col] = tile;
    }
    
//    @Override
//    public void matchCheck() {
//
//    }
//
//    @Override
//    public void save() {
//
//    }
//
//    @Override
//    public int quit() { return score; }

//    TODO fill either 2 or 4
    public void fillTwo() {
        // Set two cells to value 2 to get beginning board
        // 90% fill 2, 10% fill 4
        
    	ArrayList<Cell> emptyCells = new ArrayList<Cell>();
        for (int i = 0; i < MAX_ROWS; i++){
            for(int j = 0; j < MAX_COLS; j++){
                if (getTileValue(i, j) == 0){
                    emptyCells.add(new Cell(i, j));
                }
            }
        }
        
        if (emptyCells.size() == 0) {
            boardFilled = true;
        }

        int index = tileFactory.getRandom(0, emptyCells.size() - 1);
        Cell tempCell = emptyCells.get(index);
        
        setTileValue(tempCell.getRow(), tempCell.getCol(), 2);
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}