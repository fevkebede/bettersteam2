module tetris {

//    requires tmge;
    
    requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	
	opens tetris to javafx.fxml;
	
	exports tetris;
	
}