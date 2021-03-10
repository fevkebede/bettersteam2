package tmge;

public class PlayerData {
    private Statistics playerData;
    private String name;

    public PlayerData(String name) {
        playerData = new Statistics();
        this.name = name;
    }

    public void saveData(Statistics statistics) {
        playerData = statistics;
    }

    public Statistics retrieveData() {
        return playerData;
    }
}
