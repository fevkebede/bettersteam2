module tfe {
	requires transitive tmge;
    
	requires javafx.controls;

	opens tfe to javafx.fxml;
	
    exports tfe;
}