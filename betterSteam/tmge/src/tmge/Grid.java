package tmge;

public class Grid {
    private int[][] grid;

    public Grid(int rows, int cols) {
        grid = new int[rows][cols];
    }

    public void updateCell(int row, int col, int val) {
        grid[row][col] = val;
    }

    public String printGrid() {
        // TODO Auto-generated method stub
        String result = "";
        for (int i = 0; i<grid.length; i++) {
            for (int j = 0; j<grid[i].length; j++) {
                result += String.format(" %d ", grid[i][j]);
            }
            result+="\n";
        }
        return result;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }
}