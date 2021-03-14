package launcher;

import tfe.TFE;
import tmge.PlayerData;
import bejeweled.Bejeweled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    	
    	Label label = new Label("Enter player name");
    	TextField input = new TextField();
    	input.setPromptText("Name");

    	Button submit = new Button("Submit");
    	Button highScores = new Button("High Scores");
    	
    	submit.setOnAction(e -> {
    		
    		String name = input.getText();
    		System.out.println(name);
    		
    		// check if player exists
    		if(players.containsKey(name)) {
    			currentPlayer = players.get(name);
    		}
    		// add new player to map
    		else {
        		currentPlayer = new PlayerData(input.getText());
        		players.put(name, currentPlayer);
    		}
    		
    		clearScreen();
    		showMenu();
    	});
    	
    	highScores.setOnAction(e -> {
    		clearScreen();
    		showAllHighScores();
    	});
    	    	
    	window.add(label, 0, 0);
    	window.add(input, 0	, 1);
    	window.add(submit, 1, 1);
    	window.add(highScores, 0, 2);
    	
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
    	
    	for (Map.Entry mapElement : players.entrySet()) { 
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
    
    private void changePlayer() {
//    	TODO
    	System.out.println("change player screen");
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
    	clearScreen();
    	showMenu();
    }
    
    private void clearScreen() {
    	window.getChildren().clear();
    }
    
    private void startGame(GameType type) {
    	clearScreen();
    	
    	Function<Integer, Integer> endGame = (e) -> {
            onEndGame();
            return e;
        };
    
    	switch(type) {
    		case TFE:
    			System.out.println("run tge");
    			TFE tfe = new TFE(currentPlayer, endGame);
    			GridPane tfe_board = tfe.createGame();
//    			tfe.addKeyPressListeners(tfe_board);
    			
    			window.add(tfe_board, 0, 2);
    			break;
    			
    		case BEJEWELED:
    			System.out.println("run bejeweled");
    			
    			Bejeweled bejeweled = new Bejeweled(currentPlayer, endGame);
    	    	GridPane bejeweled_board = bejeweled.createGame();
    	    	
    	    	window.add(bejeweled_board, 0, 2);
    			break;
    	}
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
