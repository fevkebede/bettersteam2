module tfe {
	requires transitive tmge;
    
//	requires transitive javafx.graphics;
//	requires javafx.controls;
//	requires javafx.fxml;
//	requires javafx.base;

	opens tfe to javafx.fxml;
	
    exports tfe;
}