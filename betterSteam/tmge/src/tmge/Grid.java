package tmge;

public class Grid {
    private BejeweledTile[][] grid;

    public Grid(int rows, int cols) {
        grid = new BejeweledTile[rows][cols];
    }
    

    public void updateCell(int row, int col, BejeweledTile val) {
        grid[row][col] = val;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String result = "    ";
        for (int k = 0; k<grid[0].length; k++) {
            result += String.format(" %d ", k);
        }
        result+="\n    ";

        for (int l = 0; l<grid[0].length; l++) {
            result += "---";
        }
        result+="\n";

        for (int i = 0; i<grid.length; i++) {
            result += String.format("%d | ", i);
            for (int j = 0; j<grid[i].length; j++) {
                result += String.format(" %d ", grid[i][j]);
            }
            result+="\n";
        }
        return result;
    }

    public BejeweledTile[][] getGrid() {
        return grid;
    }
    
    public void setGrid(BejeweledTile[][] grid) {
        this.grid = grid;
    }
    
    public void setCell(int row, int col, BejeweledTile tile) {
        grid[row][col] = tile;
    }
    
}