package launcher;

import tfe.TFE;
import tmge.PlayerData;
import bejeweled.Bejeweled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GameManager  extends Application {
	private HashMap<String, PlayerData> players = new HashMap<String, PlayerData>();
	private PlayerData currentPlayer;
	
	GridPane window = new GridPane();
	Button playTFE = new Button("2048");
	Button playBejeweled = new Button("Bejeweled");
	Button viewHighScores = new Button("View Scores");
	Button switchPlayer = new Button("Switch Player");
	
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
    	    	
    	window.add(label, 0, 0);
    	window.add(input, 0	, 1);
    	window.add(submit, 1, 1);
    	
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
    	String bejeweledText = "Bejeweled\n--------------------\n";
    	String tfeText = "2048\n--------------------\n";
    	
    	Iterator playersIt = players.entrySet().iterator(); 
    	
    	
    	// Iterate through hashmap
    	
    	Text bejeweledScore = new Text();
    	bejeweledScore.setText(bejeweledText);
    	
    	Text tfeScore = new Text();
    	tfeScore.setText(tfeText);
    	
    	Button close = new Button("Go Back");
    	
    	close.setOnAction(e -> {
    		clearScreen();
    		showMenu();
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
    	window.add(playBejeweled, 0, 0);
    	window.add(playTFE, 1, 0);
    	window.add(viewHighScores, 0, 1);
    	window.add(switchPlayer, 1, 1);
    	
    }
    
    private void clearScreen() {
    	window.getChildren().clear();
    }
    
    private void startGame(GameType type) {
    	clearScreen();
    	
    	switch(type) {
    		case TFE:
    			System.out.println("run tge");
    			TFE tfe = new TFE(currentPlayer);
    			GridPane tfe_board = tfe.createGame();
    			tfe.addKeyPressListeners(tfe_board);
    			
    			window.add(tfe_board, 0, 2);
    			break;
    			
    		case BEJEWELED:
    			System.out.println("run bejeweled");
    			
    			Bejeweled bejeweled = new Bejeweled(currentPlayer);
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
    	
    	switchPlayer.setOnAction(e -> {
//    		clearScreen();
    		changePlayer();
    	});
    	
    	
    }
    
    public static void main(String[] args) {
//    	System.out.println("GameManager");
        launch(args);
    }

}
