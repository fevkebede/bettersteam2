package bejeweled;

import tmge.Cell;

import tmge.Grid;
import tmge.PlayerData;
import tmge.BejeweledTile;
import tmge.TileFactory;

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Bejeweled {
	
	private PlayerData player;

    ArrayList<Cell> toDelete = new ArrayList<Cell>(); // List of locations to delete
    public static int MAX_ROWS = 7;
    public static int MAX_COLS = 7;
    
    private Grid grid = new Grid(MAX_ROWS, MAX_COLS);
    private TileFactory tileFactory = new TileFactory();
    private BejeweledTile selected = null;
    
//    IntegerProperty allows for binding to update score on the screen automatically 
    private IntegerProperty score = new SimpleIntegerProperty();
    private IntegerProperty movesLeft = new SimpleIntegerProperty(30);
    private IntegerProperty level = new SimpleIntegerProperty(1);
    private IntegerProperty goal = new SimpleIntegerProperty(500);
 
    
    public Bejeweled(PlayerData player) {
    	this.player = player;
    	
    	System.out.println("Bejeweled Contructor for " + player.getName() );
    }
    
    public GridPane createGame() {
    	GridPane root = new GridPane();
//    	root.setPadding(new Insets(20, 20, 20, 20));

    	GridPane board = new GridPane();
        
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
            	BejeweledTile new_tile = tileFactory.createBejeweledTile(j, i);
            	new_tile.setOnMouseClicked(event -> {
            		
//            		System.out.println("\ntile clicked " + new_tile);
                    if (selected == null) {
                        selected = new_tile;
                        selected.setSeleted();
                        
                    }
                    else {

                        swap(new_tile, selected);
                        
                        selected.removeSelected();
                        selected = null;
                    }
                    removeAllMatches(true);
                    if (checkGameover()) {
                    	quit();
                    }
                });
            	
                grid.setCell(i, j, new_tile);
                board.add(new_tile, j, i);
            }
        }
        
//        spacing between grid row/col
        board.setHgap(5);
        board.setVgap(5);
        
        removeAllMatches(false);

        Text title = new Text("Bejeweled");
        Text textLevel = new Text();
        Text textMoves = new Text();
        Text textGoal  = new Text();
        Text textScore = new Text();
//        Text movesLeft, , goal
        
        
        title.setFont(Font.font(64));
        
        textLevel.setFont(Font.font(44));
        textScore.setFont(Font.font(44));
        textMoves.setFont(Font.font(44));
        textGoal.setFont(Font.font(44));
        
        textLevel.textProperty().bind(level.asString("Level: %d"));
        textScore.textProperty().bind(score.asString("Score: %d"));
        textMoves.textProperty().bind(movesLeft.asString("Moves: %d"));
        textGoal.textProperty().bind(goal.asString("Goal: %d"));
        

        root.add(title, 0, 0);
        root.add(board, 0, 1);
        root.add(textLevel,  0,  2);
        root.add(textMoves, 0, 3);
        root.add(textGoal, 0, 4);
        root.add(textScore, 0, 5);
        return root;
    }
    
    private void swap(BejeweledTile a, BejeweledTile b) {
//    	System.out.println("SWAP" +  a + " -> " + b);
    	
        swapColors(a,b);
        matchCheck(a,b);
    }
    
    private void swapColors(BejeweledTile a, BejeweledTile b) {
    	if (validMove(a,b)) {
    		movesLeft.setValue(movesLeft.getValue()-1);
    		
    		int val = a.getValue();
    		a.setValue(b.getValue());
    		b.setValue(val);
    		
//    		Paint a_color = a.getColor();
//            int a_colorId = a.getColorId();
//            
//            a.setColor(b.getColor(), b.getColorId());
//            b.setColor(a_color, a_colorId);
    	} 
    }

//    TODO needs to make sure tiles to swap are neighbors
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
        if (toDelete.size() == 0) {
            swapColors(a,b);
        } else {
        	
        }
    }
    
    private void horizontalMatch(int row) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < grid.getGrid()[row].length; i++) {
            if (grid.getGrid()[row][i].getValue() != current) {
                if (tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(row, i));
                current = grid.getGrid()[row][i].getValue();

            } else {
                tempToDelete.add(new Cell(row, i));
                if (i == grid.getGrid()[row].length-1 && tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
                }
            }
        }
    }

    private void verticalMatch(int col) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < grid.getGrid().length; i++) {
            if (grid.getGrid()[i][col].getValue() != current) {
                if (tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(i, col));

                current = grid.getGrid()[i][col].getValue();

            } else {
                tempToDelete.add(new Cell(i, col));
                if (i == grid.getGrid().length-1 && tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
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
        if (POINT_FLAG) { save(); }
        toDelete.clear();
    }

    private void gravityColumn(int col) {
        ArrayList<Integer> tempColumn = new ArrayList<Integer>();
        
        for (int i = 0; i < grid.getGrid().length; i++) {
            if (grid.getGrid()[i][col].getFlag() == false ) {
                tempColumn.add(grid.getGrid()[i][col].getValue());
            } else {
            	grid.getGrid()[i][col].setFlag(false);
            }
        }      

        while (tempColumn.size() < MAX_COLS)  {
            tempColumn.add(0, tileFactory.getRandomColorId());
        }

        for (int i = 0; i < grid.getGrid().length; i++) {
            grid.getGrid()[i][col].setValue(tempColumn.get(i));
        }
    }
    
    private void printTiles() {
        System.out.println("\nTiles");
        for (int i = 0; i < grid.getGrid().length; i++) {
            for (int j = 0; j < grid.getGrid()[i].length; j++) {
                System.out.print(" " + grid.getGrid()[i][j] + " | ");
            }
            System.out.println();
        }
        System.out.println();
    }
   
    private void printBoard() {
        System.out.println("\nBOARD");
        for (int i = 0; i < grid.getGrid().length; i++) {
            for (int j = 0; j < grid.getGrid()[i].length; j++) {
            	System.out.print(" " + grid.getGrid()[i][j].getValue() + " | ");
            }
            System.out.println();
        }
        System.out.println();
    }

//    @Override
    public void save() {
    	score.setValue(score.getValue() + (10*toDelete.size()));
        if (score.getValue() >= goal.getValue()) {
        	goal.setValue(goal.getValue() + 250);
        	level.setValue(level.getValue() + 1);
        	
        	score.setValue(0);
        	movesLeft.setValue(30);
        }
    }

//    @Override
    public void quit() {
//        return score.getValue();
        player.setHighScore(score.getValue());
        player.setInGame(false);
    }
    
    
//  @Override
    public boolean checkGameover() {
//    	TODO check for possible moves
    	return (movesLeft.getValue() <= 0);
    }
    

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setScene(new Scene(createContent(), MAX_ROWS * TILE_SIZE + 200, MAX_COLS * TILE_SIZE + 200));
//        primaryStage.setTitle("Bejeweled");
//        primaryStage.show();
//    }
//    
//    public static void main(String[] args) {
//        launch(args);
//    }
}
