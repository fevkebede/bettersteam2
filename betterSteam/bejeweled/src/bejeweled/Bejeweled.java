package bejeweled;

import tmge.Cell;
import tmge.Grid;
import tmge.TileFactory;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Bejeweled extends tmge.Game {
    ArrayList<Cell> toDelete; // List of locations to delete
    private Grid grid;
    private Grid tempGrid;
    private TileFactory tileFactory;
    private int score;
    public static int MAX_ROWS = 8;
    public static int MAX_COLS = 8;

    private int movesLeft = 30;
    private int level = 1;
    private int goal = 500;
    private boolean GAME_ACTIVE = true;

    public Bejeweled() {
        score = 0;
        this.tileFactory = new TileFactory();
        this.initGrid();
        this.toDelete = new ArrayList<Cell>();
    }

    @Override
    public void startGame() {
        removeAllMatches(false);
        do {
            displayGrid();
            handleInput();
            matchCheck();
            checkGameover();
            checkContinue();
        } while (GAME_ACTIVE);
        quit();
    }

    @Override
    public void initGrid() {
        this.grid = new Grid(MAX_ROWS, MAX_COLS);
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                grid.setCell(i, j, tileFactory.getNewTile(1, 8));
            }
        }
        tempGrid = grid;
    }

    public void checkContinue() {
        System.out.println("Continue playing?");
        System.out.println("1) Continue");
        System.out.println("2) Quit");
        int choice = getIntInput("Enter integer option: ", 1, 2);
        GAME_ACTIVE = (choice == 2) ? false : true;
    }
    @Override
    public void handleInput() {
        int rowNumber;
        int colNumber;
        int dirNumber;
        do {
            System.out.println("Select jewels to swap");
            rowNumber = getIntInput("Enter row number: ", 0, MAX_COLS);
            colNumber = getIntInput("Enter column number: ", 0, MAX_COLS);
            System.out.println("Select direction of jewel to swap");
            System.out.println("1) Up\n2) Down\n3) Left\n4) Right");
            dirNumber = getIntInput("Enter direction: ", 1, 4);
        } while (!validMove(rowNumber, colNumber, dirNumber));
        // DO THE SWAP
        movesLeft--;
        swap(rowNumber, colNumber, dirNumber);
    }

    private void swap(int row, int col, int dir) {
        tempGrid = grid;
        switch (dir) {
            case 1: {
                // SWAP UP
                int tempFirst = grid.getGrid()[row][col];
                int tempSecond = grid.getGrid()[row-1][col];
                grid.setCell(row-1, col, tempFirst);
                grid.setCell(row, col, tempSecond);
                break;
            }
            case 2: {
                // SWAP DOWN
                int tempFirst = grid.getGrid()[row][col];
                int tempSecond = grid.getGrid()[row+1][col];
                grid.setCell(row+1, col, tempFirst);
                grid.setCell(row, col, tempSecond);
                break;
            }
            case 3: {
                // SWAP LEFT
                int tempFirst = grid.getGrid()[row][col];
                int tempSecond = grid.getGrid()[row][col-1];
                grid.setCell(row, col-1, tempFirst);
                grid.setCell(row, col, tempSecond);
                break;
            }
            case 4: {
                // SWAP RIGHT
                int tempFirst = grid.getGrid()[row][col];
                int tempSecond = grid.getGrid()[row][col+1];
                grid.setCell(row, col+1, tempFirst);
                grid.setCell(row, col, tempSecond);
                break;
            }
        }
    }

    private boolean validMove(int row, int col, int dir) {
        boolean result = true;
        if (row == 0 && dir == 1) { result = false; }
        if (row == MAX_ROWS-1 && dir == 2) { result = false; }
        if (col == 0 && dir == 3) { result = false; }
        if (col == MAX_COLS-1 && dir == 4) { result = false; }

        if (!result) {
            System.out.print("Invalid direction!\nRe-");
        }
        return result;
    }

    @Override
    public void checkGameover() {
        GAME_ACTIVE = movesLeft > 0;
    }

    @Override
    public void displayGrid() {
        System.out.print(grid);
        System.out.println(String.format("Level: %d",level));
        System.out.println(String.format("Goal: %d",goal));
        System.out.println(String.format("Score: %d",score));
        System.out.println(String.format("Moves: %d\n",movesLeft));
    }

    @Override
    public void matchCheck() {
        // HORIZONTAL SEARCH
        for (int i = 0; i < grid.getGrid().length; i++) {
            horizontalMatch(i);
        }
        for (int i = 0; i < grid.getGrid()[0].length; i++) {
            verticalMatch(i);
        }

        if (toDelete.size() == 0) {
            grid = tempGrid;
            System.out.println("No match!");
        } else {
            removeAllMatches(true);
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
                grid = tempGrid;
                CHECKING = false;
            } else {
                markDeletion(); // CHANGE TO -1
                clearDeletions(FLAG); // Sets -1 to 0; Set POINT_FLAG == true if rewarding points
                for (int i = 0; i < grid.getGrid()[0].length; i++) {
                    gravityColumn(i); // SHIFT EACH COLUMN DOWN
                }
                genNewTiles();
            }
        }
    }

    private void genNewTiles() {
        for (int i = 0; i < grid.getGrid().length; i++) {
            for (int j = 0; j < grid.getGrid()[i].length; j++) {
                if (grid.getGrid()[i][j] == 0) {
                    grid.setCell(i, j, tileFactory.getNewTile(1, 8));
                }
            }
        }
    }

    private void markDeletion() {
        for (int i = 0; i < toDelete.size(); i++) {
            grid.setCell(toDelete.get(i).getRow(),
                         toDelete.get(i).getCol(),
                         -1);
        }
    }

    private void clearDeletions(boolean POINT_FLAG) {
        if (POINT_FLAG) {
            score += (10*toDelete.size());
            if (score >= goal) {
                level++;
                goal+=500;
                movesLeft = 30;
            }
        }
        toDelete.clear();
        for (int i = 0; i < grid.getGrid().length; i++) {
            for (int j = 0; j < grid.getGrid()[i].length; j++) {
                if (grid.getGrid()[i][j] == -1) {
                    grid.setCell(i, j, 0);
                }
            }
        }
    }

    private void gravityColumn(int col) {
        ArrayList<Integer> tempColumn = new ArrayList<Integer>();

        for (int i = 0; i < grid.getGrid().length; i++) {
            if (grid.getGrid()[i][col] != 0) {
                tempColumn.add(grid.getGrid()[i][col]);
            }
        }
        while (tempColumn.size() < MAX_COLS)  {
            tempColumn.add(0, 0);
        }

        for (int i = 0; i < grid.getGrid().length; i++) {
            grid.setCell(i, col, tempColumn.get(i));
        }
     }

    private void horizontalMatch(int row) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < grid.getGrid()[row].length; i++) {
            if (grid.getGrid()[row][i] != current) {
                if (tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(row, i));

                current = grid.getGrid()[row][i];

            } else {
                tempToDelete.add(new Cell(row, i));
            }
        }
    }

    private void verticalMatch(int col) {
        ArrayList<Cell> tempToDelete = new ArrayList<Cell>();
        int current = -1;

        for (int i = 0; i < grid.getGrid().length; i++) {
            if (grid.getGrid()[i][col] != current) {
                if (tempToDelete.size() >= 3) {
                    toDelete.addAll(tempToDelete);
                }
                tempToDelete.clear();
                tempToDelete.add(new Cell(i, col));

                current = grid.getGrid()[i][col];

            } else {
                tempToDelete.add(new Cell(i, col));
            }
        }
    }

    @Override
    public void save() {

    }

    @Override
    public int quit() {
        return score;
    }
    private int getIntInput(String prompt, int lowerBound, int higherBound) {
        Scanner in = new Scanner(System.in);
        int getInput = -1;
        do {
            System.out.println(prompt);
            try {
                getInput = in.nextInt();
            } catch(InputMismatchException e) {
                getInput = -1;
            }
        } while (getInput > higherBound || getInput < lowerBound);

        System.out.println();
        return getInput;
    }

    // SETTERS AND GETTERS
    public Grid getGrid() { return grid; }
    public void setGrid(Grid grid) { this.grid = grid; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public static void main(String[] args) {
    	Bejeweled game = new Bejeweled();
        game.startGame();
    }
}
