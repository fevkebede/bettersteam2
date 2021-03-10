package tmge;

public class GameManager {
    private Game[] games;
    private int players;
    private String gameInstance;

    public GameManager() {
    }

    public Game[] getGames() {
        return games;
    }

    public void setGames(Game[] games) {
        this.games = games;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public String getGameInstance() {
        return gameInstance;
    }

    public void setGameInstance(String gameInstance) {
        this.gameInstance = gameInstance;
    }
}
