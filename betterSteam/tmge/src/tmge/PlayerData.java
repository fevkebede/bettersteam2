package tmge;

public class PlayerData {
	private String name;
    private int highScore;
    private Statistics playerData;

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

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

	public void saveData(Statistics statistics) {
		playerData = statistics;
	}
	
	public Statistics retrieveData() {
		return playerData;
	}
}
