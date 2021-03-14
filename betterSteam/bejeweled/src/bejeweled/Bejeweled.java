package bejeweled;

import tmge.Cell;
import tmge.Game;
import tmge.Grid;
import tmge.PlayerData;
import tmge.Tile;
import tmge.BejeweledTile;
import tmge.BejeweledTileFactory;

import java.util.ArrayList;
import java.util.function.Function;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;



public class Bejeweled extends Game {
    final static int ROWS = 7;
    final static int COLUMNS = 7;
    
	private ArrayList<Cell> updateList = new ArrayList<Cell>(); // List of locations to delete
    private BejeweledTileFactory bejeweledTileFactory = BejeweledTileFactory.getInstance();
    private Tile selected = null;
    
    private IntegerProperty movesLeft = new SimpleIntegerProperty(30);
    private IntegerProperty level = new SimpleIntegerProperty(1);
    private IntegerProperty goal = new SimpleIntegerProperty(500);
 
    
    public Bejeweled(PlayerData player, Function<Integer, Integer> onGameEnd) {
    	this.grid = new Grid(ROWS, COLUMNS);
    	this.player = player;
    	this.onGameEnd = onGameEnd;
    	System.out.println("Bejeweled Contructor for " + player.getName() );
    }
    
    public GridPane createGame() {
    	GridPane root = new GridPane();
    	GridPane board = new GridPane();
        
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
            	BejeweledTile new_tile = bejeweledTileFactory.createTile(j, i);
            	new_tile.setOnMouseClicked(event -> {
            		
//            		System.out.println("\ntile clicked " + new_tile);
                    if (selected == null) {
                        selected = new_tile;
                        //selected.setSeleted();
                        
                    }
                    else {

                        swap(new_tile, selected);
                        selected = null;
                    }
                    removeAllMatches(true);
                    if (checkGameover()) {
                    	quit();
                    }
                });
            	
                grid.setTile(i, j, new_tile);
                board.add(new_tile, j, i);
            }
        }
        
        board.setHgap(5);
        board.setVgap(5);
        
        removeAllMatches(false);

        Text textLevel = new Text();
        Text textMoves = new Text();
        Text textGoal  = new Text();
        Text textScore = createScoreText("Score: 0");
        Button quit = new Button("Quit");
        
        quit.setOnAction(e -> {
    		quit();
    	});
        
        textLevel.setFont(Font.font(44));
        textMoves.setFont(Font.font(44));
        textGoal.setFont(Font.font(44));
        
        textLevel.textProperty().bind(level.asString("Level: %d"));
        textMoves.textProperty().bind(movesLeft.asString("Moves: %d"));
        textGoal.textProperty().bind(goal.asString("Goal: %d"));
        
        root.add(createTitle("Bejeweled"), 0, 0);
        root.add(board, 0, 1);
        root.add(textLevel,  0,  2);
        root.add(textMoves, 0, 3);
        root.add(textGoal, 0, 4);
        root.add(textScore, 0, 5);
        root.add(quit, 0, 6);
        return root;
    }
    
    private void swap(Tile a, Tile b) {
//    	System.out.println("SWAP" +  a + " -> " + b);
    	
        swapColors(a,b);
        matchCheck(a,b);
    }
    
    private void swapColors(Tile a, Tile b) {
    	if (validMove(a,b)) {
    		movesLeft.setValue(movesLeft.getValue()-1);
    		
    		int val = a.getValue();
    		a.setValue(b.getValue());
    		b.setValue(val);
    	} 
    }

//    TODO needs to make sure tiles to swap are neighbors
    private boolean validMove(Tile a, Tile b) {        
        if (a.getRow() == b.getRow()) {
        	return (b.getColumn() == a.getColumn()-1 || b.getColumn() == a.getColumn()+1);
        }
        if (a.getColumn() == b.getColumn()) {
        	return (b.getRow() == a.getRow()-1 || b.getRow() == a.getRow()+1);
        }
        return false;
    }


//    @Override
    public void matchCheck(Tile a, Tile b) {
        // HORIZONTAL SEARCH
        for (int i = 0; i < ROWS; i++) {
            horizontalMatch(i);
        }
        for (int i = 0; i < COLUMNS; i++) {
            verticalMatch(i);
        }
        if (updateList.size() == 0) {
            swapColors(a,b);
        } else {
        	
        }
    }
    
    private void horizontalMatch(int row) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < ROWS; i++) {
        	int tileValue = grid.getTile(row, i).getValue();
        	
            if (tileValue != current) {
                if (tempToDelete.size() >= 3) {
                    updateList.addAll(tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(row, i));
                current = tileValue;

            } else {
                tempToDelete.add(new Cell(row, i));
                if (i == ROWS - 1 && tempToDelete.size() >= 3) {
                    updateList.addAll(tempToDelete);
                }
            }
        }
    }

    private void verticalMatch(int col) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < COLUMNS; i++) {
        	int tileValue = grid.getTile(i, col).getValue();
        	
            if (tileValue != current) {
                if (tempToDelete.size() >= 3) {
                    updateList.addAll(tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(i, col));

                current = tileValue;

            } else {
                tempToDelete.add(new Cell(i, col));
                if (i == COLUMNS - 1 && tempToDelete.size() >= 3) {
                    updateList.addAll(tempToDelete);
                }
            }
        }
    }

    private void removeAllMatches(boolean FLAG) {
        boolean CHECKING = true;
        while (CHECKING) {
            for (int i = 0; i < ROWS; i++) {
                horizontalMatch(i);
            }
            for (int i = 0; i < COLUMNS; i++) {
                verticalMatch(i);
            }

            if (updateList.size() == 0) {
                CHECKING = false;
                
            } else {
                for (int i = 0; i < COLUMNS; i++) {
                    gravityColumn(i); // SHIFT EACH COLUMN DOWN
                }    
                if (FLAG) { save(); }
                updateList.clear();
            }
        }
    }
    
    private boolean inToDelete(int row, int col) {
    	for (int i = 0; i < updateList.size(); i++) {
    		if (updateList.get(i).getRow() == row && updateList.get(i).getCol() == col) {
    			return true;
    		}
    	}
    	return false;
    }

    private void gravityColumn(int col) {
        ArrayList<Integer> tempColumn = new ArrayList<Integer>();
        
        for (int i = 0; i < ROWS; i++) {
        	if (!inToDelete(i, col)) {
        		tempColumn.add(grid.getTile(i, col).getValue());
        	}
        }      
    	
        while (tempColumn.size() < COLUMNS)  {
            tempColumn.add(0, bejeweledTileFactory.getRandomColorId());
        }

        for (int i = 0; i < ROWS; i++) {
        	grid.getTile(i, col).setValue(tempColumn.get(i));
        }
    }
   

//    @Override
    public void save() {
    	score.setValue(score.getValue() + (10 * updateList.size()));
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
        player.setHighScore(1, score.getValue());
        System.out.println(player.getHighScore());
        player.setInGame(false);
        onGameEnd.apply(1);
    }
    
    
//  @Override
    public boolean checkGameover() {
//    	TODO check for possible moves
    	return (movesLeft.getValue() <= 0);
    }
}
