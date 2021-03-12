module bejeweled {
	requires tmge;

	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;

	opens bejeweled to javafx.fxml;

	exports bejeweled;
}
