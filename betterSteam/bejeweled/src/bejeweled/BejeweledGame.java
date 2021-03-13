package bejeweled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import java.util.Random;

// i believe these are just iterators
import java.util.stream.Collectors;
//import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
//import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BejeweledGame extends Application {

    static final int W = 8; // rows
    static final int H = 8; // columns
    static final int SIZE = 50; //pixels size of jewel

    static Color[] colors = new Color[] {
            Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW
    };

    private Jewel selected = null;
    private List<Jewel> jewels = new ArrayList<Jewel>();

    private IntegerProperty score = new SimpleIntegerProperty();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent(), 800, 800));
        primaryStage.setTitle("Bejeweled");
        primaryStage.show();
    }
    
    private Parent createContent() {
    	GridPane root = new GridPane();
    	root.setPadding(new Insets(20, 20, 20, 20));

        Pane board = new Pane();
        board.setPrefSize(W * SIZE, H * SIZE);
//        root.setPrefSize(W * SIZE, H * SIZE+ 150);
        
        System.out.println("createContent: " );
        
        for (int i=0; i < W * H; i++ ) {
//        	Point2D takes in x,y coords
        	
        	Jewel new_jewel = new Jewel(new Point2D(i % W, i / W));
        	System.out.println("new_jewel at row " +  new_jewel.getRow() + ", col " + new_jewel.getColumn());

        	new_jewel.setOnMouseClicked(event -> {
        		System.out.println("jewel " +  new_jewel.getColor());;
                if (selected == null) {
                    selected = new_jewel;
                }
                else {
                    swap(selected, new_jewel);
                    checkState();
                    selected = null;
                }
            });
        	jewels.add(new_jewel);
        }

        System.out.println("jewels: " + jewels.size() + " " + jewels);
        board.getChildren().addAll(jewels);

        Text title = new Text("Bejeweled");
        Text textScore = new Text();
        
        title.setFont(Font.font(64));
        textScore.setFont(Font.font(44));
        textScore.textProperty().bind(score.asString("Score: [%d]"));

//        add(item, colInd, rowInd)
        root.add(title, 0, 0);
        root.add(board, 0, 1);
        root.add(textScore, 0, 2);
//        root.getChildren().add(textScore);
        return root;
    }

    private void checkState() {
        Map<Integer, List<Jewel>> rows = jewels.stream().collect(Collectors.groupingBy(Jewel::getRow));
        Map<Integer, List<Jewel>> columns = jewels.stream().collect(Collectors.groupingBy(Jewel::getColumn));

        rows.values().forEach(this::checkCombo);
        columns.values().forEach(this::checkCombo);
    }

    private void checkCombo(List<Jewel> jewelsLine) {
        Jewel jewel = jewelsLine.get(0);
        long count = jewelsLine.stream().filter(j -> j.getColor() != jewel.getColor()).count();
        if (count == 0) {
            score.set(score.get() + 1000);
            jewelsLine.forEach(Jewel::randomize);
        }
    }

    private void swap(Jewel a, Jewel b) {
        Paint color = a.getColor();
        a.setColor(b.getColor());
        b.setColor(color);
    }


    public static void main(String[] args) {
        launch(args);
    }
}