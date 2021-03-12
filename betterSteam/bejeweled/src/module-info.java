module bejeweled {
	requires tmge;

	requires transitive javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;

	opens bejeweled to javafx.fxml;

	exports bejeweled;
}
