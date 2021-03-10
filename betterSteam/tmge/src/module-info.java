module tmge {
    
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	
	opens tmge to javafx.fxml;
	
	exports tmge;
	
}