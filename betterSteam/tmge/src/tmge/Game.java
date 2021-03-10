package tmge;

abstract class Game {
    private Grid grid;
    private Events event;
    private int Score;
    // private for GUI if we get to it

    public Game() {
        this.grid = new Grid(5, 5);
    }

    public Grid getGrid() {
        return grid;
    }

    public void update(){

    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
