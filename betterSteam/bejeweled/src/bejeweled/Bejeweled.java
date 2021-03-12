package bejeweled;

import tmge.Cell;
import tmge.Grid;
import tmge.Tile;
import tmge.TileFactory;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Bejeweled extends Application {

    ArrayList<Cell> toDelete = new ArrayList<Cell>(); // List of locations to delete
    public static int MAX_ROWS = 8;
    public static int MAX_COLS = 8;
    public static int TILE_SIZE = 50; //pixel diameter of tile
    
    private Grid grid = new Grid(MAX_ROWS, MAX_COLS);
    private TileFactory tileFactory = new TileFactory();
    private Tile selected = null;
    
//    IntegerProperty allows for binding to update score on the screen automatically 
    private IntegerProperty score = new SimpleIntegerProperty();

    private int movesLeft = 30;
    private int level = 1;
    private int goal = 500;
//    private boolean GAME_ACTIVE = true;
    private Cell tempCellSource;
    private Cell tempCellSwap;

    
    private Parent createContent() {
    	GridPane root = new GridPane();
    	root.setPadding(new Insets(20, 20, 20, 20));

        Pane board = new Pane();
        board.setPrefSize(MAX_ROWS * TILE_SIZE, MAX_COLS * TILE_SIZE);
        
        System.out.println("creating scene ");
        
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
            	Tile new_tile = tileFactory.createCircleTile(new Point2D(j, i), TILE_SIZE);
            	new_tile.setOnMouseClicked(event -> {
            		System.out.println("clicked tile " + new_tile);
                    if (selected == null) {
                        selected = new_tile;
//                        TODO add a border to highlight the selected tile
                    }
                    else {
                        swap(new_tile, selected);
                        selected = null;
                    }
                });
            	
                grid.setCell(i, j, new_tile);
                board.getChildren().add(new_tile);
            }
        }
        
        removeAllMatches(false);

        Text title = new Text("Bejeweled");
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
    
    private void swap(Tile a, Tile b) {
    	System.out.println("\n\nSWAP" +  a + " -> " + b);
//    	validMove(a, b);
    	tempCellSource = new Cell(a.getRow(), a.getColumn());
    	tempCellSwap = new Cell(b.getRow(), b.getColumn());
        
	   swapColors(a,b);
        
        matchCheck();
    }
    
    private void swapColors(Tile a, Tile b) {
    	Paint a_color = a.getColor();
        int a_colorId = a.getColorId();
        a.setColor(b.getColor(), b.getColorId());
        b.setColor(a_color, a_colorId);
    }

////    TODO needs to make sure tiles to swap are neighbors
//    private boolean validMove(int row, int col, int dir) {
//        boolean result = true;
//        if (row == 0 && dir == 1) { result = false; }
//        if (row == MAX_ROWS-1 && dir == 2) { result = false; }
//        if (col == 0 && dir == 3) { result = false; }
//        if (col == MAX_COLS-1 && dir == 4) { result = false; }
//
//        if (!result) {
//            System.out.print("Invalid direction!\nRe-");
//        }
//        return result;
//    }


//    @Override
    public void matchCheck() {
    	System.out.println("\nmatchCheck");
        // HORIZONTAL SEARCH
        for (int i = 0; i < grid.getGrid().length; i++) {
            horizontalMatch(i);
        }
        for (int i = 0; i < grid.getGrid()[0].length; i++) {
            verticalMatch(i);
        }

        if (toDelete.size() == 0) {
        	
//        	undo swap if no matches found - only swap color values, not tile objects
        	Tile swapValue = grid.getGrid()[tempCellSwap.getRow()][tempCellSwap.getCol()];
        	Tile sourceValue = grid.getGrid()[tempCellSource.getRow()][tempCellSource.getCol()];
        	
        	grid.setCell(tempCellSource.getRow(), tempCellSource.getCol(), swapValue);
            grid.setCell(tempCellSwap.getRow(), tempCellSwap.getCol(), sourceValue);
            
            System.out.println("No match!");
        } else {
        	System.out.println("Matchs found " + toDelete.size());
            removeAllMatches(true);
        }
    }

    private void removeAllMatches(boolean FLAG) {
        boolean CHECKING = true;
        while (CHECKING) {
        	System.out.println("\nremoveAllMatches");
            for (int i = 0; i < grid.getGrid().length; i++) {
                horizontalMatch(i);
            }
            for (int i = 0; i < grid.getGrid()[0].length; i++) {
                verticalMatch(i);
            }

            if (toDelete.size() == 0) {
                CHECKING = false;
                System.out.println("No match!");
            } else {
            	System.out.println("Matchs found " + toDelete.size() + toDelete);
                markDeletion(); // CHANGE TO -1
                clearDeletions(FLAG); // Sets -1 to 0; Set POINT_FLAG == true if rewarding points
                
                printBoard();
                for (int i = 0; i < grid.getGrid()[0].length; i++) {
                    gravityColumn(i); // SHIFT EACH COLUMN DOWN
                }
                printBoard();
                genNewTiles();
            }
        }
        printBoard();
    }

    private void genNewTiles() {
    	System.out.println("\n\ngenNewTiles");
        
        for (int i = 0; i < grid.getGrid().length; i++) {
            for (int j = 0; j < grid.getGrid()[i].length; j++) {
                if (grid.getGrid()[i][j].getFlag()) {
                	Tile to_update = grid.getGrid()[i][j];
                	
                	System.out.print("(" + i+","+j+ "): " + to_update + " --> ");
                	
                	tileFactory.setRandomColor(to_update);
                	to_update.setFlag(false);
                	
                    System.out.println(grid.getGrid()[i][j]);
                }
            }
        }
    }

    private void markDeletion() {
//    	System.out.println("\nmarkDeletion " + toDelete.size());
        for (int i = 0; i < toDelete.size(); i++) {
            int row = toDelete.get(i).getRow();
            int col = toDelete.get(i).getCol();
//            System.out.print(grid.getGrid()[row][col] + " , ");
            grid.getGrid()[row][col].setFlag(true); // flag tile that should change color
        }
    }

    private void clearDeletions(boolean POINT_FLAG) {
//    	System.out.println("\n\nclearDeletions");
        if (POINT_FLAG) {
            score.setValue(score.getValue() + (10*toDelete.size()));
            if (score.getValue() >= goal) {
                level++;
                goal+=500;
                movesLeft = 30;
            }
        }
        toDelete.clear();
        
//        is there a reason to reassign -1 to 0?
//        for (int i = 0; i < grid.getGrid().length; i++) {
//            for (int j = 0; j < grid.getGrid()[i].length; j++) {
//                if (grid.getGrid()[i][j] == null) {
//                    grid.setCell(i, j, null);
//                    System.out.print(grid.getGrid()[i][j] + " , ");
//                }
//            }
//        }
    }

    private void gravityColumn(int col) {
    	System.out.print("gravityColumn" + col + ": ");
        ArrayList<Tile> tempColumn = new ArrayList<Tile>();
        ArrayList<Tile> flaggedTiles = new ArrayList<Tile>();

        for (int i = 0; i < grid.getGrid().length; i++) {
            if (grid.getGrid()[i][col].getFlag() == false ) {
            	System.out.print(grid.getGrid()[i][col] + " , ");
                tempColumn.add(grid.getGrid()[i][col]);
            } else {
            	flaggedTiles.add(grid.getGrid()[i][col]);
            }
        }
        System.out.println();
        
        while (tempColumn.size() < MAX_COLS)  {
            tempColumn.addAll(0, flaggedTiles);
        }

        for (int i = 0; i < grid.getGrid().length; i++) {
            swapColors(grid.getGrid()[i][col], tempColumn.get(i));
        }
     }

    private void horizontalMatch(int row) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;
//        Paint current = null;

        for (int i = 0; i < grid.getGrid()[row].length; i++) {
            if (grid.getGrid()[row][i].getColorId() != current) {
                if (tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
                    System.out.println("horizontalMatchs " + tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(row, i));

                current = grid.getGrid()[row][i].getColorId();

            } else {
                tempToDelete.add(new Cell(row, i));
            }
        }
    }

    private void verticalMatch(int col) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < grid.getGrid().length; i++) {
            if (grid.getGrid()[i][col].getColorId() != current) {
                if (tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
                    System.out.println("verticalMatchs " + tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(i, col));

                current = grid.getGrid()[i][col].getColorId();

            } else {
                tempToDelete.add(new Cell(i, col));
            }
        }
    }
    
    private void printBoard() {
        System.out.println("\nBOARD");
        for (int i = 0; i < grid.getGrid().length; i++) {
            for (int j = 0; j < grid.getGrid()[i].length; j++) {
            	System.out.print(" " + grid.getGrid()[i][j].getColorId() + " | ");
            }
            System.out.println();
        }
        System.out.println();
    }

//    @Override
    public void save() {

    }

//    @Override
    public int quit() {
        return score.getValue();
    }
    
    
//  @Override
    public void checkGameover() {
//    	TODO check for possible moves
    }
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent(), MAX_ROWS * TILE_SIZE + 200, MAX_COLS * TILE_SIZE + 200));
        primaryStage.setTitle("Bejeweled");
        primaryStage.show();
    }
    


    // SETTERS AND GETTERS
//    public Grid getGrid() { return grid; }
//    public void setGrid(Grid grid) { this.grid = grid; }
//    public int getScore() { return score.getValue(); }
//    public void setScore(int score) { this.score = score; }
    
    public static void main(String[] args) {
        launch(args);
    }
}
