package tfe;

import tmge.Cell;
import tmge.Grid;
import tmge.TileFactory;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class tfe extends tmge.Game{
    ArrayList<Cell> toDelete;
    private Grid grid;
    private TileFactory tileFactory;
    private int score;
    private boolean boardFilled;
    public static int MAX_ROWS = 4;
    public static int MAX_COLS = 4;

    private int goal = 2048;
    private boolean GAME_ACTIVE = true;

    public tfe() {
        boardFilled = false;
        score = 2;
        this.tileFactory = new TileFactory();
        this.initGrid();
        this.toDelete = new ArrayList<Cell>();
    }

    @Override
    public void startGame() {
        do{
            displayGrid();
            handleInput();
            if(GAME_ACTIVE){
                fillTwo();
                checkGameover();
            }
        } while(GAME_ACTIVE);
        quit();
    }

    @Override
    public void initGrid() {

        this.grid = new Grid(MAX_ROWS, MAX_COLS);
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                grid.setCell(i, j, 0);
            }
        }
        fillTwo();
        fillTwo();
    }

    @Override
    public void handleInput() {
        int dirNumber;
        do {
            System.out.println("Select direction of to move blocks");
            System.out.println("1) Up\n2) Down\n3) Left\n4) Right\n5) Quit");
            dirNumber = getIntInput("Enter direction: ", 1, 5);
        } while (!validMove(dirNumber));
        if(GAME_ACTIVE){
            // DO THE SWIPE
            moveBlocks(dirNumber);
        }
    }

    private boolean validMove(int dir) {
        switch(dir){
            case 1: { // UP
                for(int i = 1; i < MAX_ROWS; i++){
                    for(int j = 0; j < MAX_COLS; j++){
//                        int currCell = grid.getCell(i, j);
//                        if(grid.getCell(i - 1, j) == 0 && currCell != 0){
//                            if(grid.getCell(i - 1, j) == currCell){
//                                return true;
//                            }
//                        }
                        if(grid.getCell(i - 1, j) == 0){
                            return true;
                        }
                    }
                }
                break;
            }
            case 2: { // DOWN
                for(int i = 0; i < MAX_ROWS - 1; i++){
                    for(int j = 0; j < MAX_COLS; j++){
//                        int currCell = grid.getCell(i, j);
//                        if(grid.getCell(i + 1, j) == 0 && currCell != 0){
//                            if(grid.getCell(i + 1, j) == currCell){
//                                return true;
//                            }
//                        }
                        if(grid.getCell(i + 1, j) == 0){
                            return true;
                        }
                    }
                }
                break;
            }
            case 3: { // LEFT
                for(int i = 0; i < MAX_ROWS; i++){
                    for(int j = 1; j < MAX_COLS; j++){
//                        int currCell = grid.getCell(i, j);
//                        if(grid.getCell(i, j -1) == 0 && currCell != 0){
//                            if(grid.getCell(j - 1, j) == currCell){
//                                return true;
//                            }
//                        }
                        if(grid.getCell(i, j -1) == 0){
                            return true;
                        }
                    }
                }
                break;
            }
            case 4: { // RIGHT
                for(int i = 0; i < MAX_ROWS; i++){
                    for(int j = 0; j < MAX_COLS - 1; j++){
//                        int currCell = grid.getCell(i, j);
//                        if(grid.getCell(i, j + 1) == 0 && currCell != 0){
//                            if(grid.getCell(j + 1, j) == currCell){
//                                return true;
//                            }
//                        }
                        if(grid.getCell(i, j + 1) == 0){
                            return true;
                        }
                    }
                }
                break;
            }
            case 5: {
                GAME_ACTIVE = false;
                return true;
            }
        }
        System.out.print("Invalid direction!\nRe-");
        return false;
    }

    public void moveBlocks(int dir){
        switch(dir){
            case 1: { // UP
                for(int j = 0; j < MAX_COLS; j++){
                    for(int i = 1; i < MAX_ROWS; i++){
                        int k = 0;
                        int[] tempArr = {0, 0 ,0, 0};
                        while(k < i){
                            Cell A = new Cell(i, j);
                            Cell B = new Cell(k, j);
                            if(grid.getCell(k, j) == 0){
                                moveBlock(A, B);
                            }
                            else{
                                if(canCombine(A, B) && tempArr[j] != 1){
                                    combine(A, B);
                                    tempArr[j] = 1;
                                }
                            }
                            k++;
                        }
                    }
                }
                break;
            }
            case 2: { // DOWN
                for(int j = 0; j < MAX_COLS; j++){
                    for(int i = MAX_ROWS - 2; i >= 0; i--){
                        int k = MAX_ROWS - 1;
                        int[] tempArr = {0, 0 ,0, 0};
                        while(k > i){
                            Cell A = new Cell(i, j);
                            Cell B = new Cell(k, j);
                            if(grid.getCell(k, j) == 0){
                                moveBlock(A, B);
                            }
                            else{
                                if(canCombine(A, B) && tempArr[j] != 1){
                                    combine(A, B);
                                    tempArr[j] = 1;
                                }
                            }
                            k--;
                        }
                    }
                }
                break;
            }
            case 3: { // LEFT
                for(int i = 0; i < MAX_ROWS; i++){
                    int[] tempArr = {0, 0 ,0, 0};
                    for(int j = 1; j < MAX_COLS; j++){
                        int k = 0;
                        while(k < j){
                            Cell A = new Cell(i, j);
                            Cell B = new Cell(i, k);
                            if(grid.getCell(i, k) == 0){
                                moveBlock(A, B);
                            }
                            else{
                                if(canCombine(A, B) && tempArr[i] != 1){
                                    combine(A, B);
                                    tempArr[i] = 1;
                                }
                            }
                            k++;
                        }
                    }
                }
                break;
            }
            case 4: { // RIGHT
                for(int i = 0; i < MAX_ROWS; i++){
                    int[] tempArr = {0, 0 ,0, 0};
                    for(int j = MAX_COLS - 2; j >= 0; j--){
                        int k = MAX_COLS - 1;
                        while(k > j){
                            Cell A = new Cell(i, j);
                            Cell B = new Cell(i, k);
                            if(grid.getCell(i, k) == 0){
                                moveBlock(A, B);
                            }
                            else{
                                if(canCombine(A, B) && tempArr[i] != 1){
                                    combine(A, B);
                                    tempArr[i] = 1;
                                }
                            }
                            k--;
                        }
                    }
                }
                break;
            }
        }
    }

    public boolean canCombine(Cell A, Cell B){
        // Checking if values in both cells are the same
        int valueA = grid.getCell(A.getRow(), A.getCol());
        int valueB = grid.getCell(B.getRow(), B.getCol());
        if(valueA == valueB){
            return true;
        }
        return false;
    }

    // A to B
    public void combine(Cell A, Cell B){
        int doubleValue = grid.getCell(B.getRow(), B.getCol()) * 2;
        grid.setCell(B.getRow(), B.getCol(), doubleValue);
        grid.setCell(A.getRow(), A.getCol(), 0);

        if(doubleValue > score){ score = doubleValue; }
    }

    public void moveBlock(Cell A, Cell B){
        int tempVal = grid.getCell(A.getRow(), A.getCol());
        grid.setCell(B.getRow(), B.getCol(), tempVal);
        grid.setCell(A.getRow(), A.getCol(), 0);
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

    @Override
    public void checkGameover() {
        GAME_ACTIVE = !boardFilled && score < goal;
    }

    @Override
    public void displayGrid() {
        System.out.print(grid);
        System.out.println(String.format("Current Highest Block: %d",score));
        System.out.println(String.format("Goal: %d", goal));
    }

    @Override
    public void matchCheck() {

    }

    @Override
    public void save() {

    }

    @Override
    public int quit() { return score; }

    public void fillTwo(){
        // Set two cells to value 2 to get beginning board
        // 90% fill 2, 10% fill 4
        ArrayList<Cell> emptyCells = new ArrayList<Cell>();
        for(int i = 0; i < MAX_ROWS; i++){
            for(int j = 0; j < MAX_COLS; j++){
                if(grid.getCell(i, j) == 0){
                    emptyCells.add(new Cell(i, j));
                }
            }
        }
        if(emptyCells.size() == 0){
            boardFilled = true;
        }

        int index = tileFactory.getNewTile(0, emptyCells.size()- 1);
        Cell tempCell = emptyCells.get(index);
        grid.setCell(tempCell.getRow(), tempCell.getCol(), 2);
    }
}