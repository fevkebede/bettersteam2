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
	
	private PlayerData player2 = null;
	private boolean isMultiplayer = false;
	private int currentGame = -1;
	
	GridPane window = new GridPane();
	Button playTFE = new Button("2048");
	Button playBejeweled = new Button("Bejeweled");
	Button viewHighScores = new Button("View Scores");
	Button logout = new Button("Logout");
	Button settings = new Button("Settings");
	
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


    	showLoginScreen();
   	
        primaryStage.setScene(new Scene(window, 800, 800));
        primaryStage.setTitle("Better Steam");
        primaryStage.show();
    }
    
    private void showLoginScreen() {
    	
    	Label label = new Label("New player? Enter name!");
    	TextField input = new TextField();
    	input.setPromptText("Name");

    	Button submit = new Button("Submit");
    	Button highScores = new Button("High Scores");
    	
    	submit.setOnAction(e -> {
    		
    		String name = input.getText();
    		
    		setCurrentPlayer(name);
    		
    		clearScreen();
    		showMenu();
    	});
    	
    	highScores.setOnAction(e -> {
    		clearScreen();
    		showAllHighScores();
    	});
    	
    	int playerIndex = 1;
    	if (!players.isEmpty()) {
    		Text saved = new Text("Saved Players");
        	window.add(saved, 0, 0);
        	
        	
        	for (Map.Entry<String, PlayerData> mapElement : players.entrySet()) { 
                String key = (String)mapElement.getKey(); 
                
                Button usernameButton = new Button(key);
                usernameButton.setOnAction(e -> {
                	currentPlayer = players.get(key);
                	clearScreen();
            		showMenu();
                });
                window.add(usernameButton, 0, playerIndex);
                playerIndex++;
            } 
    	}
    	
    	window.add(label, 0, 0+playerIndex);
    	window.add(input, 0	, 1+playerIndex);
    	window.add(submit, 1, 1+playerIndex);
    	window.add(highScores, 0, 2+playerIndex);
    	
    }
    
    private void setCurrentPlayer(String name) {
    	// check if player exists
		if(players.containsKey(name)) {
			currentPlayer = players.get(name);
		}
		// add new player to map
		else {
    		currentPlayer = new PlayerData(name);
    		players.put(name, currentPlayer);
		}
    }
    
    private void settingScreen() {
    	Text bejeweledScore = new Text();
    	bejeweledScore.setText("Bejeweled\n--------------------\nHigh Score: " + String.valueOf(currentPlayer.retrieveData().getBejeweledHighScore()));
    	
    	Text tfeScore = new Text();
    	tfeScore.setText("2048\n--------------------\nHigh Score: " + String.valueOf(currentPlayer.retrieveData().getTfeHighScore()));
    	
    	
    	Button clearTfe = new Button("Clear 2048");
    	Button clearBejeweled = new Button("Clear Bejeweled");
    	Button clearAll = new Button("Clear all high scores");
    	Button close = new Button("Go Back");
    	
    	close.setOnAction(e -> {
    		clearScreen();
    		showMenu();
    	});
    	clearTfe.setOnAction(e -> {
    		currentPlayer.clearTfeHighScore();
    		clearScreen();
    		settingScreen();
    	});
    	clearBejeweled.setOnAction(e -> {
    		currentPlayer.clearBejeweledHighScore();
    		clearScreen();
    		settingScreen();
    	});
    	clearAll.setOnAction(e -> {
    		currentPlayer.clearAllHighScores();
    		clearScreen();
    		settingScreen();
    	});
    	window.add(tfeScore, 0, 1);
    	window.add(bejeweledScore, 1, 1);
    	window.add(clearTfe, 0, 2);
    	window.add(clearBejeweled, 1, 2);
    	window.add(clearAll, 0, 3);
    	window.add(close, 0, 4);
    }
    
    private void showHighScores() {    	
    	Text bejeweledScore = new Text();
    	bejeweledScore.setText("Bejeweled\n--------------------\nHigh Score: " + String.valueOf(currentPlayer.retrieveData().getBejeweledHighScore()));
    	
    	Text tfeScore = new Text();
    	tfeScore.setText("2048\n--------------------\nHigh Score: " + String.valueOf(currentPlayer.retrieveData().getTfeHighScore()));
    	
    	Button close = new Button("Go Back");
    	
    	close.setOnAction(e -> {
    		clearScreen();
    		showMenu();
    	});
    	
    	window.add(tfeScore, 0, 1);
    	window.add(bejeweledScore, 1, 1);
    	window.add(close, 0, 3);
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
    	
    	Button close = new Button("Go Back");
    	
    	close.setOnAction(e -> {
    		clearScreen();
    		showLoginScreen();
    	});
    	
    	window.add(tfeScore, 0, 1);
    	window.add(bejeweledScore, 1, 1);
    	window.add(close, 0, 3);
    }
    
    private void showMenu() {
    	Text welcome = new Text(String.format("Welcome, %s!", currentPlayer.getName()));
    	welcome.setFont(Font.font(20));
    	window.add(welcome, 0, 0);
    	window.add(playBejeweled, 0, 1);
    	window.add(playTFE, 1, 1);
    	window.add(viewHighScores, 0, 2);
    	window.add(logout, 0, 3);
    	window.add(settings, 1, 2);
    }
    
    public void onEndGame() {
    	if(isMultiplayer && player2 == null) {
    		secondPlayerTurn();
    	}
    	else if(isMultiplayer && player2 != null) {
    		winnerScreen();
    	}
    	else {
        	clearScreen();
        	showMenu();
    	}
    }
    
    private void clearScreen() {
    	window.getChildren().clear();
    }
    
    private void secondPlayerTurn() {
    	clearScreen();
    	
    	Function<Integer, Integer> endGame = (e) -> {
            onEndGame();
            return e;
        };
    	
        Label p2NameLabel = new Label("Enter Player 2's Name:");
    	Button goHome = new Button("Go Home");
    	
    	// get new player name
		Button start = new Button("Start!");
		TextField input = new TextField();
    	input.setPromptText("Enter Second Player Name");
    	
    	goHome.setOnAction(e -> {
    		clearScreen();
    		showMenu();
    	});
    	
    	if(currentGame == 0) {
    		start.setOnAction(e -> {
        		// set second player
        		String name = input.getText();
    			player2 = new PlayerData(name);
        		
        		// check if player exists
        		if(!players.containsKey(name)){
            		players.put(name, player2);
        		}
    			
    			Label label = new Label("It's now " + player2.getName() + "'s turn!");
    			label.setFont(Font.font(20));
    			
    			// for second player
    			clearScreen();
        		System.out.println("run tge");
    			TFE tfe = new TFE(player2, endGame);
    			GridPane tfe_board = tfe.createGame();
    			window.add(tfe_board, 0, 2);
    			window.add(label, 0, 3);
        	});
    	}
    	else if(currentGame == 1) {
    		start.setOnAction(e -> {
        		// set second player
        		String name = input.getText();
    			player2 = new PlayerData(name);
        		
        		// check if player exists
        		if(!players.containsKey(name)){
            		players.put(name, player2);
        		}
    			
    			Label label = new Label("It's now " + player2.getName() + "'s turn!");
    			label.setFont(Font.font(20));
    			
    			// for second player
    			clearScreen();
        		System.out.println("run tge");
        		Bejeweled bejeweled = new Bejeweled(player2, endGame);
    	    	GridPane bejeweled_board = bejeweled.createGame();
    	    	window.add(bejeweled_board, 0, 2);
    			window.add(label, 0, 3);
        	});
    	}
    	window.add(p2NameLabel, 0, 0);
    	window.add(input, 0, 1);
    	window.add(start, 1, 1);
    	window.add(goHome, 0, 2);
    }
    
    private void startGame(GameType type) {
    	clearScreen();
    	
    	Function<Integer, Integer> endGame = (e) -> {
            onEndGame();
            return e;
        };
    
        Button singleplayer = new Button("Singleplayer");
    	Button multiplayer = new Button("Multiplayer");
    	Button back = new Button("Go Back");
    	
    	switch(type) {
    		case TFE:
    	    	singleplayer.setOnAction(e -> {
    	    		clearScreen();
    	    		System.out.println("run tge");
    				TFE tfe = new TFE(currentPlayer, endGame);
    				GridPane tfe_board = tfe.createGame();
    				window.add(tfe_board, 0, 2);
    	    	});
    			
    	    	multiplayer.setOnAction(e -> {
    	    		clearScreen();
    	    		isMultiplayer = true;
    	    		currentGame = 0;
    	    		
    	    		Label label = new Label(currentPlayer.getName() + " is going first!");
    	    		label.setFont(Font.font(20));
    	    		window.add(label, 0, 3);
    	    		
    	    		System.out.println("run tge multiplayer");
    	    		TFE tfe = new TFE(currentPlayer, endGame);
    				GridPane tfe_board = tfe.createGame();
    				window.add(tfe_board, 0, 2);
    	    	});
    	    	
    	    	window.add(singleplayer, 0, 0);
    	    	window.add(multiplayer, 1, 0);
    	    	
    			break;
    			
    		case BEJEWELED:
    	    	singleplayer.setOnAction(e -> {
    	    		clearScreen();
    	    		System.out.println("run bewjeweled");
    	    		Bejeweled bejeweled = new Bejeweled(currentPlayer, endGame);
    		    	GridPane bejeweled_board = bejeweled.createGame();
    		    	window.add(bejeweled_board, 0, 2);
    	    	});
    			
    	    	multiplayer.setOnAction(e -> {
    	    		clearScreen();
    	    		isMultiplayer = true;
    	    		currentGame = 1;
    	    		
    	    		Label label = new Label(currentPlayer.getName() + " is going first!");
    	    		label.setFont(Font.font(20));
    	    		window.add(label, 0, 3);
    	    		
    	    		System.out.println("run tge multiplayer");
    	    		Bejeweled bejeweled = new Bejeweled(currentPlayer, endGame);
    		    	GridPane bejeweled_board = bejeweled.createGame();
    		    	window.add(bejeweled_board, 0, 2);
    	    	});
    	    	
    	    	window.add(singleplayer, 0, 0);
    	    	window.add(multiplayer, 1, 0);
    			break;
    	}
    	back.setOnAction(e -> {
    		clearScreen();
    		showMenu();
    	});
    	
    	window.add(back, 0, 1);
    }
    
    private void winnerScreen() {
    	clearScreen();
    	
    	Button close = new Button("Go Back to Main Menu");
    	
    	close.setOnAction(e -> {
    		player2 = null;
    		isMultiplayer = false;
    		currentGame = -1;
    		clearScreen();
    		showMenu();
    	}); 
    	
    	Label winner = new Label();
    	winner.setFont(Font.font(20));
    	
    	if(currentGame == 0) { // multiplayer game is tfe
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
    	
    	window.add(winner, 0, 1);
    	window.add(close, 0, 2);
    }
    
    private void initializeButtons() {
    	
    	playTFE.setOnAction(e -> {
    		startGame(GameType.TFE);
    	});
    	
    	playBejeweled.setOnAction(e -> {
    		startGame(GameType.BEJEWELED);
    	});
    	
    	viewHighScores.setOnAction(e -> {
    		clearScreen();
    		showHighScores();
    	});
    	
    	logout.setOnAction(e -> {
    		currentPlayer = null;
    		clearScreen();
    		showLoginScreen();
    	});
    	
    	settings.setOnAction(e -> {
    		clearScreen();
    		settingScreen();
    	});
    	
    }
    
    public static void main(String[] args) {
//    	System.out.println("GameManager");
        launch(args);
    }

}
