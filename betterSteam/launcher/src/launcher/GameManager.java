package launcher;

import tfe.TFE;
import tmge.PlayerData;
import bejeweled.Bejeweled;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GameManager  extends Application {
	private HashMap<String, PlayerData> players = new HashMap<String, PlayerData>();
	private PlayerData currentPlayer;
	
	private PlayerData player2 = null; // Creating player2 placeholder
	private boolean isSecondGame = false; // Identify if multiplayer game is being played
	private GameType multiplayerGame; // Identifies which game is being played - only matters for multiplayer
	
	GridPane window = new GridPane();
	Button playTFE = new Button("2048");
	Button playBejeweled = new Button("Bejeweled");
	Button allHighScores = new Button("View All Scores");
	Button logout = new Button("Logout");
	Button myScores = new Button("My Scores");
	Button home = new Button("Home");
	
	public GameManager() {
		System.out.println("GameManager Constructor");
		initializeButtons();
	}
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	System.out.println("GameManager javafx start");
    	
    	window.setAlignment(Pos.CENTER);
    	window.setHgap(20); 
        window.setVgap(20);

        showPlayersScreen();
   	
        primaryStage.setScene(new Scene(window, 800, 800));
        primaryStage.setTitle("Better Steam");
        primaryStage.show();
    }
    
    private void showPlayersScreen() {
    	clearScreen();
    	
    	Label label = new Label("New player? Enter name!");
    	TextField input = new TextField();
    	input.setPromptText("Name");

    	Button submit = new Button("Submit");
    	
    	submit.setOnAction(e -> {
    		
    		String name = input.getText();
    		
    		if (multiplayerGame == null) {    			
    			setCurrentPlayer(name);
    			
    			clearScreen();
    			showMenu();
    		} else {
    			
    			setPlayerTwo(name);
    			startGame(multiplayerGame);
    		}
    	});
    	
    	window.add(getPlayersButtons(), 0, 0);
    	window.add(label, 0, 1);
    	window.add(input, 0	, 2);
    	window.add(submit, 1, 2);
    	
    }
    
    private GridPane getPlayersButtons() {
    	
    	GridPane playerButtons = new GridPane();
    	
    	int playerIndex = 1;
    	
    	if (!players.isEmpty()) {
    		
    		playerButtons.setHgap(10); 
    		playerButtons.setVgap(10);
    		
    		Text saved = new Text("Saved Players");
    		playerButtons.add(saved, 0, 0);
        	
        	
        	for (Map.Entry<String, PlayerData> mapElement : players.entrySet()) { 
                String key = (String)mapElement.getKey(); 
                
                Button usernameButton = new Button(key);
                usernameButton.setOnAction(e -> {
                	
                	if (multiplayerGame == null) {
                		
                		currentPlayer = players.get(key);
                		clearScreen();
                		showMenu();
                	} else {
                		player2 = players.get(key);
                		startGame(multiplayerGame);
                	}
                });
                
                playerButtons.add(usernameButton, 0, playerIndex);
                playerIndex++;
            } 
    	}
    	
    	return playerButtons;
    }
    
    
    private void showMenu() {
    	Text welcome = new Text(String.format("Welcome, %s!", currentPlayer.getName()));
    	welcome.setFont(Font.font(20));
    	window.add(welcome, 0, 0);
    	window.add(playBejeweled, 0, 1);
    	window.add(playTFE, 1, 1);
    	window.add(allHighScores, 1, 2);
    	window.add(logout, 0, 3);
    	window.add(myScores, 0, 2);
    }
    
    
    private void setCurrentPlayer(String name) {
		if(players.containsKey(name)) { // check if player exists
			currentPlayer = players.get(name);
		}
		else { // add new player to map
    		currentPlayer = new PlayerData(name);
    		players.put(name, currentPlayer);
		}
    }
    
    private void setPlayerTwo(String name) {
		if(players.containsKey(name)) { // check if player exists
			player2 = players.get(name);
		}
		else { // add new player to map
    		player2 = new PlayerData(name);
    		players.put(name, player2);
		}
    }
    
    
    private void getGameMode(GameType type) {
    	clearScreen();
    	Button singleplayer = new Button("Singleplayer");
    	Button multiplayer = new Button("Multiplayer");
    	
    	
    	singleplayer.setOnAction(e -> {
    		clearScreen();
    		startGame(type);
    	});
    	
    	multiplayer.setOnAction(e -> {
    		multiplayerGame = type;
    		showPlayersScreen();
    	});
    	
    	window.add(singleplayer, 0, 0);
    	window.add(multiplayer, 1, 0);
    }
    
    
    private void startGame(GameType type) {
     	clearScreen();

     	Function<Integer, Integer> endGame = (e) -> {
            onEndGame();
            return e;
        };
        
        System.out.println("\nstartGame");
        System.out.println("\nmultiplayer " + multiplayerGame != null);
        System.out.println("\nisSecondGame " + isSecondGame);
        
        PlayerData player;
        
        if (multiplayerGame != null && isSecondGame == true) {
        	player = player2;
        	
        	Label label = new Label(player.getName() + "'s turn!");
    		label.setFont(Font.font(20));
    		window.add(label, 0, 3);
    		
        } else if (multiplayerGame != null && isSecondGame == false) {
        	player = currentPlayer;
        	
        	Label label = new Label(player.getName() + " is going first!");
    		label.setFont(Font.font(20));
    		window.add(label, 0, 3);
        } else {
        	player = currentPlayer; // single player
        }
        
     	switch(type) {
     		case TFE:
     			System.out.println("run tge");
     			TFE tfe = new TFE(player, endGame);
     			GridPane tfe_board = tfe.createGame();
     			
     			window.add(tfe_board, 0, 2);
     			break;

     		case BEJEWELED:
     			System.out.println("run bejeweled");

     			Bejeweled bejeweled = new Bejeweled(player, endGame);
     	    	GridPane bejeweled_board = bejeweled.createGame();

     	    	window.add(bejeweled_board, 0, 2);
     			break;
     	}
     }
    
    public void onEndGame() {
    	if(multiplayerGame != null && isSecondGame == false) { // first player finished, continue to second player turn
    		isSecondGame = true;
    		startGame(multiplayerGame);
    	}
    	else if(multiplayerGame != null && isSecondGame == true) { // both players finished, continue to winner's screen
    		winnerScreen();
    	}
    	else { // is single player, go back to main menu
        	clearScreen();
        	showMenu();
    	}
    }
    
    private void showAllHighScores() {    	
    	String bejeweledText = "Bejeweled High Scores\n-------------------------\n";
    	String tfeText = "2048 High Scores\n-------------------------\n";
    	
    	for (Map.Entry<String, PlayerData> mapElement : players.entrySet()) { 
            String key = (String)mapElement.getKey(); 
            PlayerData value = ((PlayerData)mapElement.getValue()); 
  
            tfeText += key + ":\t\t" + String.valueOf(value.retrieveData().getTfeHighScore()) + "\n"; 
            bejeweledText += key + ":\t\t" + String.valueOf(value.retrieveData().getBejeweledHighScore()) + "\n"; 
        }
        
    	Text bejeweledScore = new Text();
    	bejeweledScore.setText(bejeweledText);
    	
    	Text tfeScore = new Text();
    	tfeScore.setText(tfeText);
    	
    	window.add(tfeScore, 0, 1);
    	window.add(bejeweledScore, 1, 1);
    	window.add(home, 0, 3);
    }
    
    private void showCurrentPlayerScores() {
    	
    	Text playerName = new Text("Scores for " + currentPlayer.getName());
    	playerName.setFont(Font.font(18));
    	
    	Text bejeweledScore = new Text();
    	bejeweledScore.setText("Bejeweled\n--------------------\nHigh Score: " + String.valueOf(currentPlayer.retrieveData().getBejeweledHighScore()));
    	
    	Text tfeScore = new Text();
    	tfeScore.setText("2048\n--------------------\nHigh Score: " + String.valueOf(currentPlayer.retrieveData().getTfeHighScore()));
    	
    	
    	Button clearTfe = new Button("Clear 2048");
    	Button clearBejeweled = new Button("Clear Bejeweled");
    	Button clearAll = new Button("Clear all high scores");

    	clearTfe.setOnAction(e -> {
    		currentPlayer.clearTfeHighScore();
    		clearScreen();
    		showCurrentPlayerScores();
    	});
    	clearBejeweled.setOnAction(e -> {
    		currentPlayer.clearBejeweledHighScore();
    		clearScreen();
    		showCurrentPlayerScores();
    	});
    	clearAll.setOnAction(e -> {
    		currentPlayer.clearAllHighScores();
    		clearScreen();
    		showCurrentPlayerScores();
    	});
    	
    	window.add(playerName, 0, 0);
    	window.add(tfeScore, 0, 1);
    	window.add(bejeweledScore, 1, 1);
    	window.add(clearTfe, 0, 2);
    	window.add(clearBejeweled, 1, 2);
    	window.add(clearAll, 0, 3);
    	window.add(home, 0, 4);
    }
    
    private void winnerScreen() { // prints the winner between two players in multiplayer mode
    	clearScreen();
    	
    	Label winner = new Label();
    	winner.setFont(Font.font(20));
    	
    	if (multiplayerGame == GameType.TFE) { // multiplayer game is tfe
	    	if(currentPlayer.getHighScore() > player2.getHighScore()) {
				winner.setText(currentPlayer.getName() + " won with a score of " + currentPlayer.getHighScore() + " /2048 over " +  player2.getName() + "'s score of " + player2.getHighScore() + "/2048!");
			}
			else if(currentPlayer.getHighScore() < player2.getHighScore()) {
				winner.setText(player2.getName() + " won with a score of " + player2.getHighScore() + " /2048 over " +  currentPlayer.getName() + "'s score of " + currentPlayer.getHighScore() + "/2048!");
			}
			else { // players tied
				winner.setText("Wow, you both tied with a score of " + currentPlayer.getHighScore() + "/2048!");
			}
    	}
    	else{ // multiplayer game is bejeweled
    		if(currentPlayer.getHighScore() > player2.getHighScore()) {
    			winner.setText(currentPlayer.getName() + " won with a score of " + currentPlayer.getHighScore() + " over " +  player2.getName() + "'s score of " + player2.getHighScore() + "!");
    		}
    		else if(currentPlayer.getHighScore() < player2.getHighScore()) {
    			winner.setText(player2.getName() + " won with a score of " + player2.getHighScore() + " over " +  currentPlayer.getName() + "'s score of " + currentPlayer.getHighScore() + "!");
    		}
    		else { // players tied
    			winner.setText("Wow, you both tied with a score of " + currentPlayer.getHighScore() + "!");
    		}
    	}
    	
    	player2 = null;
    	isSecondGame = false; 
    	multiplayerGame = null;
    	
    	window.add(winner, 0, 1);
    	window.add(home, 0, 2);
    }
    
    private void clearScreen() {
    	window.getChildren().clear();
    }
    
    private void initializeButtons() {
    	
    	playTFE.setOnAction(e -> {
    		getGameMode(GameType.TFE);
    	});
    	
    	playBejeweled.setOnAction(e -> {
    		getGameMode(GameType.BEJEWELED);
    	});
    	
    	allHighScores.setOnAction(e -> {
    		clearScreen();
    		showAllHighScores();
    	});
    	
    	logout.setOnAction(e -> {
    		currentPlayer = null;
    		clearScreen();
    		showPlayersScreen();
    	});
    	
    	myScores.setOnAction(e -> {
    		clearScreen();
    		showCurrentPlayerScores();
    	});
    	
    	home.setOnAction(e -> {
    		clearScreen();
    		showMenu();
    	});
    	
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
