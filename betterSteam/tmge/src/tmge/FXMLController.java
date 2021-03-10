package tmge;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FXMLController {
    
    @FXML
    private Label label;
    
    
//    similar to a constructor for the view, runs after fxml view loads
    public void initialize() {
    	System.out.println("FXMLController");
    }    
}