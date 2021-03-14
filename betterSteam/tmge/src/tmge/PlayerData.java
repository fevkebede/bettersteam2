package tmge;

public class PlayerData {
	private String name;
    private int highScore;
    private Statistics playerData;
    private boolean inGame;

    public PlayerData(String name) {
        playerData = new Statistics();
        this.name = name;
    }

    public String getName() {
    	return name;
    }
    
	public int getHighScore() {
		return highScore;
	}
	
	public boolean getInGame() {
		return inGame;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
	
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public void saveData(Statistics statistics) {
		playerData = statistics;
	}
	
	public Statistics retrieveData() {
		return playerData;
	}
}
