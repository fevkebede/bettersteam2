package tmge;

public class PlayerData {
	private String name;
    private int highScore;
    private Statistics playerData;
//    private boolean inGame;

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
	
	public void setHighScore(int GAME_FLAG, int highScore) {
		this.highScore = highScore;
		switch (GAME_FLAG) {
			case 0: {
				if (playerData.getTfeHighScore() < highScore) {
					playerData.setTfeHighScore(highScore);
				}
				break;
			}
			case 1: {
				if (playerData.getBejeweledHighScore() < highScore) {
					playerData.setBejeweledHighScore(highScore);
				}
				break;
			}
		}
	}
	
	public void saveData(Statistics statistics) {
		playerData = statistics;
	}
	
	public Statistics retrieveData() {
		return playerData;
	}
	
	public void clearBejeweledHighScore() {
		playerData.setBejeweledHighScore(0);
	}
	
	public void clearTfeHighScore() {
		playerData.setTfeHighScore(0);
	}
	
	public void clearAllHighScores() {
		clearBejeweledHighScore();
		clearTfeHighScore();
	}
}
