package tmge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));

        stage.setTitle("Better Steam");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
        
    }

    
    public static void main(String[] args) {
    	System.out.println("MainApp");
        launch(args);
        
//	     Game test = new Game();
//	     System.out.println(test.getGrid().printGrid());
    }
}
