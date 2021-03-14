package tmge;

public class Grid {
    private Tile[][] grid;

    public Grid(int rows, int columns) {
        grid = new Tile[rows][columns];
        
    }    
    
    protected void setTile(int row, int col, Tile tile) {
        grid[row][col] = tile;
    }
    
    public Tile getTile(int row, int col) {
    	return grid[row][col];
    }

}