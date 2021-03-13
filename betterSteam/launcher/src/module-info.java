module launcher {
	requires transitive tfe;
	requires transitive bejeweled;
//	requires tmge;
	
	
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires tmge;

	opens launcher to javafx.fxml;
	
	exports launcher;
	
}