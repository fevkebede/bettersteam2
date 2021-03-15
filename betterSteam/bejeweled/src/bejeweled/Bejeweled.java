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
    private BejeweledTileFactory bejeweledTileFactory = BejeweledTileFactory.getInstance();
    private BejeweledTile selected = null;
    
    private IntegerProperty movesLeft = new SimpleIntegerProperty(30);
    private IntegerProperty level = new SimpleIntegerProperty(1);
    private IntegerProperty goal = new SimpleIntegerProperty(500);
    
    private Text gameOverLabel = new Text();
 
    
    public Bejeweled(PlayerData player, Function<Integer, Integer> onGameEnd) {
    	super(ROWS, COLUMNS);
    	this.grid = new Grid(ROWS, COLUMNS);
    	this.player = player;
    	this.onGameEnd = onGameEnd;
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
        Button quit = new Button("Quit and Save");
        
        quit.setOnAction(e -> {
    		quit();
    		onGameEnd.apply(1);
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
        root.add(gameOverLabel, 1, 6);
        
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
        
        if (!GAME_ACTIVE) {
        	gameOverLabel.setText("Game Over! Out of moves!");
        	gameOverLabel.setFont(Font.font(20));
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
        	findMatch(i, false);
        }
        for (int i = 0; i < COLUMNS; i++) {
        	findMatch(i, true);
        }
        if (updateList.size() == 0) {
            swap(a,b);
        } else {
        	
        }
    }
    
    private boolean matchExistsInBoard() {
      for (int i = 0; i < ROWS; i++) {
    	  if(matchExists(i, false)) {
    		  return true;
    	  }
    	  
      }
      for (int i = 0; i < COLUMNS; i++) {
    	  if(matchExists(i, true)) {
    		  return true;
    	  }
      }
      
      return false;
    }
  
    private boolean checkRightSwap(int row, int col) {
		swap(row,col, RIGHT);
		if(matchExistsInBoard()) {
			//unswap
			swap(row,col+1,LEFT);
			return true;
		}else {
			//unswap
			swap(row, col+1, LEFT);
		}
		return false;
    }
    private boolean checkLeftSwap(int row, int col) {
		swap(row,col, LEFT);
		if(matchExistsInBoard()) {
			//unswap
			swap(row,col-1, RIGHT);
			return true;
		}else {
			//unswap
			swap(row, col-1, RIGHT);
		}
    	return false;
    }
    
    private boolean checkDownSwap(int row, int col) {
		swap(row,col, DOWN);
		if(matchExistsInBoard()) {
			//unswap
			swap(row+1,col,UP);
			return true;
		}else {
			//unswap
			swap(row+1, col, UP);
		}
		return false;
    }
    
    private boolean checkUpSwap(int row, int col) {
		swap(row,col, UP);
		if(matchExistsInBoard()) {
			//unswap
			swap(row-1,col,DOWN);
			return true;
		}else {
			//unswap
			swap(row-1, col, DOWN);
		}
    	return false;
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
    			//swap down check if board has matches
    			if(checkDownSwap(row, col)) {
    				return true; 
    			}
    			
    			//swap right check if board has matches 
    			if(checkRightSwap(row, col)) {
    				return true; 
    			}
    		}
    		else if(col == ROWS-1) {	
    			if(checkDownSwap(row, col)) {
    				return true; 
    			}
    			if(checkLeftSwap(row, col)) {
    				return true; 
    			}
    		//top inner row 
    		}else {
    			//swap up left and right 
    			if(checkDownSwap(row, col)) {
    				return true; 
    			}
    			
    			if(checkRightSwap(row, col)) {
    				return true; 
    			}
    			
    			if(checkLeftSwap(row, col)) {
    				return true; 
    			}
    			
    		}

    	}
    	
    	//bottom row
    	else if(row == ROWS-1) {
  	
    		//bottom left coord
    		if(col == 0) {
    			if(checkRightSwap(row, col)) {
    				return true; 
    			}
    			if(checkUpSwap(row, col)) {
    				return true; 
    			}
    		
    		//bottom right coord 
    		}else if(col == COLUMNS-1) {
    			if(checkLeftSwap(row, col)) {
    				return true; 
    			}
    			if(checkUpSwap(row, col)) {
    				return true; 
    			}
    			
    		//bottom inner row 
    		}else {
    			if(checkUpSwap(row, col)) {
    				return true; 
    			}
    			
    			if(checkRightSwap(row, col)) {
    				return true; 
    			}
    			
    			if(checkLeftSwap(row, col)) {
    				return true; 
    			}
    			
    		}
    	}
    	
    	//left most column
    	else if(col == 0) {
    		//left most column inner 
			if(checkUpSwap(row, col)) {
				return true; 
			}
			
			if(checkRightSwap(row, col)) {
				return true; 
			}
			
			if(checkDownSwap(row, col)) {
				return true; 
			}
    	}
    	
    	//right most column
    	else if(col == COLUMNS-1) {
    		//top right coord 
    		if(row == 0) {
    			if(checkLeftSwap(row, col)) {
    				return true; 
    			}
    			
    			if(checkDownSwap(row, col)) {
    				return true; 
    			}
    		}
    		
    		//bottom right coord
    		else if(row == ROWS-1) {
    			if(checkLeftSwap(row, col)) {
    				return true; 
    			}
    			
    			if(checkUpSwap(row, col)) {
    				return true; 
    			}
    		
    		//right most column inner 
    		}else {
    			if(checkLeftSwap(row, col)) {
    				return true; 
    			}
    			
    			if(checkUpSwap(row, col)) {
    				return true; 
    			}
     			if(checkDownSwap(row, col)) {
    				return true; 
    			}
    		}		
    	}else {
    		//non edge coords
			if(checkDownSwap(row, col)) {
				return true; 
			}
				if(checkUpSwap(row, col)) {
					return true;
				}
			if(checkLeftSwap(row, col)) {
				return true; 
			}
			if(checkRightSwap(row, col)) {
				return true; 
			}
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
    	
    	boolean possibleMoveFound = false; 
    	//iterate through each cell 
        for (int i = 0; i < ROWS; i++) {
        	for(int j = 0; j < COLUMNS; j++) {
        		if(executePossibleMoves(new Cell(i, j))) {
        			possibleMoveFound = true; 
        		}
        	}
        }
        //no matches found
        return possibleMoveFound;
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
    

    private void removeAllMatches(boolean FLAG) {
        boolean CHECKING = true;
        while (CHECKING) {
            for (int i = 0; i < ROWS; i++) {
            	findMatch(i, false);
            }
            for (int i = 0; i < COLUMNS; i++) {
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
        player.setHighScore(1, score.getValue());
    }
    
    
  @Override
  protected void checkGameover() {
	  	if(findPossibleMatches()) {
	  		GAME_ACTIVE = (movesLeft.getValue() >= 0);
	  	}else {
	  		GAME_ACTIVE = false; 
	  	}
    }
}
