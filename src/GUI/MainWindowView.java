package GUI;

import GameSetup.GameType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainWindowView extends Application {

    private Stage window;
    private Scene scene;

    // Panes
    private BorderPane root;
    private VBox vBox;
    private HBox hBox;
    // Labels
    private Label titleLabel;
    // private Label choiceLabel;
    // Buttons
    private Button beginnerButton;
    private Button interButton;
    private Button expertButton;
    // css styleSheet
    private String styleString;


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // set CSS style sheet
        styleString = getClass().getResource("Components/Style.css").toExternalForm();


        window = stage;
        window.setMinWidth(700);
        window.setMinHeight(700);

        root = new BorderPane();

        vBox = new VBox();
        hBox = new HBox();
        // set IDs for css
        vBox.setId("startPane");
        hBox.setId("choicePane");
        vBox.getStylesheets().add(styleString);
        hBox.getStylesheets().add(styleString);

        titleLabel = new Label("Minesweeper");
        titleLabel.setId("titleLabel");
        titleLabel.getStylesheets().add(styleString);


        beginnerButton = new Button("BEGINNER");
        interButton = new Button("INTERMEDIATE");
        expertButton = new Button("EXPERT");
        Button jokeButton = new Button("Joke");

        beginnerButton.setOnAction(e -> createGame(GameType.BEGINNER));
        interButton.setOnAction(e -> createGame(GameType.INTERMEDIATE));
        expertButton.setOnAction(e -> createGame(GameType.EXPERT));
        jokeButton.setOnAction(e -> createGame(GameType.JOKE));

        beginnerButton.getStyleClass().add("difficulty-Buttons");
        interButton.getStyleClass().add("difficulty-Buttons");
        expertButton.getStyleClass().add("difficulty-Buttons");

        expertButton.getStylesheets().add(styleString);
        interButton.getStylesheets().add(styleString);
        beginnerButton.getStylesheets().add(styleString);

        hBox.getChildren().addAll(beginnerButton,interButton,expertButton, jokeButton);
        vBox.getChildren().addAll(titleLabel, hBox);

        root.setCenter(vBox);

        scene = new Scene(root);

        window.setScene(scene);

        window.show();
    }

    public void createGame(GameType difficulty) {
        Pair<Integer,Integer> numberSquares = difficulty.getDimensions();
        
        double sqrWidth =  Math.min(Math.max((window.getWidth() - 50) / numberSquares.getKey(), 30),60);
        double sqrHeight = Math.min(Math.max((window.getHeight() - 50) / numberSquares.getValue(), 30),60);
        GameView gameView = new GameView(difficulty, styleString, sqrWidth, sqrHeight, this);
        root.setCenter(gameView.getPane());
    }

    public void setMainWindow() {
        root.setCenter(vBox);
    }

    public void setSize(double width, double height){
        System.out.println(width + "  " + height);
        window.setWidth(width);
        window.setHeight(height);

    }

}
