package bejeweled;

import tmge.Cell;

import tmge.Grid;
import tmge.BejeweledTile;
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
    public static int MAX_ROWS = 7;//7;
    public static int MAX_COLS = 7;//7;
    public static int TILE_SIZE = 50; //pixel diameter of tile
    
    private Grid grid = new Grid(MAX_ROWS, MAX_COLS);
    private TileFactory tileFactory = new TileFactory();
    private BejeweledTile selected = null;
    
//    IntegerProperty allows for binding to update score on the screen automatically 
    private IntegerProperty score = new SimpleIntegerProperty();

    private int movesLeft = 30;
    private int level = 1;
    private int goal = 500;
    private Grid tempGrid;

    
    private Parent createContent() {
    	GridPane root = new GridPane();
    	root.setPadding(new Insets(20, 20, 20, 20));

        Pane board = new Pane();
        board.setPrefSize(MAX_ROWS * TILE_SIZE, MAX_COLS * TILE_SIZE);
        
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
            	BejeweledTile new_tile = tileFactory.createCircleTile(new Point2D(j, i), TILE_SIZE);
            	new_tile.setOnMouseClicked(event -> {
                    if (selected == null) {
                        selected = new_tile;
//                        TODO add a border to highlight the selected tile
                    }
                    else {
                    	
                        swap(new_tile, selected);
                        selected = null;
                    }
                    removeAllMatches(false);
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

        root.add(title, 0, 0);
        root.add(board, 0, 1);
        root.add(textScore, 0, 2);
        return root;
    }
    
    private void swap(BejeweledTile a, BejeweledTile b) {
//    	System.out.println("\n\nSWAP" +  a + " -> " + b);
        swapColors(a,b);
        matchCheck(a,b);
    }
    
    private void swapColors(BejeweledTile a, BejeweledTile b) {
    	if (validMove(a,b)) {
    		Paint a_color = a.getColor();
            int a_colorId = a.getColorId();
            a.setColor(b.getColor(), b.getColorId());
            b.setColor(a_color, a_colorId);
    	} 
    }

////    TODO needs to make sure tiles to swap are neighbors
    private boolean validMove(BejeweledTile a, BejeweledTile b) {        
        if (a.getRow() == b.getRow()) {
        	return (b.getColumn() == a.getColumn()-1 || b.getColumn() == a.getColumn()+1);
        }
        if (a.getColumn() == b.getColumn()) {
        	return (b.getRow() == a.getRow()-1 || b.getRow() == a.getRow()+1);
        }
        return false;
    }


//    @Override
    public void matchCheck(BejeweledTile a, BejeweledTile b) {
        // HORIZONTAL SEARCH
        for (int i = 0; i < grid.getGrid().length; i++) {
            horizontalMatch(i);
        }
        for (int i = 0; i < grid.getGrid()[0].length; i++) {
            verticalMatch(i);
        }
       
//        System.out.println(toDelete);
        if (toDelete.size() == 0) {
            swapColors(a,b);
//            System.out.println("No match!");
        } else {
        	
        }
    }
    
    private void horizontalMatch(int row) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < grid.getGrid()[row].length; i++) {
            if (grid.getGrid()[row][i].getColorId() != current) {
                if (tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(row, i));
                current = grid.getGrid()[row][i].getColorId();

            } else {
                tempToDelete.add(new Cell(row, i));
                if (i == grid.getGrid()[row].length-1 && tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
//                    System.out.println("horizontalMatchs " + tempToDelete);
                }
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
                if (i == grid.getGrid().length-1 && tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
//                    System.out.println("verticalMatchs " + tempToDelete);
                }
            }
        }
    }

    private void removeAllMatches(boolean FLAG) {
        boolean CHECKING = true;
        while (CHECKING) {
            for (int i = 0; i < grid.getGrid().length; i++) {
                horizontalMatch(i);
            }
            for (int i = 0; i < grid.getGrid()[0].length; i++) {
                verticalMatch(i);
            }

            if (toDelete.size() == 0) {
                CHECKING = false;
                
            } else {
                markDeletion(FLAG); // CHANGE TO -1
                for (int i = 0; i < grid.getGrid()[0].length; i++) {
                    gravityColumn(i); // SHIFT EACH COLUMN DOWN
                }    
            }
        }
    }

    private void markDeletion(boolean POINT_FLAG) {
        for (int i = 0; i < toDelete.size(); i++) {
            int row = toDelete.get(i).getRow();
            int col = toDelete.get(i).getCol();
            grid.getGrid()[row][col].setFlag(true); // flag tile that should change color
        }
        
        if (POINT_FLAG) {
            score.setValue(score.getValue() + (10*toDelete.size()));
            if (score.getValue() >= goal) {
                level++;
                goal+=500;
                movesLeft = 30;
            }
        }
        toDelete.clear();
    }

    private void gravityColumn(int col) {
//    	System.out.print("gravityColumn" + col + ": ");
        ArrayList<Integer> tempColumn = new ArrayList<Integer>();
        
        for (int i = 0; i < grid.getGrid().length; i++) {
            if (grid.getGrid()[i][col].getFlag() == false ) {
                tempColumn.add(grid.getGrid()[i][col].getColorId());
            } else {
            	grid.getGrid()[i][col].setFlag(false);
            }
        }      

        while (tempColumn.size() < MAX_COLS)  {
            tempColumn.add(0, tileFactory.getRandomInt());
        }

        for (int i = 0; i < grid.getGrid().length; i++) {
            grid.getGrid()[i][col].updateColor(tempColumn.get(i));
        }
    }
    	
    
    private void printTiles() {
//        System.out.println("\nTiles");
        for (int i = 0; i < grid.getGrid().length; i++) {
            for (int j = 0; j < grid.getGrid()[i].length; j++) {
                System.out.print(" " + grid.getGrid()[i][j] + " | ");
            }
            System.out.println();
        }
        System.out.println();
    }
   
    private void printBoard() {
//        System.out.println("\nBOARD");
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
    
    public static void main(String[] args) {
        launch(args);
    }
}
