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
    private final static int ROWS = 7;
    private final static int COLUMNS = 7;
    
    private final static int UP= 1;
    private final static int DOWN = 2;
    private final static int LEFT = 3;
    private final static int RIGHT = 4;
    
    private boolean GAME_ACTIVE = true;
    
	private ArrayList<Cell> updateList = new ArrayList<Cell>(); // List of locations to delete
	private ArrayList<Cell> matchingPairs = new ArrayList<Cell>();
    private BejeweledTileFactory bejeweledTileFactory = BejeweledTileFactory.getInstance();
    private BejeweledTile selected = null;
    
    private IntegerProperty movesLeft = new SimpleIntegerProperty(30);
    private IntegerProperty level = new SimpleIntegerProperty(1);
    private IntegerProperty goal = new SimpleIntegerProperty(500);
 
    
    public Bejeweled(PlayerData player, Function<Integer, Integer> onGameEnd) {
    	super(ROWS, COLUMNS);
    	this.grid = new Grid(ROWS, COLUMNS);
    	this.player = player;
    	this.onGameEnd = onGameEnd;
    	System.out.println("Bejeweled Contructor for " + player.getName() );
    }
   
    
    public GridPane createGame() {
    	GridPane root = new GridPane();
    	GridPane board = createBoard(false);
                
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
    
    protected BejeweledTile createTile(int row, int col) {
    	BejeweledTile new_tile = bejeweledTileFactory.createTile(row, col);
    	
    	new_tile.setOnMouseClicked(event -> {
    		handleTileClick(new_tile);
        });
    	
    	return new_tile;
    }
    
    
    private void handleTileClick(BejeweledTile tile) {
        if (selected == null) {
            selected = tile;
            selected.setSeleted();
            
        } else {
        		
    		swap(tile, selected);
    		matchCheck(tile, selected);
            
            selected.removeSelected();
            selected = null;
        }
        
        removeAllMatches(true);
        checkGameover();
        
        if (GAME_ACTIVE) {
        	quit();
        }
    }
   
    
    private void swap(Tile a, Tile b) {
    	if (validMove(a,b)) {
    		movesLeft.setValue(movesLeft.getValue()-1);
    		
    		int val = a.getValue();
    		a.setValue(b.getValue());
    		b.setValue(val);
    	} 
    }
    
    private boolean validMove(Tile a, Tile b) {        
        if (a.getRow() == b.getRow()) {
        	return (b.getColumn() == a.getColumn()-1 || b.getColumn() == a.getColumn()+1);
        }
        if (a.getColumn() == b.getColumn()) {
        	return (b.getRow() == a.getRow()-1 || b.getRow() == a.getRow()+1);
        }
        return false;
    }
    
    
//  @Override
    private void matchCheck(Tile a, Tile b) {
        // HORIZONTAL SEARCH
        for (int i = 0; i < ROWS; i++) {
//            horizontalMatch(i);
        	findMatch(i, false);
        }
        for (int i = 0; i < COLUMNS; i++) {
//            verticalMatch(i);
        	findMatch(i, true);
        }
        if (updateList.size() == 0) {
            swap(a,b);
        } else {
        	
        }
    }
    
    
    private boolean executePossibleMoves(Cell position) {
    	//	Cell position is a jewel that must be swapped in every direction possible
    	//  in order to find potential matches 
    	int row = position.getRow();
    	int col = position.getCol();
    	
    	
    	//top row
    	if(row == 0) {
    		
    		//top left coord
    		if(col == 0) { 
    			//swap down right 
    			
    		//top right coord	
    		}else if(col == COLUMNS-1){
    			//swap down left
    		
    		//top middle coords
    		}else {
    			//swap down left right
    			
    		}
    	}
    	
    	//bottom row 
    	else if(row == ROWS-1) {
    		
    		//bottom left coord
    		if(col == 0) { 
    			//swap up right
    			
    		//bottom right coord	
    		}else if(col == COLUMNS-1){
    			//swap up left
    		
    		//bottom middle coords
    		}else {
    			//swap up left right 
    			
    		}
    	//every other coord 
    	}else { 
    		//swap up down left right
    		swap(row, col, UP);

    	}
    	
    	return false; 
    }
    
    private void swap(int row, int col, int dir) {
    	
        Tile firstTile = grid.getTile(row, col);
        
        switch (dir) {
            case UP: {
                // SWAP UP
                Tile secondTile = grid.getTile(row-1,col);
              
                int firstTileValue = firstTile.getValue();
                firstTile.setValue(secondTile.getValue());
                secondTile.setValue(firstTileValue);
                break;
            }
            case DOWN: {
                // SWAP DOWN
 
                Tile secondTile = grid.getTile(row+1,col);
                
                int firstTileValue = firstTile.getValue();
                firstTile.setValue(secondTile.getValue());
                secondTile.setValue(firstTileValue);
                break;
                break;
            }
            case LEFT: {
                // SWAP LEFT
                Tile secondTile = grid.getTile(row,col-1);
                
                int firstTileValue = firstTile.getValue();
                
                firstTile.setValue(secondTile.getValue());
                secondTile.setValue(firstTileValue);
                break;
            }
            case RIGHT: {
                // SWAP RIGHT
                Tile secondTile = grid.getTile(row,col+1);
                
                int firstTileValue = firstTile.getValue();
                
                firstTile.setValue(secondTile.getValue());
                secondTile.setValue(firstTileValue);
                break;
            }
        }
    
    }
    private boolean findPossibleMatches() {
    	//iterate through each cell 
        for (int i = 0; i < ROWS; i++) {
        	for(int j = 0; j < COLUMNS; j++) {
        		executePossibleMoves(new Cell(i, j));
        	}
        }
        return false;
    }
    

    private boolean matchExists(int position, boolean vertical) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < ROWS; i++) {
        	
        	int tileValue = vertical ? 
        			grid.getTile(i, position).getValue() : grid.getTile(position, i).getValue();
        	
        			
            if (tileValue != current) {
                if (tempToDelete.size() >= 3) {
                    return true; 
                }
                tempToDelete.clear();
                tempToDelete.add(vertical ? new Cell(i, position) : new Cell(position, i));
                
                current = tileValue;

            } else {
                tempToDelete.add(vertical ? new Cell(i, position) : new Cell(position, i));
                if (i == ROWS - 1 && tempToDelete.size() >= 3) {
                    return true; 
                }
            }
        }
        
        return false; 
    }
    
    
    private void findMatch(int position, boolean vertical) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < ROWS; i++) {
        	
        	int tileValue = vertical ? 
        			grid.getTile(i, position).getValue() : grid.getTile(position, i).getValue();
        	
        			
            if (tileValue != current) {
                if (tempToDelete.size() >= 3) {
                    updateList.addAll(tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(vertical ? new Cell(i, position) : new Cell(position, i));
                
                current = tileValue;

            } else {
                tempToDelete.add(vertical ? new Cell(i, position) : new Cell(position, i));
                if (i == ROWS - 1 && tempToDelete.size() >= 3) {
                    updateList.addAll(tempToDelete);
                }
            }
        }
    }
    
    
//    private void horizontalMatch(int row) {
//        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
//        int current = -1;
//
//        for (int i = 0; i < ROWS; i++) {
//        	int tileValue = grid.getTile(row, i).getValue();
//        	
//            if (tileValue != current) {
//                if (tempToDelete.size() >= 3) {
//                    updateList.addAll(tempToDelete);
//                }
//                tempToDelete.clear();
//                tempToDelete.add(new Cell(row, i));
//                
//                current = tileValue;
//
//            } else {
//                tempToDelete.add(new Cell(row, i));
//                if (i == ROWS - 1 && tempToDelete.size() >= 3) {
//                    updateList.addAll(tempToDelete);
//                }
//            }
//        }
//    }
//
//    private void verticalMatch(int col) {
//        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
//        int current = -1;
//
//        for (int i = 0; i < COLUMNS; i++) {
//        	int tileValue = grid.getTile(i, col).getValue();
//        	
//            if (tileValue != current) {
//                if (tempToDelete.size() >= 3) {
//                    updateList.addAll(tempToDelete);
//                }
//                tempToDelete.clear();
//                tempToDelete.add(new Cell(i, col));
//
//                current = tileValue;
//
//            } else {
//                tempToDelete.add(new Cell(i, col));
//                if (i == COLUMNS - 1 && tempToDelete.size() >= 3) {
//                    updateList.addAll(tempToDelete);
//                }
//            }
//        }
//    }
    

    private void removeAllMatches(boolean FLAG) {
        boolean CHECKING = true;
        while (CHECKING) {
            for (int i = 0; i < ROWS; i++) {
//                horizontalMatch(i);
            	findMatch(i, false);
            }
            for (int i = 0; i < COLUMNS; i++) {
//                verticalMatch(i);
            	findMatch(i, true);
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
    private void save() {
    	score.setValue(score.getValue() + (10 * updateList.size()));
        if (score.getValue() >= goal.getValue()) {
        	goal.setValue(goal.getValue() + 250);
        	level.setValue(level.getValue() + 1);
        	
        	score.setValue(0);
        	movesLeft.setValue(30);
        }
    }

    @Override
    protected void quit() {
//        return score.getValue();
        player.setHighScore(1, score.getValue());
        System.out.println(player.getHighScore());
        player.setInGame(false);
        onGameEnd.apply(1);
    }
    
    
  @Override
  protected void checkGameover() {
//    	TODO check for possible moves
	  	GAME_ACTIVE = (movesLeft.getValue() <= 0);
    }
}
